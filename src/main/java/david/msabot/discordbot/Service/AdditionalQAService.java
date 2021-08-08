package david.msabot.discordbot.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import david.msabot.discordbot.Entity.AdditionalQuestion.AdditionalQuizList;
import david.msabot.discordbot.Entity.AdditionalQuestion.QuestionList;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class AdditionalQAService {

//    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//    private static AdditionalQuizList quizList;
//    private static ArrayList<AdditionalQuizList> lists = null;
    private final String additionalSettingPath;
    private final String longMsgHandleApi;
    private static List<AdditionalQuizList> lists;

    /**
     * read settings from application.properties
     * @param env application context, injected by spring
     */
    @Autowired
    public AdditionalQAService(Environment env){
        additionalSettingPath = env.getProperty("additional.setting.path");
        longMsgHandleApi = env.getProperty("long.message.handle.url");
        if(loadFile())
            System.out.println("[AdditionalQA][init] setting file load successfully.");
        else
            System.out.println("[DEBUG][AdditionalQA][init] failed to load file.");
    }

    public List<AdditionalQuizList> getAdditionalQuizList(){
        return lists;
    }

    /**
     * get current question list in HashMap
     * @return current question list
     */
    public HashMap<String, HashMap<String, QuestionList>> getMap(){
        return parse();
    }

    /**
     * read additional question list from yaml file
     * set file path in application.properties
     * @return if yaml file is loaded correctly
     */
    public boolean loadFile(){
//        mapper = new ObjectMapper(new YAMLFactory());
//        mapper.findAndRegisterModules();
        /* parse multiple yaml documents from single file */
        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper();
        try {
//            YAMLParser yamlParser = yamlFactory.createParser(new File("./src/main/resources/static/QuizList.yaml"));
            YAMLParser yamlParser = yamlFactory.createParser(new File(additionalSettingPath));
            lists = mapper.readValues(yamlParser, AdditionalQuizList.class).readAll();
            //System.out.println(lists);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * parse loaded question list in to HashMap
     * @return loaded question list
     */
    public HashMap<String, HashMap<String, QuestionList>> parse(){
        HashMap<String, HashMap<String, QuestionList>> target = new HashMap<>();
        lists.forEach(channel -> {
            HashMap<String, QuestionList> map = new HashMap<>();
            channel.getList().forEach(questionList -> {
                map.put(questionList.getQuestion().toLowerCase(), questionList);
            });
            target.put(channel.getChannel().toLowerCase(), map);
        });
        return target;
    }

    /**
     * SUSPEND, use long message service instead
     * handle message over 2000 characters
     * send message to other service and save in mongodb
     * return link of message
     * @param msg long message
     * @return url of message
     */
    public String insertMessage(String msg){
        JSONObject json = new JSONObject();
        json.put("message", msg);
//        String url = "http://140.121.197.130:9008/message/add";
        String url = longMsgHandleApi + "/add";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(json.toString(), headers);

        String objectId = template.postForEntity(url, entity, String.class).getBody();
//        return template.postForEntity(url, entity, String.class).getBody();
        return longMsgHandleApi + "/" + objectId;
    }

    /**
     * fire request via restTemplate
     * @param source api url
     * @param method get or post
     * @return request result
     */
    public static String restRequest(String source, String method){
        String raw = method.toLowerCase();
        String type = raw.split(":")[0];
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", methodType(method));
        System.out.println(methodType(method));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        switch(type){
            case "get":
                // request get method
                return template.exchange(source, HttpMethod.GET, entity, String.class).getBody();
            case "post":
                // request post method
                break;
            default:
                return "";
        }
        return null;
    }

    /**
     * return string of MediaType for each method
     * @param method method type, xml or json
     * @return string of MediaType
     */
    private static String methodType(String method){
        String[] token = method.split(":");
        switch(token[token.length - 1]){
            case "xml":
                return MediaType.APPLICATION_XML_VALUE;
            case "json":
                return MediaType.APPLICATION_JSON_VALUE;
        }
        return MediaType.ALL_VALUE;
    }
}
