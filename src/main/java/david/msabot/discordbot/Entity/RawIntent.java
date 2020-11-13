package david.msabot.discordbot.Entity;

public class RawIntent {
    public Intent text;

    public void setText(Intent text) {
        this.text = text;
    }

    public Intent getText() {
        return text;
    }

    @Override
    public String toString() {
        return "RawIntent{" +
                "text=" + text +
                '}';
    }
}
