package david.msabot.discordbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscordBotApplication {

//    private static RestTemplate template = new RestTemplate();
//    private static String discordPath = "http://localhost:8080/Disbot";
//    private static String rabbitPath = "http://localhost:8080/rabbit";

    public static void main(String[] args) {
        SpringApplication.run(DiscordBotApplication.class, args);

        // set request content
//        JsonObject content = new JsonObject();
//        content.addProperty("msg", "test comment");
//        System.out.println(content.toString());

//        String testPost = rabbitPath + "/post";
//        HttpHeaders headers = new HttpHeaders();  // http headers setting
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestContent = new HttpEntity<String>("{\"message\":\"kkkkkkkkkkk\"}", headers);
//        ResponseEntity<String> response = template.exchange(testPost, HttpMethod.POST, requestContent, String.class);
//        System.out.println(response);
//        System.out.println(response.getBody());


    }

}
