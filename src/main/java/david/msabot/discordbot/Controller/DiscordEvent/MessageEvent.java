package david.msabot.discordbot.Controller.DiscordEvent;

import david.msabot.discordbot.Entity.AQA.Quiz;
import david.msabot.discordbot.Entity.Rasa.IntentSet;
import david.msabot.discordbot.Service.*;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * this class will listen to Discord onMessage received event
 */
@Service
public class MessageEvent extends ListenerAdapter {

    /* required spring handled service */
    private final AdditionalQAService aqaService;
    private final RasaService rasa;
    private final IntentHandleService intentHandler;
    private final JDAMessageHandler jdaMsg;

    private HashMap<String, HashMap<String, Quiz>> additionQAList;

    /**
     * constructor for spring, instance are injected by spring
     * @param additionalQAService instance of AdditionalQAService class
     * @param rasa instance of RasaService class
     * @param intentHandle inject by spring, handle intent analyze and define what to do
     * @param jdaMsg inject by spring, handle jda message-related service
     */
    @Autowired
    public MessageEvent(AdditionalQAService additionalQAService, RasaService rasa, IntentHandleService intentHandle, JDAMessageHandler jdaMsg){
        this.aqaService = additionalQAService;
        this.rasa = rasa;
        this.intentHandler = intentHandle;
        this.jdaMsg = jdaMsg;

        // load additional question list
        this.additionQAList = additionalQAService.getMap();
    }

    /**
     * listen MessageReceivedEvent
     * handle what to do with incoming message here
     * notice that you can only send message with size smaller than 2000
     * use api to store massive response message, send api url instead of raw message
     *
     * note:
     * if message start with no decorator: normal message
     *   -> send to rasa intent analyze -> check what to do
     * if message start with decorator '!': additional question list from yaml setting file
     *   -> check additional question list
     *     -> response with assigned method (result in file, result from api, etc), check the question file for details
     * @param event message received event
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        if(event.isFromType(ChannelType.PRIVATE)){
            System.out.printf("[private message] %s: %s\n", event.getAuthor().getName(), event.getMessage().getContentDisplay());
        }else{
            // print [Service_name][Current_TextChannel] author_name: message content
//            System.out.println("[DEBUG][Discord onMessage]: " + event.getMessage().getType());
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(), event.getTextChannel().getName(), Objects.requireNonNull(event.getMember()).getEffectiveName(), event.getMessage().getContentDisplay());

            /* respond when speak is not a bot */
            if(!event.getAuthor().isBot()){
//                RestTemplate template = new RestTemplate();
//                String testPost = "http://localhost:8080/rabbit/send";
//                HttpHeaders headers = new HttpHeaders();  // http headers setting
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                HttpEntity<String> requestContent = new HttpEntity<String>("{\"message\":\"" + event.getMessage().getContentDisplay() + "\"}", headers);
//                ResponseEntity<String> response = template.exchange(testPost, HttpMethod.POST, requestContent, String.class);
//                System.out.println(response);
//                System.out.println(response.getBody());
                /* message received from discord */
                String msgReceived = event.getMessage().getContentDisplay();
                /* check if message starts with any decorator */
                if(msgReceived.startsWith("!")){
                    // additional qa
                    try {
                        handleAdditionalQARequest(event, msgReceived);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                }else if(msgReceived.startsWith("^")){
//                    // rasa tunnel for additional qa
//
                }else{
                    /* intent analyze */
                    try {
                        // default message handle: send message to rasa to check intent
                        // send analyzedResult back
                        String analyzeResult = defaultMessageHandle(msgReceived);
//                         /* print detected intent for now */
//                        event.getTextChannel().sendMessage(rasa.analyzeIntent(msgReceived).toString()).queue();
                        jdaMsg.sendMessage(event.getTextChannel(), analyzeResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * default message input, send to Rasa to analyze intent
     * do stuffs with detected intent and send back to discord
     *
     * normal message handle (rasa analyze)
     * @param input user input
     * @return handle result
     */
    private String defaultMessageHandle(String input) throws Exception{
        IntentSet detectedIntent = rasa.analyzeIntent(input);
        // send intent to intentHandler to check next move
        // send message back to frontend
        String correspondMsg = intentHandler.checkIntent(detectedIntent);
        return correspondMsg;
    }

    /**
     * handle requests for additional question
     * @param event incoming onMessage event
     * @param input raw message of user input
     */
    private void handleAdditionalQARequest(@NotNull MessageReceivedEvent event, String input) throws Exception {
        String msg = parse(input);
        String currentChannelName = event.getTextChannel().getName().toLowerCase();
        TextChannel currentChannel = event.getTextChannel();
        if(additionQAList.get(currentChannelName) != null){
            String result = "";
            /* do stuffs if current channel have questions in list */
            Quiz quiz = additionQAList.get(currentChannelName).get(msg.toLowerCase());
            if(quiz != null){
                String quizResource = quiz.getResource().toLowerCase();
                switch (quizResource){
                    case "rest":
                        /* handle addition question with resource from rest */
                        result = AdditionalQAService.restRequest(quiz.getSource(), quiz.getMethod());
                        break;
                    case "rasa":
                        /* handle addition question with resource from rasa*/
                        result = intentHandler.checkIntent(rasa.analyzeIntent(quiz.getQuestion()));
                        break;
                    default:
                        /* extract answer from setting file */
                        result = quiz.getAnswer();
                        break;
                }
                if(result != null)
                    jdaMsg.sendMessage(currentChannel, quiz, result);
                else
                    System.out.println("[DEBUG][addition_QA] null result !");
            }else
                event.getTextChannel().sendMessage("[Warning] No Additional question found or question list is not available.").queue();
        }else
            event.getTextChannel().sendMessage("[DEBUG] something goes wrong with setting file.").queue();
    }

    /**
     * remove white space from beginning and convert to lower case
     * @param input input string
     * @return result
     */
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
}
