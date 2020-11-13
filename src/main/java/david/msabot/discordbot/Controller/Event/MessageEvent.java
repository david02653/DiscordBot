package david.msabot.discordbot.Controller.Event;

import david.msabot.discordbot.Service.RasaService;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class MessageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        if(event.isFromType(ChannelType.PRIVATE)){
            System.out.printf("[private message] %s: %s\n", event.getAuthor().getName(), event.getMessage().getContentDisplay());
        }else{
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(), event.getTextChannel().getName(), event.getMember().getEffectiveName(), event.getMessage().getContentDisplay());

            /* reply with 'I hear you' when any user input detected */
//            if(event.getTextChannel().getName().contains("channel") && event.getMember().getEffectiveName().contains("david")){
//                event.getTextChannel().sendMessage("I hear you.").queue();
//            }

            if(!event.getAuthor().isBot()){
//                RestTemplate template = new RestTemplate();
//                String testPost = "http://localhost:8080/rabbit/send";
//                HttpHeaders headers = new HttpHeaders();  // http headers setting
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                HttpEntity<String> requestContent = new HttpEntity<String>("{\"message\":\"" + event.getMessage().getContentDisplay() + "\"}", headers);
//                ResponseEntity<String> response = template.exchange(testPost, HttpMethod.POST, requestContent, String.class);
//                System.out.println(response);
//                System.out.println(response.getBody());

                String msgReceived = event.getMessage().getContentDisplay();
                RasaService.analyzeIntent(msgReceived);
            }
        }
    }
}
