package dan.dcalabrese22.com.jobapptracker.database;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dcalabrese on 12/5/2017.
 */

public class DbApplication extends Application {

    private DbHelper mDbHelper;

    public DbApplication() {
        mDbHelper = new DbHelper(this);
    }

    public SQLiteDatabase getConnection() {
        return mDbHelper.getWritableDatabase();
    }
}
