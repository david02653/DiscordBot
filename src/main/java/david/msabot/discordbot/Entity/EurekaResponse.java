package david.msabot.discordbot.Entity;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.ArrayList;

public class EurekaResponse {
    private String versionsDelta;
    private String appsHashCode;
    private ArrayList<Application> appList;

    public void setVersionsDelta(String versionsDelta) {
        this.versionsDelta = versionsDelta;
    }

    public void setAppsHashCode(String appsHashCode) {
        this.appsHashCode = appsHashCode;
    }

    public void setAppList(ArrayList<Application> appList) {
        this.appList = appList;
    }

    public String getVersionsDelta() {
        return versionsDelta;
    }

    public String getAppsHashCode() {
        return appsHashCode;
    }

    public ArrayList<Application> getAppList() {
        return appList;
    }

    @Override
    public String toString() {
        return "EurekaResponse{" +
                "versionsDelta='" + versionsDelta + '\'' +
                ", appsHashCode='" + appsHashCode + '\'' +
                ", appList=" + appList +
                '}';
    }
}

class Application{
    private String name;
    private ArrayList<Instance> instanceList;

    public void setName(String name) {
        this.name = name;
    }

    public void setInstanceList(ArrayList<Instance> instanceList) {
        this.instanceList = instanceList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Instance> getInstanceList() {
        return instanceList;
    }

    @Override
    public String toString() {
        return "Application{" +
                "name='" + name + '\'' +
                ", instanceList=" + instanceList +
                '}';
    }
}

class Instance{
    private String instanceId;
    private String hostName;
    private String app;
    private String ipAddr;
    private String status;
    private String overriddenStatus;
    private String port;
    private String securePort;
    private int countryId;
    private DataCenterInfo dataCenterInfo;
    private LeaseInfo leaseInfo;
    private MetaData metaData;
    private String homePageUrl;
    private String statusPageUrl;
    private String healthCheckUrl;
    private String vipAddress;
    private String secureVipAddress;
    private boolean isCoordinatingDiscoveryServer;
    private String lastUpdatedTimestamp;
    private String lastDirtyTimestamp;
    private String actionType;

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOverriddenStatus(String overriddenStatus) {
        this.overriddenStatus = overriddenStatus;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setSecurePort(String securePort) {
        this.securePort = securePort;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setDataCenterInfo(DataCenterInfo dataCenterInfo) {
        this.dataCenterInfo = dataCenterInfo;
    }

    public void setLeaseInfo(LeaseInfo leaseInfo) {
        this.leaseInfo = leaseInfo;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    public void setStatusPageUrl(String statusPageUrl) {
        this.statusPageUrl = statusPageUrl;
    }

    public void setHealthCheckUrl(String healthCheckUrl) {
        this.healthCheckUrl = healthCheckUrl;
    }

    public void setVipAddress(String vipAddress) {
        this.vipAddress = vipAddress;
    }

    public void setSecureVipAddress(String secureVipAddress) {
        this.secureVipAddress = secureVipAddress;
    }

    public void setCoordinatingDiscoveryServer(boolean coordinatingDiscoveryServer) {
        isCoordinatingDiscoveryServer = coordinatingDiscoveryServer;
    }

    public void setLastUpdatedTimestamp(String lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public void setLastDirtyTimestamp(String lastDirtyTimestamp) {
        this.lastDirtyTimestamp = lastDirtyTimestamp;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}

class DataCenterInfo{
    private String name;
}

class LeaseInfo{
    private int renewalIntervalInSecs;
    private int durationInSecs;
    private String registrationTimestamp;
    private String lastRenewalTimestamp;
    private String evictionTimestamp;
    private String serviceUpTimestamp;
}

class MetaData{
    private int managementPort;
}

/*
<applications>
  <versions__delta>1</versions__delta>
  <apps__hashcode>UP_6_</apps__hashcode>
  <application>
    <name>CINEMACATALOG</name>
    <instance>
      <instanceId>3cf16c3aa1dc:cinemacatalog:9014</instanceId>
      <hostName>140.121.197.130</hostName>
      <app>CINEMACATALOG</app>
      <ipAddr>140.121.197.130</ipAddr>
      <status>UP</status>
      <overriddenstatus>UNKNOWN</overriddenstatus>
      <port enabled="true">9014</port>
      <securePort enabled="false">443</securePort>
      <countryId>1</countryId>
      <dataCenterInfo class="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo">
        <name>MyOwn</name>
      </dataCenterInfo>
      <leaseInfo>
        <renewalIntervalInSecs>1</renewalIntervalInSecs>
        <durationInSecs>2</durationInSecs>
        <registrationTimestamp>1606073608201</registrationTimestamp>
        <lastRenewalTimestamp>1606075092179</lastRenewalTimestamp>
        <evictionTimestamp>0</evictionTimestamp>
        <serviceUpTimestamp>1606073608201</serviceUpTimestamp>
      </leaseInfo>
      <metadata>
        <management.port>9014</management.port>
      </metadata>
      <homePageUrl>http://140.121.197.130:9014/</homePageUrl>
      <statusPageUrl>http://140.121.197.130:9014/info</statusPageUrl>
      <healthCheckUrl>http://140.121.197.130:9014/health</healthCheckUrl>
      <vipAddress>cinemacatalog</vipAddress>
      <secureVipAddress>cinemacatalog</secureVipAddress>
      <isCoordinatingDiscoveryServer>false</isCoordinatingDiscoveryServer>
      <lastUpdatedTimestamp>1606073608201</lastUpdatedTimestamp>
      <lastDirtyTimestamp>1606073608132</lastDirtyTimestamp>
      <actionType>ADDED</actionType>
    </instance>
  </application>
  <application></application>
  <application></application>
  ...
</applications>
 */