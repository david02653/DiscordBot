package david.msabot.discordbot.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import david.msabot.discordbot.Entity.Intent;
import david.msabot.discordbot.Entity.RawIntent;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

public class RasaService {

//    @Value("${env.setting.rasa.url}")
//    String RASA_ENDPOINT;

    public static String analyzeIntent(String data){

        String RASA_ENDPOINT = "http://140.121.197.130:9004";

        // implement function stage_rasa(...)
        // POST method to rasa endpoint, return with json type data {intent, data}

        RestTemplate template = new RestTemplate();
        String path = RASA_ENDPOINT + "/webhooks/rest/webhook";
//        System.out.println(path);

        // set request content
//        JsonObject content = new JsonObject();
//        content.addProperty("msg", "test comment");
//
//        String testPost = rabbitPath + "/post";
//        HttpHeaders headers = new HttpHeaders();  // http headers setting
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestContent = new HttpEntity<String>("{\"message\":\"kkkkkkkkkkk\"}", headers);
//        ResponseEntity<String> response = template.exchange(testPost, HttpMethod.POST, requestContent, String.class);
//        System.out.println(response);
//        System.out.println(response.getBody());
        JsonObject content = new JsonObject();
        content.addProperty("message", data);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestContent = new HttpEntity<String>(content.toString(), headers);
        ResponseEntity<String> response = template.exchange(path, HttpMethod.POST, requestContent, String.class);
//        System.out.println(response.getBody());


        String raw = changeFormat(response.getBody());
        System.out.println(raw);
//        raw = raw.replace("\"", "\\\"");
//        raw = raw.replace("\\\\\"", "\\\"");
//        System.out.println(raw);
//        JSONObject object = new JSONObject(changeFormat(raw));
//        System.out.println(object);

        try{
//            JSONObject object = new JSONObject(raw);
//            System.out.println(object);
            ObjectMapper mapper = new ObjectMapper();
//            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Intent rawIntent = mapper.readValue(raw, Intent.class);
//            System.out.println(rawIntent.recipient_id);
//            System.out.println(rawIntent.text);
//            Intent intent = mapper.readValue(object.getString("text"), Intent.class);
//            System.out.println(intent.intent);
//            System.out.println(intent.service);
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    public static String changeFormat(String raw){
        String[] token = raw.split(",");
        String data = "{";
        for(int i=1; i<token.length; i++)
            data += token[i];
        data = data.replace("'", "\"");
        StringBuilder builder = new StringBuilder(data);
        builder.deleteCharAt(builder.length()-1);
//        builder.deleteCharAt(0);
        return builder.toString();
    }
}
