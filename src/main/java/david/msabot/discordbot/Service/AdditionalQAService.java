package david.msabot.discordbot.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import david.msabot.discordbot.Entity.AdditionalQuizList;
import david.msabot.discordbot.Entity.Quiz;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
public class AdditionalQAService {

    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private static AdditionalQuizList quizList;
    private static ArrayList<Quiz> list = null;

    public static ArrayList<Quiz> getQuizList(){
        return list;
    }

    /* load yaml context */
    public static boolean loadFile(){
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try{
            quizList = mapper.readValue(new File("./src/main/resources/static/QuizList.yaml"), AdditionalQuizList.class);

            list = quizList.getList();
//            System.out.println(list);;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
