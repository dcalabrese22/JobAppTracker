package dan.dcalabrese22.com.jobapptracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import dan.dcalabrese22.com.jobapptracker.database.DbOperations;
import dan.dcalabrese22.com.jobapptracker.interfaces.JobClickHandler;
import dan.dcalabrese22.com.jobapptracker.objects.Job;

public class MainActivity extends AppCompatActivity implements JobClickHandler{

    private FloatingActionButton mNewJobBtn;
    private Context mContext;
    private RecyclerView mRecyclerview;
    private JobListAdapter mAdapter;
    private DbOperations mOperator;

    public static final String JOB_ID_EXTRA = "job_id_extra";
    public static final String JOB_COMPANY_NAME_EXTRA = "company_name_extra";
    public static final String JOB_DATE_APPLIED_EXTRA = "date_applied_extra";
    public static final String JOB_DESCRIPTION_EXTRA = "description_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOperator = new DbOperations(this);
        mContext = this;
        mNewJobBtn = findViewById(R.id.btn_new_job);
        mRecyclerview = findViewById(R.id.rv_jobs_list);

        mAdapter = new JobListAdapter(this, this);
        mAdapter.setJobData();
        mRecyclerview.setAdapter(mAdapter);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(manager);
        mNewJobBtn.setOnClickListener(new NewJobBtnClickListener());

        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Job job = mAdapter.getJobs().get(position);
                if (direction == ItemTouchHelper.LEFT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(getResources().getString(R.string.request_to_delete));
                    builder.setPositiveButton(getResources().getString(R.string.delete_alert_button),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mAdapter.notifyItemRemoved(position);
                                    mOperator.deleteJob(job.getJobId());
                                    mAdapter.getJobs().remove(position);
                                }
                            }).setNegativeButton(getResources().getString(R.string.cancel_alert_button),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mAdapter.notifyItemRemoved(position + 1);
                                    mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                                }
                            }).setCancelable(false)
                            .show();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

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
        bundle.putLong(JOB_ID_EXTRA, job.getJobId());
        bundle.putString(JOB_COMPANY_NAME_EXTRA, job.getCompanyName());
        bundle.putString(JOB_DATE_APPLIED_EXTRA, job.getDateApplied());
        bundle.putString(JOB_DESCRIPTION_EXTRA, job.getJobDescription());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onJobDescriptionClick(View view, Job job) {
        if (view.getTag().equals("hidden")) {
            TextView textView = (TextView) view;
            textView.setText(job.getJobDescription());
            textView.setTag("displayed");
        } else if (view.getTag().equals("displayed")) {
            TextView textView = (TextView) view;
            textView.setText(getResources().getString(R.string.tv_job_description_holder));
            textView.setTag("hidden");
        }
    }

    private class NewJobBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, NewJobActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.action_write_to_drive):

            }
        return super.onOptionsItemSelected(item);
    }
}
