package david.msabot.discordbot.Service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import david.msabot.discordbot.Entity.Eureka.Application;
import david.msabot.discordbot.Entity.Eureka.EurekaResponse;
import david.msabot.discordbot.Entity.Eureka.Instance;
import david.msabot.discordbot.Entity.Rasa.IntentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:application.properties")
public class MSAService {
    // TODO: implement all MSABot functions

    @Autowired
    public Environment environment;

//    @Value("${env.setting.rasa.url}")
    private static String zuul = "http://140.121.197.130:9039";

    // Eureka service
    public void serviceEnvironment(){
        // implement action_service_env
    }

    // Zuul and swagger service
    public static void apiList(){
        // implement action_service_api_list
        String url = zuul + "/";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        System.out.println(restTemplate.exchange(url, HttpMethod.GET, entity, String.class));
    }

    // Zuul, swagger and Actuator service
    public void usageInfo(){
        // implement action_service_using_info
    }

    // Zuul and Actuator service
    public void serviceInfo(){
        // implement action_service_info
    }

    // Eureka service
    public static String healthData(){
        // implement action_service_health
        // need to parse xml
        String url = "http://140.121.197.130:9040/eureka/apps";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_XML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String xml = response.getBody();
        if(xml == null || response.getStatusCodeValue() != 200){
            // request error
            return "error occurred at api request";
        }
        StringBuilder builder = new StringBuilder();
        try{
            XmlMapper mapper = new XmlMapper();
            EurekaResponse result = mapper.readValue(replaceReversed(xml), EurekaResponse.class);
            HashMap<String, Instance> map = mapSerialize(result);

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
        }catch (Exception e){
            e.printStackTrace();
        }
        return builder.toString();
    }

    // Eureka service
    public void connectError(){
        // implement action_connect_error
    }

    // Jenkins service
    public void buildFail(){
        // implement action_build_fail
    }

    // bot using guide
    public String botHelp(){
        // implement action_bot_help
        StringBuilder builder = new StringBuilder();
        // build help message and return
        return builder.toString();
    }

    // action when new bot join
    public void botJoin(){
        // implement action_bot_join
    }

    // ask user for service
    public void noService(){
        // implement stage_no_service
    }

    // check if result has service name
    public static String checkIntent(IntentSet set){
        // implement stage_check_intent
        String intent = set.getIntent();
        String service = set.getService();

        /* intents don't need service name */
        switch (intent){
            case "action_service_env":
                return null;
            case "action_service_health":
                return healthData();
            default:
                break;
        }

        return null;
    }

    public static String replaceReversed(String source){
        return source.replace("class=", "source=");
    }

    public static HashMap<String, Instance> mapSerialize(EurekaResponse raw){
        ArrayList<Application> list = raw.getAppList();
        HashMap<String, Instance> map = new HashMap<>();
        list.forEach(app -> {
            map.put(app.getName(), app.getInstance());
        });
        return map;
    }
}
