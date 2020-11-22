package david.msabot.discordbot.Service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.GregorianCalendar;

@Service
@PropertySource("classpath:application.properties")
public class MSAService {
    // TODO: implement all MSABot functions

//    @Value("${env.setting.rasa.url}")
    private String zuul = "http://140.121.197.130:9039";

    // Eureka service
    public void serviceEnvironment(){
        // implement action_service_env
    }

    // Zuul and swagger service
    public void apiList(){
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
    public void healthData(){
        // implement action_service_health
        // need to parse xml
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_XML_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("url", HttpMethod.GET, entity, String.class);
        String response = responseEntity.toString();
        Gson gson = new Gson();
        System.out.println(response);
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
    public void checkIntent(){
        // implement stage_check_intent
    }
}
