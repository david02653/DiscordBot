package david.msabot.discordbot.Entity.AdditionalQuestion;

import java.util.ArrayList;

public class AdditionalQuizList {
    private String channel;
    private ArrayList<QuestionList> list;

    public AdditionalQuizList(){}

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setList(ArrayList<QuestionList> list) {
        this.list = list;
    }

    public String getChannel() {
        return channel;
    }

    public ArrayList<QuestionList> getList() {
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
