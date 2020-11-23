package david.msabot.discordbot.Controller.Event;

import david.msabot.discordbot.Entity.AQA.Quiz;
import david.msabot.discordbot.Service.AdditionalQAService;
import david.msabot.discordbot.Service.RasaService;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
                    String aqaMsg = parse(msgReceived);
                    String channel = event.getTextChannel().getName().toLowerCase();
                    HashMap<String, HashMap<String, Quiz>> map = AdditionalQAService.getMap();
//                    System.out.println(AdditionalQAService.getAdditionalQuizList());
                    /* check additional QA */
//                    System.out.println(map);
                    if(map.get(channel) != null){
                        Quiz quiz = map.get(channel).get(aqaMsg.toLowerCase());
                        System.out.println(quiz);
                        if(quiz != null){
                            if(quiz.getResource().toLowerCase().equals("rest")){
                                // request for answer
                                event.getTextChannel().sendMessage(restRequest(quiz.getSource(), quiz.getMethod())).queue();
                            }else if(quiz.getResource().toLowerCase().equals("rasa")){
                                // send to rasa
                                event.getTextChannel().sendMessage(RasaService.analyzeIntent(quiz.getQuestion())).queue();
                            }else{
                                // return answer from file
                                event.getTextChannel().sendMessage(quiz.getAnswer()).queue();
                            }
                        }
                    }else{
                        event.getTextChannel().sendMessage("yaml file mapping error").queue();
                    }
                }else{
                    /* intent analyze */
                    RasaService.analyzeIntent(msgReceived);
                }


            }
        }
    }

    private static String parse(String input){
        String raw = input.substring(1); // remove command head

//        String[] token = raw.split(" ");
//        StringBuilder builder = new StringBuilder();
//        Iterator<String> stringIterator = Arrays.stream(token).iterator();
//        while(stringIterator.hasNext()){
//            builder.append(stringIterator.next());
//            if(stringIterator.hasNext())
//                builder.append(" ");
//        }
        return raw.trim().toLowerCase();
    }

    public static String restRequest(String source, String method){
        String raw = method.toLowerCase();
        String type = raw.split(":")[0];
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", methodType(method));
        System.out.println(methodType(method));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        switch(type){
            case "get":
                // request get method
                return template.exchange(source, HttpMethod.GET, entity, String.class).getBody();
            case "post":
                // request post method
                break;
            default:
                return "";
        }
//        final String url = "https://contractbody-fakeapi.herokuapp.com/";
//        RestTemplate restTemplate = new RestTemplate();
//        Gson gson = new Gson();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//        System.out.println(restTemplate.exchange(url, HttpMethod.GET, entity, Contract.class));
//        System.out.println(restTemplate.exchange(url, HttpMethod.GET, entity, String.class));
        return null;
    }

    public static String methodType(String method){
        String[] token = method.split(":");
        switch(token[token.length - 1]){
            case "xml":
                return MediaType.APPLICATION_XML_VALUE;
            case "json":
                return MediaType.APPLICATION_JSON_VALUE;
        }
        return MediaType.ALL_VALUE;
    }
}
