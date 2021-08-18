package david.msabot.discordbot.Entity.CustomConfig;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigSet {
    private ArrayList<String> channel; // applied channel name
    private ArrayList<Config> configs; // applied configs

    public void setChannel(ArrayList<String> channel) {
        this.channel = channel;
    }

    public void setConfigs(ArrayList<Config> configs) {
        this.configs = configs;
    }

    public ArrayList<String> getChannel() {
        return channel;
    }

    public ArrayList<Config> getConfigs() {
        return configs;
    }

    @Override
    public String toString() {
        return "ConfigSet{" +
                "channel=" + channel +
                ", configs=" + configs +
                '}';
    }

    /**
     * query config by given config name
     * if no config matches with given name, return an empty string
     * @param configName target config name
     * @return matched config name, empty if nothing found
     */
    public String getConfigByConfigName(String configName){
        for(Config config: configs){
            if(config.getName().equals(configName)){
                return config.getValue();
            }
        }
        return "";
    }

    /**
     * query config value with input string
     * expect keyword like this: "jenkins endpoint"
     * input will be split into tokens by ' '
     * if any config key contains all token is found, return to config value
     * only the first matched one will be return
     * an empty string will be return if nothing matched found
     * @param keyword keyword input
     * @return first matched config, empty if nothing found
     */
    public String getConfigByKeyword(String keyword){
        String[] key = keyword.split(" ");
        for(Config config: configs){
            if(Arrays.stream(key).allMatch(token -> config.getName().contains(token)))
                return config.getValue();
        }
        return "";
    }
}
