package david.msabot.discordbot.Entity;

public class AuthorizationGrantParameter {

    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String grant_type;
    private String code;
    private String scope;

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public String getCLIENT_SECRET() {
        return CLIENT_SECRET;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public String getCode() {
        return code;
    }

    public String getScope() {
        return scope;
    }
}
