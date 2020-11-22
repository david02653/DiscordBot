package david.msabot.discordbot.Controller.Event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import david.msabot.discordbot.Entity.AdditionalQuizList;
import david.msabot.discordbot.Entity.Quiz;
import david.msabot.discordbot.Service.AdditionalQAService;
import david.msabot.discordbot.Service.MSAService;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadyListener implements EventListener {

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
