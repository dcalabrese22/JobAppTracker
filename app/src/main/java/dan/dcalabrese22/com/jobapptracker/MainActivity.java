package dan.dcalabrese22.com.jobapptracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import dan.dcalabrese22.com.jobapptracker.database.DbOperations;
import dan.dcalabrese22.com.jobapptracker.interfaces.JobClickHandler;
import dan.dcalabrese22.com.jobapptracker.objects.Job;

public class MainActivity extends AppCompatActivity implements JobClickHandler{

    private FloatingActionButton mNewJobBtn;
    private Context mContext;
    private RecyclerView mRecyclerview;
    private JobListAdapter mAdapter;
    private DbOperations mOperator;
    private SignInButton mSignInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private DriveResourceClient mDriveResourceClient;
    private DriveClient mDriveClient;
    private TaskCompletionSource<DriveId> mOpenItemTaskSource;

    public static final String JOB_ID_EXTRA = "job_id_extra";
    public static final String JOB_COMPANY_NAME_EXTRA = "company_name_extra";
    public static final String JOB_DATE_APPLIED_EXTRA = "date_applied_extra";
    public static final String JOB_DESCRIPTION_EXTRA = "description_extra";
    public static final int RC_SIGN_ON = 1;
    public static final int REQUEST_CODE_OPEN_ITEM = 2;

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUi(account);
        mDriveResourceClient = Drive.getDriveResourceClient(this, account);
        mDriveClient = Drive.getDriveClient(this, account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInButton.setOnClickListener(new SignInButtonListener());

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Drive.SCOPE_FILE)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

    /**
     * Prompts the user to select a folder using OpenFileActivity.
     *
     * @param openOptions Filter that should be applied to the selection
     * @return Task that resolves with the selected item's ID.
     */
    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions) {
        mOpenItemTaskSource = new TaskCompletionSource<>();
        mDriveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith(new Continuation<IntentSender, Void>() {
                    @Override
                    public Void then(@NonNull Task<IntentSender> task) throws Exception {
                        startIntentSenderForResult(
                                task.getResult(), REQUEST_CODE_OPEN_ITEM, null, 0, 0, 0);
                        return null;
                    }
                });
        return mOpenItemTaskSource.getTask();
    }



    public void openFile(DriveFile file) {
        Task<DriveContents> openFileTask = mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);

        openFileTask.continueWithTask(new Continuation<DriveContents, Task<Void>>() {
            @Override
            public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {
                DriveContents contents = task.getResult();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(contents.getInputStream()))) {
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                    Log.d("Message", builder.toString());
                }

                Task<Void> discardTask = mDriveResourceClient.discardContents(contents);
                return discardTask;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setJobData();
    }

    public void updateUi(GoogleSignInAccount account) {
        if (account != null) {
            mRecyclerview.setVisibility(View.VISIBLE);
            mNewJobBtn.setVisibility(View.VISIBLE);
            mSignInButton.setVisibility(View.INVISIBLE);
        } else {
            mRecyclerview.setVisibility(View.INVISIBLE);
            mNewJobBtn.setVisibility(View.INVISIBLE);
            mSignInButton.setVisibility(View.VISIBLE);
        }
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

    private class SignInButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_ON);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_ON:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
            case REQUEST_CODE_OPEN_ITEM:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    mOpenItemTaskSource.setResult(driveId);
                } else {
                    mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }
                break;
        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            updateUi(account);
            mDriveResourceClient = Drive.getDriveResourceClient(this, account);
            mDriveClient = Drive.getDriveClient(this, account);
        } catch (ApiException e) {
            e.printStackTrace();
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
                break;
            case (R.id.logout):
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mRecyclerview.setVisibility(View.INVISIBLE);
                                mNewJobBtn.setVisibility(View.INVISIBLE);
                                mSignInButton.setVisibility(View.VISIBLE);
                            }
                        });
        }

        return super.onOptionsItemSelected(item);
    }
}
