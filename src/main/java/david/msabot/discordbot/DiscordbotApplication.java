package david.msabot.discordbot;

import david.msabot.discordbot.Service.JDAService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiscordbotApplication {

    public static void main(String[] args) throws Exception {
        JDAService.createJDA("");

        SpringApplication.run(DiscordbotApplication.class, args);
    }

}
