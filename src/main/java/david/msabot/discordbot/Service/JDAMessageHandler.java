package david.msabot.discordbot.Service;

import david.msabot.discordbot.Entity.AQA.Quiz;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * this class handle jda message services
 * sending message, sending embed message, etc.
 * local jda and channel information using event (pass by parameter)
 */
@Service
public class JDAMessageHandler {

    /**
     * Additional question service is used to call api for long messages (length > 2000)
     */
    private final AdditionalQAService additionalQAService;

    @Autowired
    public JDAMessageHandler(AdditionalQAService aqa){
        this.additionalQAService = aqa;
    }


    /**
     * find correspond answer in additional setting file and send back
     * if message length > 2000, call api to handle
     * @param channel target channel, default: where message received
     * @param quiz quiz list of target channel
     * @param msg incoming message
     */
    public void sendMessage(TextChannel channel, Quiz quiz, String msg){
        if(msg.length() > 2000)
            channel.sendMessage(additionalQAService.insertMessage(AdditionalQAService.restRequest(quiz.getSource(), quiz.getMethod()))).queue();
        else
            channel.sendMessage(msg).queue();
    }

    /**
     * check message length and send back
     * @param channel target channel, default: where message received
     * @param msg message to send
     */
    public void sendMessage(TextChannel channel, String msg){
        if(msg.length() > 2000)
            channel.sendMessage(additionalQAService.insertMessage(msg)).queue();
        else
            channel.sendMessage(msg).queue();
    }
    /* in case needs to send message to other channel */
    public void sendMessage(MessageReceivedEvent event, String id, String msg){}
    public void sendMessage(MessageReceivedEvent event, long id, String msg){}
    public void sendMessage(MessageReceivedEvent event, String msg){}

    public void sendEmbedMessage(String title, ArrayList<String> content){
        // todo: send embed message
    }

    /**
     * send embedMessage to target text channel
     * @param channel target channel
     * @param embedMsg embed message
     */
    public void sendEmbedMessage(TextChannel channel, MessageEmbed embedMsg){
        channel.sendMessage(embedMsg).queue();
    }
}
