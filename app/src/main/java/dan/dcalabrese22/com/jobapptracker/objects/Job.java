package dan.dcalabrese22.com.jobapptracker.objects;

/**
 * Created by dcalabrese on 12/5/2017.
 */

public class Job {

    private int jobId;
    private String companyName;
    private String dateApplied;
    private String jobDescription;

    public Job() {}

    public Job(int jobId, String companyName, String dateApplied, String jobDescription) {
        this.jobId = jobId;
        this.companyName = companyName;
        this.dateApplied = dateApplied;
        this.jobDescription = jobDescription;
    }

    public Job(String companyName, String dateApplied, String jobDescription) {
        this.companyName = companyName;
        this.dateApplied = dateApplied;
        this.jobDescription = jobDescription;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(String dateApplied) {
        this.dateApplied = dateApplied;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }
}
