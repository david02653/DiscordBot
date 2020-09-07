package david.msabot.discordbot.Rabbitmq.Consumer;

import org.springframework.stereotype.Service;

@Service
public class MessageHandler {

    public void handleMessage(String msg){
        System.out.println(msg);
    }
}
