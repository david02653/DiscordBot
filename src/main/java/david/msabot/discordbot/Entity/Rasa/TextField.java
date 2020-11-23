package david.msabot.discordbot.Entity.Rasa;

public class TextField{
    public String intent;
    public String service;

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public void setService(String service) {
        this.service = service;
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
