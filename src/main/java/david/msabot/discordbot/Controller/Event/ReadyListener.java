package david.msabot.discordbot.Controller.Event;

import david.msabot.discordbot.Service.AdditionalQAService;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class ReadyListener implements EventListener {

    @Autowired
    public AdditionalQAService service;

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof ReadyEvent){
            System.out.println("> JDA API ready");

            if(AdditionalQAService.loadFile()){
                System.out.println(">> yaml file load success !");
                System.out.println(AdditionalQAService.getMap());
            }else{
                System.out.println(">> yaml file load error");
            }
        }
    }
}
