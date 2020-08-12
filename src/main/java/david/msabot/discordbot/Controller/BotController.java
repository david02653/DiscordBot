package david.msabot.discordbot.Controller;

import david.msabot.discordbot.Entity.BotAuthParam;
import david.msabot.discordbot.Service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/DisBot", produces = MediaType.APPLICATION_JSON_VALUE)
public class BotController {

    @Autowired
    private BotService service = new BotService();

    @GetMapping(value = "/register")
    public ResponseEntity<String> getTest(){
        // test if get method is working
        return ResponseEntity.ok("success");
    }

    @GetMapping(value = "/token")
    public ResponseEntity<BotAuthParam> getCode(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "guild_id", required = false) String guildId, @RequestParam(value = "permissions", required = false) Integer permissions){
        BotAuthParam param = new BotAuthParam(code, guildId, permissions);
        System.out.println(param);
        service.saveParam(param);

        String url = "https://discord.com/api/oauth2/token";
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("client_id", "737214248962490418");
        payload.add("client_secret", "jhsau_0MwJllwK0ggME8RUWdg70SbEE8");
        payload.add("grant_type", "authorization_code");
        payload.add("code", code);
        payload.add("redirect_uri", "http://localhost:8080/DisBot/token");
        payload.add("scope", "identify%20bot");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(payload, headers);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.println(response);
        System.out.println(response.getBody());


        return ResponseEntity.ok(param);
    }

//    @PostMapping(value = "/exchange")
//    public ResponseEntity<Object> tokenExchange(){
//        return null;
//    }

    @GetMapping(value = "/code")
    public ResponseEntity<String> getCode(){
        return ResponseEntity.ok().body(service.getCode());
    }
}
