package david.msabot.discordbot.Entity.CustomConfig;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerConfig {
    private String server;                       // server name
    private ArrayList<CategoryConfig> groups;

    public void setServer(String server) {
        this.server = server;
    }

    public void setGroups(ArrayList<CategoryConfig> groups) {
        this.groups = groups;
    }

    public String getServer() {
        return server;
    }

    public ArrayList<CategoryConfig> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "server='" + server + '\'' +
                ", groups=" + groups +
                '}';
    }

    /**
     * get Hashmap of server config
     * should parse like this: hashmap(groupName, groupConfig)
     * groupName is String
     * @return parsed hashmap
     */
    public HashMap<String, CategoryConfig> getServerConfigMap(){
        HashMap<String, CategoryConfig> result = new HashMap<>();
        for(CategoryConfig categoryConfig : groups){
            result.put(categoryConfig.getGroupName(), categoryConfig);
        }
        return result;
    }
}
