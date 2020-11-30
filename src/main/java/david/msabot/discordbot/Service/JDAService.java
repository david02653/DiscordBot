package david.msabot.discordbot.Service;

import david.msabot.discordbot.Controller.DiscordEvent.MessageEvent;
import david.msabot.discordbot.Controller.DiscordEvent.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.List;

public class JDAService {
    public static JDA jda;

    public static void createJDA(String token) throws Exception {
        JDABuilder builder = JDABuilder.createDefault(token);

        configure(builder);
        // add customized ReadyListener
        builder.addEventListeners(new ReadyListener());
        // add customized MessageListener
        builder.addEventListeners(new MessageEvent());
        jda = builder.build();
    }

    public static void configure(JDABuilder builder){
        // disable member activities (streaming / games / spotify)
        builder.disableCache(CacheFlag.ACTIVITY);
        // disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);
    }

    public static void send(String channel, String msg){
        List<TextChannel> channels = jda.getTextChannelsByName(channel, true);
        for(TextChannel ch: channels){
            ch.sendMessage(msg).queue();
        }
    }
}
