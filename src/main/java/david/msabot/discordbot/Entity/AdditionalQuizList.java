package david.msabot.discordbot.Entity;

import java.util.ArrayList;

public class AdditionalQuizList {
    private String channel;
    private ArrayList<Quiz> list;

    public AdditionalQuizList(){}

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setList(ArrayList<Quiz> list) {
        this.list = list;
    }

    public String getChannel() {
        return channel;
    }

    public ArrayList<Quiz> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "AdditionalQuizList{" +
                "channel='" + channel + '\'' +
                ", list=" + list +
                '}';
    }
}
