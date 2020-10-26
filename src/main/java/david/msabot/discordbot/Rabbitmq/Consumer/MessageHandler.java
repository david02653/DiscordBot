package david.msabot.discordbot.Rabbitmq.Consumer;

import david.msabot.discordbot.Service.JDAService;
import org.json.JSONObject;
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

    public void handleEurekaMessage(byte[] message){
        String msg = new String(message);
        JSONObject obj  = new JSONObject(msg);
        System.out.println(obj.toString());

//        System.out.println(obj.get("status"));
        String status = (String) obj.get("status");
        String result = "";
        switch(status){
            case "Failed":
                result = "[" + status + "] Service : " + obj.get("appName") + " is dead. Please check it.";
                break;
            case "Server Start":
                result = "[" + status + "] Eureka Server just start working !";
                break;
            case "Server Registry Start":
                result = "[" + status + "] Eureka Registry Server just start working !";
                break;
        }

        JDAService.send("test_channel", result);
    }
}
