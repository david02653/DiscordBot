package david.msabot.discordbot.Entity.Jenkins;

import com.google.gson.Gson;
import david.msabot.discordbot.Service.AdditionalQAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

public class JenkinsTestReport {

    private ArrayList<JenkinsTestSuite> testSuites;
    private int totalDuration;
    private int failedCount;
    private int passedCount;
    private int skipCount;
    private String reportDetailUrl;

    public JenkinsTestReport(){
        this.testSuites = new ArrayList<>();
    }

    public static JenkinsTestReport createJenkinsTestReport(int totalDuration, int failedCount, int passedCount, int skipCount){
        JenkinsTestReport newReport = new JenkinsTestReport();
        newReport.setTotalDuration(totalDuration);
        newReport.setFailedCount(failedCount);
        newReport.setPassedCount(passedCount);
        newReport.setSkipCount(skipCount);
        return newReport;
    }

    public void setTestSuites(ArrayList<JenkinsTestSuite> testSuites) {
        this.testSuites = testSuites;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public void setPassedCount(int passedCount) {
        this.passedCount = passedCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public void setReportDetailUrl(String reportDetailUrl) {
        this.reportDetailUrl = reportDetailUrl;
    }

    public ArrayList<JenkinsTestSuite> getTestSuites() {
        return testSuites;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public int getPassedCount() {
        return passedCount;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public int getSkipCount() {
        return skipCount;
    }

    public String getReportDetailUrl() {
        return reportDetailUrl;
    }

    /**
     * json string format output
     * @return object json string
     */
    public String asJsonString(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    @Override
    public String toString() {
//        return "JenkinsTestReport{" +
//                "totalDuration=" + totalDuration +
//                ", failedCount=" + failedCount +
//                ", passedCount=" + passedCount +
//                ", skipCount=" + skipCount +
//                ", reportDetailUrl='" + reportDetailUrl +
//                "'}";
        return "[Jenkins Test Report] :" + "\n" +
                "Total Duration = " + totalDuration + "\n" +
                "Failed count = " + failedCount + "\n" +
                "Passed count = " + passedCount + "\n" +
                "Skipped count = " + skipCount + "\n" +
                "Check this url for report details: " + reportDetailUrl;
    }
}
