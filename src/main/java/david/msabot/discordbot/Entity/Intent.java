package david.msabot.discordbot.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Intent {
    public String intent;
    public String service = null;

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
        return "Intent{" +
                "intent='" + intent + '\'' +
                ", service='" + service + '\'' +
                '}';
    }
}
