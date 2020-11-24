package david.msabot.discordbot.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import david.msabot.discordbot.Entity.Rasa.Intent;
import david.msabot.discordbot.Entity.Rasa.IntentSet;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

public class RasaService {

//    @Value("${env.setting.rasa.url}")
//    String RASA_ENDPOINT;
    private static int stage;

    public static IntentSet analyzeIntent(String data){
        MSAService msaService = new MSAService();

        String RASA_ENDPOINT = "http://140.121.197.130:9004";

        // implement function stage_rasa(...)
        // POST method to rasa endpoint, return with json type data {intent, data}

        RestTemplate template = new RestTemplate();
        String path = RASA_ENDPOINT + "/webhooks/rest/webhook";
//        System.out.println(path);

        // set request content
        JsonObject content = new JsonObject();
        content.addProperty("message", data);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<String>(content.toString(), headers);
        ResponseEntity<String> response = template.exchange(path, HttpMethod.POST, entity, String.class);

        System.out.println(response);
//        System.out.println("response body : " + response.getBody());
        /* jsonObject version string pre-handle */
//        String raw = changeFormat(response.getBody());
//        System.out.println("target json : " + raw);

        /* gson string pre-handle */
        String raw = noSlash(response.getBody());
        System.out.println("raw : " + raw);

        try{
            /* Gson version, no backslash and no '"' surround object required */
            Gson gson = new Gson();
            Intent analyseResult = gson.fromJson(raw, Intent.class);
            System.out.println("gson analyzed: " + analyseResult);
            /* available version, backslash and no '"' surround object required */
//            JSONObject object = new JSONObject(raw);
//            String inner = object.getString("text");
//            System.out.println(inner);
//            JSONObject innerObject = new JSONObject(inner);
//            System.out.println(innerObject);
//            if(innerObject.has("service")) System.out.println(innerObject.getString("service"));
//            else System.out.println("service not exist");
//            System.out.println(innerObject.getString("intent"));

            String intent = analyseResult.getText().getIntent();
            String service = analyseResult.getText().getService();

//            if(service != null && !service.equals("none")){
//                /* handle service */
//                System.out.println(service);
//            }else{
//                /* no service */
//                if(intent.equals("action_service_health"))
//                    return msaService.healthData();
//            }
            return analyseResult.getText();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String changeFormat(String raw){
        String[] token = raw.split(",");
        String data = "{";
        for(int i=1; i<token.length; i++) {
            data += token[i];
            if(i != token.length-1) data += ",";
        }
        data = data.replace("'", "\"");
        StringBuilder builder = new StringBuilder(data);
        builder.deleteCharAt(builder.length()-1);
//        builder.deleteCharAt(0);

        String temp = builder.toString();
        String result = "{";
        String[] second = temp.split("");
        for(int i=1; i<second.length; i++){
//            System.out.println("current=[" + second[i] + "], i=" + i + ", result=[" + result + "]");
            int open = StringUtils.countOccurrencesOf(temp.substring(0, i), "{");
            int close = StringUtils.countOccurrencesOf(temp.substring(0, i), "}");
            if(second[i].equals("\"")){
                if(open - close == 1){
                    if(second[i+1].equals("{") || second[i-1].equals("}")) {
                        result += "";
                    }
                    else{
                        result += "\\\"";
                    }
//                    result += second[i];
                    continue;
                }else{
                    if(second[i-1].equals("\\")) {
                        result += second[i];
                    }else{
                        result += "\\\"";
                    }
                    continue;
                }
            }
            result += second[i];
        }

        return result;
    }

    public static String noSlash(String raw){
        String[] token = raw.split("");
        StringBuilder result = new StringBuilder();
        for(String t: token){
            if(t.equals("\\")) continue;
            if(t.equals("'")){
                result.append("\"");
                continue;
            }
            result.append(t);
        }
        result.deleteCharAt(result.length()-1);
        result.deleteCharAt(0);

        String temp = result.toString();
        StringBuilder output = new StringBuilder("{");
        String[] second = temp.split("");
        for(int i=1; i<second.length; i++){
//            System.out.println("current=[" + second[i] + "], i=" + i + ", result=[" + result + "]");
            int open = StringUtils.countOccurrencesOf(temp.substring(0, i), "{");
            int close = StringUtils.countOccurrencesOf(temp.substring(0, i), "}");
            if(second[i].equals("\"")){
                if(open - close == 1){
                    if(second[i+1].equals("{") || second[i-1].equals("}")) {
                        continue;
                    }
                }
            }
            output.append(second[i]);
        }
        return output.toString();
    }


}
