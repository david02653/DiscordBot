package david.msabot.discordbot.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.*;
import david.msabot.discordbot.Entity.Eureka.Application;
import david.msabot.discordbot.Entity.Eureka.EurekaResponse;
import david.msabot.discordbot.Entity.Eureka.Instance;
import david.msabot.discordbot.Entity.Jenkins.JenkinsTestCase;
import david.msabot.discordbot.Entity.Jenkins.JenkinsTestReport;
import david.msabot.discordbot.Entity.Jenkins.JenkinsTestSuite;
import david.msabot.discordbot.Entity.Rasa.IntentSet;
import david.msabot.discordbot.Exception.RequestFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * this class should be used to handle incoming messages in discord bot
 * send message via jdaMessage handler
 * check intent by using rasa service
 * find question by using additional question service
 *
 * note: need to pass JDA instance via parameter, in case needs to use jda message service, etc
 */
@Service
public class IntentHandleService {
    // todo: consider add message report to some kind of master service to manage all bots

    /* endpoint path from properties */
    private final String zuulEndpoint;
    private final String rasaEndpoint;
    private final String jenkinsEndpoint;
    private final String eurekaEndpoint;
    private final String jenkinsUser;
    private final String jenkinsPw;
    private final String jenkinsToken;

    /* required spring managed service */
    private final RasaService rasaService;
    private final AdditionalQAService additionalQAService;

    // todo: jdaMessageHandle, jdaMemberHandle: future feature, may or may not be updated
    /* required service */
    private JDAMessageHandler jdaMessageHandler;
    private JDAMemberHandle jdaMemberHandler;

    @Autowired
    public IntentHandleService(Environment env, RasaService rasa, AdditionalQAService aqa){
        this.zuulEndpoint = env.getProperty("env.setting.zuul.url");
        this.rasaEndpoint = env.getProperty("env.setting.rasa.v2.url");
        this.jenkinsEndpoint = env.getProperty("env.setting.jenkins.url");
        this.eurekaEndpoint = env.getProperty("env.setting.eureka.url");
        this.jenkinsUser = env.getProperty("env.setting.jenkins.user");
        this.jenkinsPw = env.getProperty("env.setting.jenkins.pw");
        this.jenkinsToken = env.getProperty("env.setting.jenkins.token");

        this.rasaService = rasa;
        this.additionalQAService = aqa;
    }


    /**
     * check if intent object contains service_name
     * otherwise, do stuffs with object intent
     *
     * note: implements stage_check_intent in MSABot
     * @param set target intent set object
     * @return correspond result
     */
    public String checkIntent(IntentSet set) throws Exception {
        String intent = set.getIntent();
        String service = set.getService();

        /* bot help and intent with no service dependency */
        switch (intent){
            case "bot_help":
                return botHelp();
            case "action_service_env":
                return requestServiceEnv();
            case "action_service_health":
                return requestHealthData();
            default:
                break;
        }
        /* no service name found */
        if(service == null || service.equals("") || service.equals("none"))
            return "service name required but no service name found.";
        /* check intent with service dependency */
        switch (intent){
            case "action_detail_api": // missing definition in MSABot, check it out
                break;
            case "action_connect_error":
                return connectError(service);
            case "action_build_fail":
//                return requestLastFailedBuildStatus(service);
                return requestLastTestCaseDetail(service);
            case "action_service_info":
                return requestServiceInfo(service);
            case "action_service_using_info":
                return requestServiceUsingInfo(service);
            case "action_service_api_list":
                return requestApiList(service);
            default:
                break;
        }
        return "We have no idea what are you talking about, please consider ask again in another way.";
    }

