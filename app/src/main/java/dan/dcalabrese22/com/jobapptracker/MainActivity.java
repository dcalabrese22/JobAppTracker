package dan.dcalabrese22.com.jobapptracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import dan.dcalabrese22.com.jobapptracker.interfaces.JobClickHandler;
import dan.dcalabrese22.com.jobapptracker.objects.Job;

public class MainActivity extends AppCompatActivity implements JobClickHandler{

    private Button mNewJobBtn;
    private Context mContext;
    private RecyclerView mRecyclerview;
    private JobListAdapter mAdapter;

    public static final String JOB_ID_EXTRA = "job_id_extra";
    public static final String JOB_COMPANY_NAME_EXTRA = "company_name_extra";
    public static final String JOB_DATE_APPLIED_EXTRA = "date_applied_extra";
    public static final String JOB_DESCRIPTION_EXTRA = "description_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mNewJobBtn = findViewById(R.id.btn_new_job);
        mRecyclerview = findViewById(R.id.rv_jobs_list);

        mAdapter = new JobListAdapter(this, this);
        mAdapter.setJobData();
        mRecyclerview.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(manager);
        mNewJobBtn.setOnClickListener(new NewJobBtnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setJobData();
    }

    @Override
    public void onJobClick(Job job) {
        Intent intent = new Intent(mContext, JobDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(JOB_ID_EXTRA, job.getJobId());
        bundle.putString(JOB_COMPANY_NAME_EXTRA, job.getCompanyName());
        bundle.putString(JOB_DATE_APPLIED_EXTRA, job.getDateApplied());
        bundle.putString(JOB_DESCRIPTION_EXTRA, job.getJobDescription());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class NewJobBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, NewJobActivity.class);
            startActivity(intent);
        }
    }
}
