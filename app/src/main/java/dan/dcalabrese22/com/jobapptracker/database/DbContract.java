package dan.dcalabrese22.com.jobapptracker.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dcalabrese on 12/5/2017.
 */

public class DbContract {

    public static final String CONTENT_AUTHORITY = "dan.dcalabrese22.com.jobapptracker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_JOB = "job";
    public static final String PATH_INTERACTION = "interaction";

    public static class JobEntry implements BaseColumns {

        public static final Uri JOB_CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_JOB)
                .build();

        public static final String JOB_TABLE = "job";

        public static final String JOB_COLUMN_COMPANY = "company";
        public static final String JOB_COLUMN_DATE_APPLIED = "date_applied";
        public static final String JOB_COLUMN_DESCRIPTION = "job_description";
    }

    public static class InteractionEntry implements BaseColumns {

        public static final Uri NOTE_CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_INTERACTION)
                .build();

        public static final String INTERACTION_TABLE = "interaction";

        public static final String INTERACTION_COLUMN_FOREIGN_KEY = "job_id";
        public static final String INTERACTION_COLUMN_NOTE = "note";
    }
}