    /**
     * create user guide message
     * build an embed message and return
     * note: implements action_bot_help in MSABot
     * @return help message
     */
    public String botHelp(){
        return "1. Search the service health data.\n" +
                "2. Search the service's information.\n" +
                "3. Search the service's overview.\n" +
                "4. Search the service's api list.\n" +
                "5. Search the env setting.\n" +
                "6. Search the build data on Jenkins.\n" +
                "~~7. Search the connection status on Eureka.~~ \n" +
                "~~8. Get the dependency graph from VMAMV.~~ ";
    }
//    public MessageEmbed botHelp(){
//        EmbedBuilder embedBuilder = new EmbedBuilder();
//        embedBuilder.setAuthor("Master Bot");
//        embedBuilder.setColor(Color.WHITE);
//        embedBuilder.setTitle("How to use :thinking:");
//        String helpMsg =
//                "1. Search the service health data.\n" +
//                "2. Search the service's information.\n" +
//                "3. Search the service's overview.\n" +
//                "4. Search the service's api list.\n" +
//                "5. Search the env setting.\n" +
//                "6. Search the build data on Jenkins.\n" +
//                "7. Search the connection status on Eureka.\n" +
//                "8. Get the dependency graph from VMAMV.";
//        embedBuilder.setDescription(helpMsg);
//        embedBuilder.setTimestamp(Instant.now());
//        embedBuilder.setFooter("bot help");
//        return embedBuilder.build();
//    }

