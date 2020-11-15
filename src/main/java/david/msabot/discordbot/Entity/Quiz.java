package david.msabot.discordbot.Entity;

public class Quiz {
    private String creator;
    private String question;
    private String answer;

    public Quiz(){}

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreator() {
        return creator;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "creator='" + creator + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
