package david.msabot.discordbot.Entity.CustomConfig;

public class Config {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Config{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
