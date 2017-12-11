package dan.dcalabrese22.com.jobapptracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import dan.dcalabrese22.com.jobapptracker.database.DbOperations;
import dan.dcalabrese22.com.jobapptracker.objects.Interaction;

public class JobDetailActivity extends AppCompatActivity {

    private TextView mCompanyName;
    private TextView mDateApplied;
    private TextView mJobDescription;
    private EditText mNewInteraction;
    private ImageButton mAddInteraction;
    private RecyclerView mRecyclerView;
    private InteractionsListAdapter mAdapter;
    private long mJobId;
    private String mJobDescriptionText;
    private DbOperations mOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        Bundle extras = getIntent().getExtras();
        mCompanyName = findViewById(R.id.tv_company_name);
        mDateApplied = findViewById(R.id.tv_date);
        mJobDescription = findViewById(R.id.tv_job_description);
        mNewInteraction = findViewById(R.id.et_new_interaction);
        mAddInteraction = findViewById(R.id.btn_add_interaction);
        mRecyclerView = findViewById(R.id.rv_interactions_list);
        mAddInteraction.setOnClickListener(new AddInteractionListener());

        mCompanyName.setText(extras.getString(MainActivity.JOB_COMPANY_NAME_EXTRA));
        mDateApplied.setText(extras.getString(MainActivity.JOB_DATE_APPLIED_EXTRA));
        mJobDescriptionText = extras.getString(MainActivity.JOB_DESCRIPTION_EXTRA);
        mJobDescription.setText(getResources().getString(R.string.tv_job_description_holder));
        mJobDescription.setOnClickListener(new JobDescriptionListener());
        mJobId = extras.getLong(MainActivity.JOB_ID_EXTRA);

        mOperator = new DbOperations(this);

        mAdapter = new InteractionsListAdapter(this);
        List<Interaction> interactionList = mOperator.getAllInteractionsForJob(mJobId);
        Log.d("interactions", interactionList.toString());
        mAdapter.setInteractionData(interactionList);
        Log.d("mAdapter", mAdapter.getInteractionsList().toString());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);


    }

    private class AddInteractionListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!mNewInteraction.getText().toString().equals("")) {
                Interaction interaction = new Interaction(mNewInteraction.getText().toString());
                mOperator.insertInteraction(interaction, mJobId);
                mAdapter.clearInteractions();
                mAdapter.setInteractionData(mOperator.getAllInteractionsForJob(mJobId));
                mNewInteraction.getText().clear();
            }
        }
    }

    private class JobDescriptionListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getTag().equals("hidden")) {
                TextView textView = (TextView) view;
                textView.setText(mJobDescriptionText);
                mNewInteraction.setVisibility(View.INVISIBLE);
                mAddInteraction.setVisibility(View.INVISIBLE);
                textView.setTag("displayed");
            } else if (view.getTag().equals("displayed")) {
                TextView textView = (TextView) view;
                textView.setText(getResources().getString(R.string.tv_job_description_holder));
                textView.setTag("hidden");
                mNewInteraction.setVisibility(View.VISIBLE);
                mAddInteraction.setVisibility(View.VISIBLE);
            }
        }
    }
}
