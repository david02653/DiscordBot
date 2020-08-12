package david.msabot.discordbot.Service;

import david.msabot.discordbot.Entity.BotAuthParam;
import org.springframework.stereotype.Service;

@Service
public class BotService {

    final String discord_token_url = "https://discord.com/api/oauth2/token";
    final String CLIENT_SECRET = "jhsau_0MwJllwK0ggME8RUWdg70SbEE8";
    final String CLIENT_ID = "737214248962490418";
    private BotAuthParam param = null;

    public void saveParam(BotAuthParam param){
        this.param = param;
    }

    public String getCode(){
        return param.getCode();
    }

//    public String createPayload(String code){
//        String temp = "client_id=" + CLIENT_ID + "&";
//        temp += "client_secret=" + CLIENT_SECRET + "&";
//        temp += "grant_scope=authorization_code&";
//        temp += "code=" + code + "&";
//        temp += "scope=identify%20bot";
//        return temp;
//    }



}
