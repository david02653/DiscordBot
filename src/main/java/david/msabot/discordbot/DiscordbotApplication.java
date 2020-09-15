package david.msabot.discordbot;

import com.google.gson.JsonObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DiscordbotApplication {

    private static RestTemplate template = new RestTemplate();
    private static String discordPath = "http://localhost:8080/Disbot";
    private static String rabbitPath = "http://localhost:8080/rabbit";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DiscordbotApplication.class, args);
        //JDAService.createJDA("NzM3MjE0MjQ4OTYyNDkwNDE4.Xx6GRQ.9vXWpSN5LgxnqmDG1Ia1fCfwk4k");

        // set request content
        JsonObject content = new JsonObject();
        content.addProperty("msg", "test comment");
//        System.out.println(content.toString());

        String testPost = rabbitPath + "/post";
        HttpHeaders headers = new HttpHeaders();  // http headers setting
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestContent = new HttpEntity<String>("{\"message\":\"kkkkkkkkkkk\"}", headers);
        ResponseEntity<String> response = template.exchange(testPost, HttpMethod.POST, requestContent, String.class);
        System.out.println(response);
        System.out.println(response.getBody());


    }

}
