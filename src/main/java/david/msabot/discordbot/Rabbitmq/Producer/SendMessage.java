package david.msabot.discordbot.Rabbitmq.Producer;

import david.msabot.discordbot.Rabbitmq.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * send message to rabbitMQ server
 * this controller is currently UNUSED
 * just keep it in case i need a rabbitmq producer
 */
@RestController
@RequestMapping(value = "/rabbit")
public class SendMessage {

    private final RabbitTemplate template;
    public SendMessage(RabbitTemplate template){
        this.template = template;
    }

    @PostMapping(value = "/send")
    public ResponseEntity<String> send(@RequestBody String message){
        System.out.println("post message :" + message);
//        template.convertAndSend(RabbitConfig.EXCHANGE_NAME, "cat.#", message);
        template.convertAndSend(RabbitConfig.TEST_EXCHANGE, "discord.block", message);
        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/post")
    public ResponseEntity<String> postTest(@RequestBody String msg){
        System.out.println("post method triggered !");
        return ResponseEntity.ok("your post msg => " + msg);
    }
}
