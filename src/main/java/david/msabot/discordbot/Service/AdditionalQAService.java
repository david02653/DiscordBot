package david.msabot.discordbot.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import david.msabot.discordbot.Entity.AQA.AdditionalQuizList;
import david.msabot.discordbot.Entity.AQA.DiscordMessage;
import david.msabot.discordbot.Entity.AQA.Quiz;
import david.msabot.discordbot.Repository.DiscordRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class AdditionalQAService {

//    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//    private static AdditionalQuizList quizList;
//    private static ArrayList<AdditionalQuizList> lists = null;
    private static List<AdditionalQuizList> lists;

    public List<AdditionalQuizList> getAdditionalQuizList(){
        return lists;
    }

    public static HashMap<String, HashMap<String, Quiz>> getMap(){
        return parse();
    }

    /* load yaml context */
    public static boolean loadFile(){
//        mapper = new ObjectMapper(new YAMLFactory());
//        mapper.findAndRegisterModules();
        /* parse multiple yaml documents from single file */
        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper();
        try {
            YAMLParser yamlParser = yamlFactory.createParser(new File("./src/main/resources/static/QuizList.yaml"));
            lists = mapper.readValues(yamlParser, AdditionalQuizList.class).readAll();
            //System.out.println(lists);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static HashMap<String, HashMap<String, Quiz>> parse(){
        HashMap<String, HashMap<String, Quiz>> target = new HashMap<>();
        lists.forEach(channel -> {
            HashMap<String, Quiz> map = new HashMap<>();
            channel.getList().forEach(quiz -> {
                map.put(quiz.getQuestion().toLowerCase(), quiz);
            });
            target.put(channel.getChannel().toLowerCase(), map);
        });
        return target;
    }

    public static String insertMessage(String msg){
        JSONObject json = new JSONObject();
        json.put("message", msg);
        String url = "http://140.121.197.130:9008/message/add";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(json.toString(), headers);

        String objectId = template.postForEntity(url, entity, String.class).getBody();
//        return template.postForEntity(url, entity, String.class).getBody();
        return "http://140.121.197.130:9008/message/" + objectId;
    }

//    @PostConstruct
//    public void init(){
//        try{
//            System.out.println("initialize");
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
