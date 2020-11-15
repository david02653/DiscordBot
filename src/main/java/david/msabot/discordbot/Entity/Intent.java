package david.msabot.discordbot.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    public String getIntent(){
        return this.text.getIntent();
    }
    public String getService(){
        return this.text.getService();
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

    static class TextField{
        public static String intent;
        public static String service;

        public void setIntent(String intent) {
            TextField.intent = intent;
        }

        public void setService(String service) {
            TextField.service = service;
        }

        public String getIntent() {
            return intent;
        }

        public String getService() {
            return service;
        }

        @Override
        public String toString() {
            return "textField{" +
                    "intent='" + intent + '\'' +
                    ", service='" + service + '\'' +
                    '}';
        }
    }
}


