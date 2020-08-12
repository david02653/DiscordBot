package david.msabot.discordbot.Entity;

public class BotAuthParam {
    public String code;
    public String guildId;
    public Integer permissions;

    public BotAuthParam(String code, String guildId, Integer permissions){
        this.code = code;
        this.guildId = guildId;
        this.permissions = permissions;
    }

    public String getCode() {
        return code;
    }

    public String getGuildId() {
        return guildId;
    }

    public Integer getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "BotAuthParam{" +
                "code='" + code + '\'' +
                ", guildId='" + guildId + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
