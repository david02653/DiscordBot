package david.msabot.discordbot.Entity;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.ArrayList;

public class EurekaResponse {
    private String versionsDelta;
    private String appsHashCode;
    private ArrayList<Application> appList;
}

class Application{
    private String name;
    private ArrayList<Instance> instanceList;
}

class Instance{
    private String id;
    private String hostName;
    private String app;
    private String ipAddr;
    private String status;
    private String overriddenStatus;
    private String port;

}

class DataCenterInfo{
}

class LeaseInfo{
}

class MetaData{
}