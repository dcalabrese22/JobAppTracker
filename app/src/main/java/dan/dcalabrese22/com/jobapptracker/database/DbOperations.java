package dan.dcalabrese22.com.jobapptracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dan.dcalabrese22.com.jobapptracker.objects.Interaction;
import dan.dcalabrese22.com.jobapptracker.objects.Job;

/**
 * Created by dcalabrese on 12/5/2017.
 */

public class DbOperations {

    private SQLiteDatabase mDatabase;

    public DbOperations(Context context) {
        DbApplication application = (DbApplication) context.getApplicationContext();
        mDatabase = application.getConnection();
    }

    public long insertJob(Job job) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.JobEntry.JOB_COLUMN_COMPANY, job.getCompanyName());
        contentValues.put(DbContract.JobEntry.JOB_COLUMN_DATE_APPLIED, job.getDateApplied());
        contentValues.put(DbContract.JobEntry.JOB_COLUMN_DESCRIPTION, job.getJobDescription());
        return mDatabase.insert(DbContract.JobEntry.JOB_TABLE, null, contentValues);
    }

    public Job getSingleJob(int jobId) {
        String selection = DbContract.JobEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(jobId)};
        Cursor cursor = mDatabase.query(DbContract.JobEntry.JOB_TABLE, null, selection,
                selectionArgs, null, null, null, null);
        cursor.moveToNext();
        String companyName = cursor.getString(cursor.getColumnIndex(DbContract.JobEntry.JOB_COLUMN_COMPANY));
        String dateApplied = cursor.getString(cursor.getColumnIndex(DbContract.JobEntry.JOB_COLUMN_DATE_APPLIED));
        String jobDesc = cursor.getString(cursor.getColumnIndex(DbContract.JobEntry.JOB_COLUMN_DESCRIPTION));
        cursor.close();
        return new Job(jobId, companyName, dateApplied, jobDesc);

    }

    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbContract.JobEntry.JOB_TABLE, null, null,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String companyName = cursor.getString(cursor.getColumnIndex(DbContract.JobEntry.JOB_COLUMN_COMPANY));
            String dateApplied = cursor.getString(cursor.getColumnIndex(DbContract.JobEntry.JOB_COLUMN_DATE_APPLIED));
            String jobDesc = cursor.getString(cursor.getColumnIndex(DbContract.JobEntry.JOB_COLUMN_DESCRIPTION));
            long jobId = cursor.getInt(cursor.getColumnIndex(DbContract.JobEntry._ID));
            Job job = new Job(jobId, companyName, dateApplied, jobDesc);
            jobs.add(job);
        }
        cursor.close();
        return jobs;
    }

    public void insertInteraction(Interaction interaction, long jobId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.InteractionEntry.INTERACTION_COLUMN_NOTE, interaction.getNote());
        contentValues.put(DbContract.InteractionEntry.INTERACTION_COLUMN_FOREIGN_KEY, jobId);
        mDatabase.insert(DbContract.InteractionEntry.INTERACTION_TABLE, null, contentValues);
    }

    public List<Interaction> getAllInteractionsForJob(long jobId) {
        List<Interaction> interactions = new ArrayList<>();
        String selection = DbContract.InteractionEntry.INTERACTION_COLUMN_FOREIGN_KEY + "=?";
        String selectionArgs[] = {String.valueOf(jobId)};
        String orderBy = DbContract.InteractionEntry._ID + " DESC";
        Cursor cursor = mDatabase.query(DbContract.InteractionEntry.INTERACTION_TABLE,
                null, selection, selectionArgs, null, null, orderBy);

        while (cursor.moveToNext()) {
            String note = cursor.getString(cursor.getColumnIndex(DbContract.InteractionEntry.INTERACTION_COLUMN_NOTE));
            Interaction interaction = new Interaction(note);
            interactions.add(interaction);
        }
        cursor.close();
        return interactions;
    }

    public void deleteJob(long jobId) {
        String whereJob = "_ID=?";
        String[] args = {String.valueOf(jobId)};
        mDatabase.delete(DbContract.JobEntry.JOB_TABLE, whereJob, args);
        String whereInteraction = DbContract.InteractionEntry.INTERACTION_COLUMN_FOREIGN_KEY + "=?";
        mDatabase.delete(DbContract.InteractionEntry.INTERACTION_TABLE, whereInteraction, args);
    }

}
