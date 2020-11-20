package david.msabot.discordbot.Controller.Event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import david.msabot.discordbot.Entity.AdditionalQuizList;
import david.msabot.discordbot.Entity.Message;
import david.msabot.discordbot.Entity.Quiz;
import david.msabot.discordbot.Service.AdditionalQAService;
import david.msabot.discordbot.Service.RasaService;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

                if(msgReceived.startsWith("!")){
                    String cmd = msgReceived.substring(1);
                    /* check additional QA */
                    System.out.println(AdditionalQAService.getAdditionalQuizList());
                    //if(AdditionalQAService.getQuizList() != null){
//                    if(lists != null) {
//                        for (Additon q : lists) {
//                            System.out.println(q);
//                            if (q.getQuestion().equals(cmd)) {
//                                event.getTextChannel().sendMessage(q.getAnswer()).queue();
//                            }
//                        }
//                    }
                }else{
                    /* intent analyze */
                    RasaService.analyzeIntent(msgReceived);
                }


            }
        }
    }
}
