package dan.dcalabrese22.com.jobapptracker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import dan.dcalabrese22.com.jobapptracker.database.DbOperations;
import dan.dcalabrese22.com.jobapptracker.objects.Job;

public class NewJobActivity extends AppCompatActivity {

    private EditText mCompanyName;
    private EditText mDateApplied;
    private EditText mJobDescription;
    private Button mGetFromClipboardBtn;
    private Button mAddJobBtn;
    private Context mContext;
    private DbOperations mOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        mContext = this;
        mCompanyName = findViewById(R.id.et_company_name);
        mDateApplied = findViewById(R.id.et_date_applied);
        mDateApplied.setText(getTodaysDate());
        mJobDescription = findViewById(R.id.et_job_description);
        mGetFromClipboardBtn = findViewById(R.id.btn_get_link_from_clipboard);
        mAddJobBtn = findViewById(R.id.btn_add_new_job);
        mOperations = new DbOperations(this);

        mGetFromClipboardBtn.setOnClickListener(new GetFromClipboardButtonListener());
        mAddJobBtn.setOnClickListener(new AddJobButtonListener());

        mJobDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    addJob();
                    return true;
                }
                return false;
            }
        });
    }

    private class GetFromClipboardButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = manager.getPrimaryClip();
            if (data != null) {
                mJobDescription.setText(data.getItemAt(0).getText());
            }
        }
    }

    private class AddJobButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            addJob();
        }
    }

    public void addJob() {
        if (!mCompanyName.getText().toString().equals("") && !mDateApplied.getText().toString().equals("") &&
                !mJobDescription.getText().toString().equals("")) {
            Job job = new Job(mCompanyName.getText().toString(), mDateApplied.getText().toString(),
                    mJobDescription.getText().toString());
            long id = mOperations.insertJob(job);
            job.setJobId(id);
            mCompanyName.getText().clear();
            mJobDescription.getText().clear();
            mDateApplied.setText(getTodaysDate());
        }
    }

    public String getTodaysDate() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(new Date());
    }


}
