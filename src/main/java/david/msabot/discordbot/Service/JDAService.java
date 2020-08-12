package david.msabot.discordbot.Service;

import david.msabot.discordbot.Controller.Event.MessageEvent;
import david.msabot.discordbot.Controller.Event.ReadyListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class JDAService {

    public static void createJDA(String token) throws Exception {
        JDABuilder builder = JDABuilder.createDefault(token);

        configure(builder);
        // add customized ReadyListener
        builder.addEventListeners(new ReadyListener());
        // add customized MessageListener
        builder.addEventListeners(new MessageEvent());
        builder.build();
    }

    public static void configure(JDABuilder builder){
        // disable member activities (streaming / games / spotify)
        builder.disableCache(CacheFlag.ACTIVITY);
        // disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);
    }
}
