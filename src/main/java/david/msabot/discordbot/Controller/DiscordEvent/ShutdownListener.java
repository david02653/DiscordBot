package david.msabot.discordbot.Controller.DiscordEvent;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ShutdownListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if(genericEvent instanceof ShutdownEvent){
            System.out.println("[JDA][ShutdownEvent] disconnected from discord !");
        }
    }
}
