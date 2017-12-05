package dan.dcalabrese22.com.jobapptracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dcalabrese on 12/5/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "JobApplications.db";
    public static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_JOB_TABLE =
                "Create Table " + DbContract.JobEntry.JOB_TABLE + " (" +
                        DbContract.JobEntry._ID + " Integer Primary Key Autoincrement, " +
                        DbContract.JobEntry.JOB_COLUMN_COMPANY + " Text Not null, " +
                        DbContract.JobEntry.JOB_COLUMN_DATE_APPLIED + " Text Not null, " +
                        DbContract.JobEntry.JOB_COLUMN_DESCRIPTION + " Text Not null" +
                        ");";

        final String SQL_CREATE_INTERACTION_TABLE =
                "Create Table " + DbContract.InteractionEntry.INTERACTION_TABLE + " (" +
                        DbContract.InteractionEntry._ID + " Integer Primary Key Autoincrement, " +
                        DbContract.InteractionEntry.INTERACTION_COLUMN_FOREIGN_KEY + " Integer Not null, " +
                        DbContract.InteractionEntry.INTERACTION_COLUMN_NOTE + "Text Not null, " +
                        "Foreign Key (" + DbContract.InteractionEntry.INTERACTION_COLUMN_FOREIGN_KEY + ") References " +
                        DbContract.JobEntry.JOB_TABLE + "(" + DbContract.JobEntry._ID + ")" +
                        ");";

        sqLiteDatabase.execSQL(SQL_CREATE_JOB_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INTERACTION_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
