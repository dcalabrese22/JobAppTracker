package dan.dcalabrese22.com.jobapptracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dan.dcalabrese22.com.jobapptracker.database.DbOperations;
import dan.dcalabrese22.com.jobapptracker.interfaces.JobClickHandler;
import dan.dcalabrese22.com.jobapptracker.objects.Job;

/**
 * Created by dcalabrese on 12/6/2017.
 */

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {

    private List<Job> mJobs;
    private Context mContext;
    private DbOperations operator;
    private JobClickHandler mClickHandler;

    public JobListAdapter(Context context, JobClickHandler handler) {
        mContext = context;
        operator = new DbOperations(context);
        mClickHandler = handler;
    }

    public void setJobData() {
        mJobs = operator.getAllJobs();
        notifyDataSetChanged();
    }

    public List<Job> getJobs() {
        return mJobs;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        TextView company = holder.mCompanyName;
        TextView date = holder.mDateApplied;
        TextView description = holder.mJobDescription;
        Job job = mJobs.get(position);
        company.setText(job.getCompanyName());
        date.setText(job.getDateApplied());
        description.setText(job.getJobDescription());
    }

    @Override
    public int getItemCount() {
        if (mJobs == null) {
            return 0;
        }
        return mJobs.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {

        private TextView mCompanyName;
        private TextView mDateApplied;
        private TextView mJobDescription;

        public JobViewHolder(View view) {
            super(view);
            mCompanyName = view.findViewById(R.id.tv_company_name);
            mDateApplied = view.findViewById(R.id.tv_date);
            mJobDescription = view.findViewById(R.id.tv_job_description);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Job job = mJobs.get(getAdapterPosition());
                    mClickHandler.onJobClick(job);
                }
            });
        }
    }
}
