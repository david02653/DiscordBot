package david.msabot.discordbot.Entity.CustomConfig;

import java.util.ArrayList;

public class CategoryConfig {
    private String groupName;              // group name
    private ArrayList<ConfigSet> ruleSet;  // applied rule sets

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setRuleSet(ArrayList<ConfigSet> ruleSet) {
        this.ruleSet = ruleSet;
    }

    public ArrayList<ConfigSet> getRuleSet() {
        return ruleSet;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return "GroupConfig{" +
                "groupName='" + groupName + '\'' +
                ", ruleSet=" + ruleSet +
                '}';
    }

    /**
     *
     * @param channel
     * @return
     */
    public ConfigSet findConfigSetByChannelName(String channel){
        for(ConfigSet set: ruleSet){
            if(set.getChannel().contains(channel))
                return set;
        }
        return null;
    }
}
