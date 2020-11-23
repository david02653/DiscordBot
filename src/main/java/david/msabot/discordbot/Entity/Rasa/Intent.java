package david.msabot.discordbot.Entity.Rasa;

public class Intent {
    public String recipient_id;
    public TextField text;

    public void setText(TextField text) {
        this.text = text;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public TextField getText() {
        return text;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    @Override
    public String toString() {
        return "Intent{" +
                "text=" + text +
                ", recipient_id='" + recipient_id + '\'' +
                '}';
    }

//    @Override
//    public String toString() {
//        return "Intent{" +
//                "text=" + text +
//                '}';
//    }


}


