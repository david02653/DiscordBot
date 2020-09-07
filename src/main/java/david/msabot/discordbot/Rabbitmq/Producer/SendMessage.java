package david.msabot.discordbot.Rabbitmq.Producer;

import david.msabot.discordbot.Rabbitmq.RabbitConfig;
import okhttp3.Response;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rabbit")
public class SendMessage {

    private final RabbitTemplate template;
    public SendMessage(RabbitTemplate template){
        this.template = template;
    }

    @PostMapping(value = "/send")
    public ResponseEntity<String> send(@RequestParam String message){
        System.out.println("post message :" + message);
        template.convertAndSend(RabbitConfig.EXCHANGE_NAME, "dog.cry", message);
        return ResponseEntity.ok(message);
    }
}
