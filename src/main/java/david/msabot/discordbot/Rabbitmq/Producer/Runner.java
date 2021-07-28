package david.msabot.discordbot.Rabbitmq.Producer;

import david.msabot.discordbot.Rabbitmq.Consumer.MessageHandler;
import david.msabot.discordbot.Rabbitmq.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Unused, ignore this class
 *
 * define rabbitMQ producer
 */
@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate template;
    private final MessageHandler handler;

    public Runner(RabbitTemplate templatem, MessageHandler handler){
        this.template = templatem;
        this.handler = handler;
    }

    @Override
    public void run(String... args) throws Exception {
        //System.out.println("Sending msg--");
        //template.convertAndSend(RabbitConfig.EXCHANGE_NAME, "cat.tail", "are you okay ?");
    }
}
