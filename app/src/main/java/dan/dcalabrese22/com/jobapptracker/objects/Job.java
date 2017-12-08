package dan.dcalabrese22.com.jobapptracker.objects;

/**
 * Created by dcalabrese on 12/5/2017.
 */

public class Job {

    private long jobId;
    private String companyName;
    private String dateApplied;
    private String jobDescription;

    public Job() {}

    public Job(long jobId, String companyName, String dateApplied, String jobDescription) {
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

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
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
