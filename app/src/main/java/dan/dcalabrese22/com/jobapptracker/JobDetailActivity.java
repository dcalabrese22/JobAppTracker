package dan.dcalabrese22.com.jobapptracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import dan.dcalabrese22.com.jobapptracker.database.DbOperations;
import dan.dcalabrese22.com.jobapptracker.objects.Interaction;

public class JobDetailActivity extends AppCompatActivity {

    private TextView mCompanyName;
    private TextView mDateApplied;
    private TextView mJobDescription;
    private EditText mNewInteraction;
    private Button mAddInteraction;
    private RecyclerView mRecyclerView;
    private int mJobId;
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
        mJobDescription.setText(extras.getString(MainActivity.JOB_DESCRIPTION_EXTRA));
        mJobId = extras.getInt(MainActivity.JOB_ID_EXTRA);

        mOperator = new DbOperations(this);


        List<Interaction> interactionList = mOperator.getAllInteractionsForJob(mJobId);


    }

    private class AddInteractionListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!mNewInteraction.getText().toString().equals("")) {
                Interaction interaction = new Interaction(mNewInteraction.getText().toString());
                mOperator.insertInteraction(interaction, mJobId);
            }
        }
    }
}
