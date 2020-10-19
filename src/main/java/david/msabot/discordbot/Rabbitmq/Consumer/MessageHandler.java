package david.msabot.discordbot.Rabbitmq.Consumer;

import david.msabot.discordbot.Service.JDAService;
import org.springframework.stereotype.Service;

@Service
public class MessageHandler {

    public void handleMessage(String msg){
        System.out.println(msg);
    }

    public void handleJenkinsMessage(byte[] message){
        String msg = new String(message);
        msg = "[jenkins] " + msg;
        System.out.println(msg);

        // <-- maybe add some filter here
        // send message to discord
        JDAService.send("test_channel", msg);
    }
}
