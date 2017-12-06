package dan.dcalabrese22.com.jobapptracker.objects;

/**
 * Created by dcalabrese on 12/5/2017.
 */

public class Job {

    private String companyName;
    private String dateApplied;
    private String jobDescription;

    public Job() {}

    public Job(String companyName, String dateApplied, String jobDescription) {
        this.companyName = companyName;
        this.dateApplied = dateApplied;
        this.jobDescription = jobDescription;
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
