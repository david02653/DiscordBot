package david.msabot.discordbot.Controller.Event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import david.msabot.discordbot.Entity.AdditionalQuizList;
import david.msabot.discordbot.Entity.Quiz;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ReadyListener implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof ReadyEvent){
            System.out.println("> JDA API ready");

//            /* load yaml context */
//            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//            mapper.findAndRegisterModules();
//            AdditionalQuizList quizList;
//            try{
//                quizList = mapper.readValue(new File("./src/main/resources/static/QuizList.yaml"), AdditionalQuizList.class);
//
//                ArrayList<Quiz> list = quizList.getList();
//                System.out.println(list);;
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
    }
}