    /**
     * request eureka endpoint to get app data (xml)
     * parse xml to extract target information
     * note: implements action_service_health in MSABot, Eureka service
     * @throws JsonProcessingException error parsing eureka xml response
     */
    public String requestHealthData() throws JsonProcessingException, Exception {
        String url = eurekaEndpoint + "/eureka/apps";
        String rawData = "";
        ResponseEntity<String> response = fireRestExchange(url, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_XML_VALUE)));
        rawData = response.getBody();
        if(rawData == null || response.getStatusCodeValue() != 200){
            /* api request failed somehow, do something */
            System.out.println("[DEBUG][healthData] eureka api request failed or return with null body.");
            throw new Exception();
        }
        XmlMapper mapper = new XmlMapper();
        EurekaResponse result = mapper.readValue(replaceReServed(rawData), EurekaResponse.class);
        HashMap<String, Instance> map = eurekaMapSerialize(result);

        /* build result string in to arrayList */
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for(Map.Entry<String, Instance> entry: map.entrySet()){
            String name = entry.getKey();
            Instance instance = entry.getValue();
            builder.append(++count);
            builder.append(".[");
            builder.append(name);
            builder.append("]: ");
            builder.append(instance.getStatus());
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * request service api list from zuul endpoint
     * use zuul and swagger
     * note: implements action_service_api_list in MSABot
     * @param service target service name
     * @return embed message
     */
    public String requestApiList(String service){
        String url = zuulEndpoint + "/" + service + "/v2/api-docs/";
        System.out.println("[DEBUG][requestApiList][url]: " + url + "");
        ResponseEntity<String> response = null;
        try {
            response = fireRestExchange(url, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE)));
        } catch (RequestFailException e) {
            return "Something goes wrong with zuul api request.";
        }
        /* use Gson, parse response json string into JsonObject */
        Gson gson = new Gson();
        JsonObject zuulResp = gson.fromJson(response.getBody(), JsonObject.class);
        /* extract target information */
        String title = zuulResp.getAsJsonObject("info").get("title").getAsString();
        String contactName = zuulResp.getAsJsonObject("info").getAsJsonObject("contact").get("name").getAsString();
        JsonObject tag = zuulResp.getAsJsonArray("tags").get(0).getAsJsonObject();
        String tagName = tag.get("name").getAsString();
        String tagDescription = tag.get("description").getAsString();
        JsonObject pathInfo = zuulResp.getAsJsonObject("paths");
        /* build result */
        StringBuilder builder = new StringBuilder();
        builder.append(title).append(" : ").append(tagName).append("\n");
        builder.append("(").append(tagDescription).append(")\n");
        builder.append("If this service went some problems, please call ").append(contactName).append("\n");
        builder.append("Some api intro : \n");
        for(Map.Entry<String, JsonElement> entry: pathInfo.entrySet()){
            String pathToken = entry.getKey();
            String apiSummary = entry.getValue().getAsJsonObject().getAsJsonObject("get").get("summary").getAsString();
            builder.append(zuulEndpoint).append("/").append(service).append(pathToken).append(" : ")
                    .append(apiSummary).append("\n");
        }
        builder.append("See detail information on Swagger. ")
                .append(zuulEndpoint).append("/").append(service).append("/swagger-ui.html").append("\n");
        builder.append("(The data was collected from your Zuul server and Swagger api.)");
        return builder.toString();
    }

    /**
     * request system environment setting from eureka endpoint
     * note: implements action_service_env in MSABot, Eureka service
     * @return result information
     */
    public String requestServiceEnv(){
        String url = eurekaEndpoint + "/env/";
        System.out.println("[DEBUG][requestServiceEnv][url]: " + url + "");
        ResponseEntity<String> response = null;
        try {
            response = fireRestExchange(url, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE)));
        } catch (RequestFailException e) {
            return "Something goes wrong with eureka api request.";
        }
        /* use Gson, parse response json string into JsonObject */
        Gson gson = new Gson();
        JsonObject eurekaResp = gson.fromJson(response.getBody(), JsonObject.class);
        /* extract target information */
        String targetPropertyName = "applicationConfig: [classpath:/application.properties]";
        JsonObject targetSection = eurekaResp.getAsJsonObject(targetPropertyName);
        /* build result string */
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, JsonElement> entry: targetSection.entrySet()){
            String propertyName = entry.getKey();
            String propertyValue = entry.getValue().getAsString();
            builder.append("The env ").append(propertyName).append(" is : ").append(propertyValue).append("\n");
        }
        return builder.toString();
    }

    /**
     * request service detail information from zuul endpoint
     * this api will need a service name in lower case
     * note: implements action_service_info in MSABot, Zuul and Actuator service
     * @param service target service name
     * @return result information
     */
    public String requestServiceInfo(String service){
        String url = zuulEndpoint + "/" + service.toLowerCase() + "/info/";
        System.out.println("[DEBUG][requestServiceInfo][url]: " + url + "");
        ResponseEntity<String> response = null;
        try {
            response = fireRestExchange(url, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE)));
        } catch (RequestFailException e) {
            return "Something goes wrong with Zuul api request.";
        }
        /* use Gson, parse response json string into JsonObject */
        Gson gson = new Gson();
        JsonObject resp = gson.fromJson(response.getBody(), JsonObject.class);
        /* extract target information */
        String version = resp.get("version").getAsString();
        String group = resp.getAsJsonObject("build").get("group").getAsString();
        JsonObject gitInfo = resp.getAsJsonObject("git");
        String remoteUrl = gitInfo.getAsJsonObject("remote").getAsJsonObject("origin").get("url").getAsString();
        String branch = gitInfo.get("branch").getAsString();
        String user = gitInfo.getAsJsonObject("commit").getAsJsonObject("user").get("name").getAsString();
        String userMail = gitInfo.getAsJsonObject("commit").getAsJsonObject("user").get("email").getAsString();
        String commitCount = gitInfo.getAsJsonObject("total").getAsJsonObject("commit").get("count").getAsString();
        /* build result string */
        String builder = "Information found on service " + service + ".\n" +
                "Version: " + version + "\n" +
                "Group: " + group + "\n" +
                "Git URL: " + remoteUrl + "\n" +
                "Git branch: " + branch + "\n" +
                "Git user: " + user + "\n" +
                "(If this service has some problems contact him/her by : " + userMail + "\n" +
                "Total commit count: " + commitCount + "\n" +
                "See detail information on Swagger! : " + zuulEndpoint + "/" + service + "/swagger-ui.html" + "\n" +
                "(The data was collected from your Zuul server and Actuator api.)";
        return builder;
    }

    /**
     * request for service using_info
     * note: implements action_service_using_info in MSABot, Zuul Swagger and Actuator api
     * @param service target service name
     * @return result message
     */
    public String requestServiceUsingInfo(String service){
        String serviceUrl = zuulEndpoint + "/" + service + "/v2/api-docs/";
        String traceUrl = zuulEndpoint + "/" + service + "/trace";
        System.out.println("[DEBUG][requestServiceUsingInfo][serviceUrl]: " + serviceUrl + "");
        System.out.println("[DEBUG][requestServiceUsingInfo][traceUrl]: " + traceUrl + "");
        ResponseEntity<String> serviceInfoResp = null;
        ResponseEntity<String> traceResp = null;
        try{
            serviceInfoResp = fireRestExchange(serviceUrl, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE)));
            traceResp = fireRestExchange(traceUrl, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE)));
        }catch (RequestFailException e){
            return "Something goes wrong with Zuul api request.";
        }
        Gson gson = new Gson();
        JsonObject serviceInfo = gson.fromJson(serviceInfoResp.getBody(), JsonObject.class);
        JsonArray traceInfo = gson.fromJson(traceResp.getBody(), JsonArray.class);
        /* extract information from json object */
        // loop serviceInfo to get all api list
        ArrayList<String> apiList = new ArrayList<>();
        HashMap<String, String> traceList = new HashMap<>();
        HashMap<String, Integer> resultList = new HashMap<>();
        JsonObject serviceApi = serviceInfo.getAsJsonObject("paths");
        for(Map.Entry<String, JsonElement> entry: serviceApi.entrySet())
            apiList.add(entry.getKey());
        for(JsonElement ele: traceInfo){
            traceList.put(ele.getAsJsonObject().getAsJsonObject("info").get("path").getAsString(), ele.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("headers").getAsJsonObject("response").get("status").getAsString());
        }
        System.out.println(apiList);
        // compare with traceInfo to get what we are after
        int totalCount = 0;
        for(Map.Entry<String, String> ele: traceList.entrySet()){
            System.out.println("[trace][" + ele.getKey() + "][" + ele.getValue() + "]");
            String pathName = ele.getKey();
            String httpStatus = ele.getValue();
            if(apiList.contains(pathName)){
                if(!resultList.containsKey(httpStatus))
                    resultList.put(httpStatus, 1);
                else
                    resultList.put(httpStatus, resultList.get(httpStatus) + 1);
                totalCount++;
            }
        }
        if(resultList.size() == 0){
            return "Seems like this service has no usage record. The total using amount is : 0";
        }
        // create chart parameter
        StringBuilder labelName = new StringBuilder();
        StringBuilder labelValue = new StringBuilder();
        for(Map.Entry<String, Integer> ele: resultList.entrySet()){
            labelName.append(ele.getKey()).append(",");
            labelValue.append(ele.getValue()).append(",");
        }
        labelName = new StringBuilder(labelName.substring(0, labelName.length() - 1));
        labelValue = new StringBuilder(labelValue.substring(0, labelValue.length() - 1));
        /* build result message */
        String builder = "The total using amount is : " + totalCount + "\n" +
                "See the pie chart I prepared for you! \n" +
                "https://quickchart.io/chart?c={type:'pie',data:{labels:[" + labelName + "],datasets:[{data:[" + labelValue + "]}]}}";
        return builder;
    }

    /**
     * send the service's connection error data back
     * note: implements action_connect_error in MSABot, Eureka api
     * @param service target service name
     * @return result message
     */
    public String connectError(String service){
        return "Here is service \"" + service + "\"'s action_connect_error.";
    }

    /**
     * fire request to jenkins api to access job related information
     * this method gathers information about last failed build
     * @param service target service name
     * @return build information
     */
    public String requestLastFailedBuildStatus(String service){
        // api format -> http(s)://<user>:<pw>@<server-host>/job/<service-name>/api/json
        String url = jenkinsEndpoint + "/job/" + service + "/api/json";
        System.out.println("[DEBUG][requestLastFailedBuildStatus][url]: " + url + "");
        ResponseEntity<String> response = null;
        try {
            response = fireBasicAuthRequest(url, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE)));
        } catch (RequestFailException e) {
            return "Something goes wrong with jenkins api request.";
        }
        Gson gson = new Gson();
        JsonObject jenkinsObj = gson.fromJson(response.getBody(), JsonObject.class);
        StringBuilder builder = new StringBuilder();
        if(jenkinsObj.get("lastFailedBuild") == null)
            builder.append("There is no failed build record.");
        else{
            String buildNumber = jenkinsObj.getAsJsonObject("lastFailedBuild").get("number").getAsString();
            String serviceDisplayName = jenkinsObj.get("fullDisplayName").getAsString();
            builder.append("Service[").append(serviceDisplayName).append("]'s last build is build").append(buildNumber).append("\n");
            builder.append("Check it on your Jenkins server").append(jenkinsEndpoint).append("/job/").append(serviceDisplayName).append("/").append(buildNumber).append("/");
        }
        return builder.toString();
    }

    /**
     * fire request to jenkins api to access specific job information, including build number and test case details
     * this method gathers information about test case details
     * @param service target service name
     * @return test case details
     */
    public String requestLastTestCaseDetail(String service){
        // build status api format -> http(s)://<user>:<pw>@<server-host>/job/<service-name>/api/json
        // build detail -> http(s)://<user>:<pw>@<server-host>/job/<service-name>/<build-number>/testReport/api/json
        try{
//            String[] hostToken = jenkinsEndpoint.split("//");
//            String statusUrl = hostToken[0] + "//" + jenkinsUser + ":" + token + "@" + hostToken[1] + "/job/" + service + "/api/json";
            String statusUrl = jenkinsEndpoint + "/job/" + service + "/api/json";
            System.out.println("[DEBUG][requestLastTestCaseDetail][statusUrl]: " + statusUrl + "");
            ResponseEntity<String> statusResp = fireBasicAuthRequest(statusUrl, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE)));
            Gson gson = new Gson();
            JsonObject statusObj = gson.fromJson(statusResp.getBody(), JsonObject.class);
            String buildNumber = statusObj.getAsJsonObject("lastBuild").get("number").getAsString();
