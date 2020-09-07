package david.msabot.discordbot;

import david.msabot.discordbot.Rabbitmq.Producer.SendMessage;
import david.msabot.discordbot.Rabbitmq.Producer.SendTest;
import david.msabot.discordbot.Rabbitmq.RabbitConfig;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DiscordbotApplication {

    public static void main(String[] args) throws Exception {
        //JDAService.createJDA("NzM3MjE0MjQ4OTYyNDkwNDE4.Xx6GRQ.9vXWpSN5LgxnqmDG1Ia1fCfwk4k");

//        SendTest test = new SendTest();
//        test.send();

//        RestTemplate template = new RestTemplate();
//        String url = "http://localhost:8080/rabbit/send";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
//        param.add("message", "jumping cat");
//        System.setProperty("proxyHost", "localhost");
//        System.setProperty("proxyPort", "8080");
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(param, headers);
//        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        //ResponseEntity<String> response = template.postForEntity(url, requestEntity, String.class);
//
//        System.out.println(response.getBody());

        SpringApplication.run(DiscordbotApplication.class, args);
    }

}
