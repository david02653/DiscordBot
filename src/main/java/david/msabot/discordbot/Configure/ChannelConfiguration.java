package david.msabot.discordbot.Configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import david.msabot.discordbot.Entity.CustomConfig.CategoryConfig;
import david.msabot.discordbot.Entity.CustomConfig.ConfigSet;
import david.msabot.discordbot.Entity.CustomConfig.ServerConfig;
import net.dv8tion.jda.api.entities.TextChannel;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * read user's custom setting when spring startup
 * we suppose MSDOBot maintains ONLY ONE SERVER, so there should only have one server custom setting instance existing in the same time
 */
@Configuration
public class ChannelConfiguration {

    private final String customSettingPath;
    private static List<ServerConfig> serverConfig;

    public ChannelConfiguration(Environment env){
        customSettingPath = env.getProperty("custom.setting.file");

        // load current custom setting file
        if(loadFile())
            System.out.println("[DEBUG][ChannelConfiguration]: custom setting file load successfully.");
        else
            System.out.println("[DEBUG][ChannelConfiguration]: custom setting file load failed.");
    }

    /**
     * read custom setting file and parse into instance
     * @return if setting file is load and parsed successfully
     */
    public boolean loadFile(){
        Yaml yaml = new Yaml();
        try{
            YAMLFactory factory = new YAMLFactory();
            ObjectMapper mapper = new ObjectMapper();
            YAMLParser parser = factory.createParser(new File(customSettingPath));
            serverConfig = mapper.readValues(parser, ServerConfig.class).readAll();
            System.out.println(serverConfig);
        }catch (IOException ioe){
            ioe.printStackTrace();
            System.out.println("[DEBUG][ChannelConfiguration]: custom setting file parse failed.");
            return false;
        }
        return true;
    }

    /**
     * get current server custom config
     * @return first object in List of serverConfig
     */
    public ServerConfig getCurrentServerCustomConfig(){
        return serverConfig.get(0);
    }

    /**
     * get config set by channel name
     * 1. use channel name to get category name
     * 2. use category name (group name) to get ruleSets (GroupConfig)
     * 3. get target config set by channel name
     * result should be a configSet, contains applied channel names and configs
     * @param channel target channel name
     * @return target config
     */
    public ConfigSet getConfigSetByChannelName(TextChannel channel){
        String categoryName = channel.getParent().getName();
        CategoryConfig config = getCurrentServerCustomConfig().getServerConfigMap().get(categoryName);
        return config.findConfigSetByChannelName(channel.getName());
    }
}