//            String url = hostToken[0] + "//" + jenkinsUser + ":" + jenkinsPw + "@" + hostToken[1] + "/job/" + service + "/" + buildNumber + "/testReport/api/json";
            String url = jenkinsEndpoint + "/job/" + service + "/" + buildNumber + "/testReport/api/json";
            System.out.println("[DEBUG][requestLastTestCaseDetail][Url]: " + url + "");
            ResponseEntity<String> detailResp = fireBasicAuthRequest(url, HttpMethod.GET, new HashMap<>(Collections.singletonMap("Accept", MediaType.APPLICATION_JSON_VALUE)));
            JsonObject detailObj = gson.fromJson(detailResp.getBody(), JsonObject.class);
            StringBuilder builder = new StringBuilder();
            JsonArray suites = detailObj.get("suites").getAsJsonArray();
            int totalDuration = detailObj.get("duration").getAsInt();
            int totalFailedCount = detailObj.get("failCount").getAsInt();
            int totalPassedCount = detailObj.get("passCount").getAsInt();
            int totalSkipCount = detailObj.get("skipCount").getAsInt();
            JenkinsTestReport testReport = JenkinsTestReport.createJenkinsTestReport(totalDuration, totalFailedCount, totalPassedCount, totalSkipCount);
            testReport.setTestSuites(getTestSuiteDetails(suites));
            testReport.setReportDetailUrl(additionalQAService.insertMessage(testReport.getTestSuites().toString()));
            return testReport.toString();
        }catch (RequestFailException e){
            return "Something goes wrong wit jenkins api request.";
        }
    }

    /**
     * extract information from test suites
     * including suite name and suite duration
     * call another method to access detail test case information
     * @param testSuites JsonArray of test Suites
     * @return test suites information
     */
    public ArrayList<JenkinsTestSuite> getTestSuiteDetails(JsonArray testSuites){
        ArrayList<JenkinsTestSuite> suiteReports = new ArrayList<>();
        for(JsonElement element: testSuites){
            JsonObject suite = element.getAsJsonObject();
            int duration = suite.get("duration").getAsInt();
            String suiteName = suite.get("name").getAsString();
            JenkinsTestSuite temp = JenkinsTestSuite.createNewTestSuite(suiteName, duration);
            temp.setTestCases(getTestCaseDetails(suite.get("cases").getAsJsonArray()));
            suiteReports.add(temp);
        }
        return suiteReports;
    }

    /**
     * extract detail information from test report json array
     * this method will extract test name, test duration, error detail , error stack trace anb test status
     * note that error detail and error stack trace might be null
     * @param testCases
     * @return
     */
    public ArrayList<JenkinsTestCase> getTestCaseDetails(JsonArray testCases){
        JenkinsTestCase testCaseTemplate;
        ArrayList<JenkinsTestCase> report = new ArrayList<>();
        for(JsonElement element: testCases){
            JsonObject testCase = element.getAsJsonObject();
            int duration = testCase.get("duration").getAsInt();
            String testName = testCase.get("name").getAsString();
            String details;
            String stackTrace;
            if(testCase.get("errorDetails").isJsonNull())
                details = null;
            else
                details = testCase.get("errorDetails").getAsString();
            if(testCase.get("errorStackTrace").isJsonNull())
                stackTrace = null;
            else
                stackTrace = testCase.get("errorStackTrace").getAsString();
            String status = testCase.get("status").getAsString();

            if(details == null)
                details = "non";
            // cut stack trace message
            if(stackTrace == null)
                stackTrace = "non";
            else if(stackTrace.contains("Exception"))
                stackTrace = stackTrace.split("Exception")[0] + "Exception";
            testCaseTemplate = JenkinsTestCase.createNewTestCase(testName, duration, details, stackTrace, status);
            report.add(testCaseTemplate);
        }
        return report;
    }

    /**
     * fire http request using RestTemplate
     * this method use RestTemplate.exchange()
     * @param httpMethod method type: get, post, etc.
     * @param headerList required header list
     * @return responseBody from exchange
     */
    public static ResponseEntity<String> fireRestExchange(String url, HttpMethod httpMethod, HashMap<String, String> headerList) throws RequestFailException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headerList.forEach(headers::set);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = null;
        try{
            result  = template.exchange(url, httpMethod, entity, String.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new RequestFailException();
        }
//        return template.exchange(url, httpMethod, entity, String.class);
        return result;
    }

    private ResponseEntity<String> fireBasicAuthRequest(String url, HttpMethod httpMethod, HashMap<String, String> headerList) throws RequestFailException{
        RestTemplate template = new RestTemplate();
        RestTemplateBuilder templateBuilder = new RestTemplateBuilder();
        template = templateBuilder.basicAuthentication(jenkinsUser, jenkinsToken).build();
        HttpHeaders headers = new HttpHeaders();
        headerList.forEach(headers::set);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = null;
        try{
            result  = template.exchange(url, httpMethod, entity, String.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new RequestFailException();
        }
//        return template.exchange(url, httpMethod, entity, String.class);
        return result;
    }

    /**
     * replace reserved words in the source string
     * @param source string format xml
     * @return cleaned xml string
     */
    private String replaceReServed(String source){
        return source.replace("class=", "source=");
    }

    /**
     * serialize eureka bean to hashmap
     * @param raw eureka bean
     * @return hashmap
     */
    private HashMap<String, Instance> eurekaMapSerialize(EurekaResponse raw){
        ArrayList<Application> list = raw.getAppList();
        HashMap<String, Instance> map = new HashMap<>();
        for (Application app : list) {
            map.put(app.getName(), app.getInstance());
        }
        return map;
    }
}
