package tr.xip.wanikani.database.v2;

import android.provider.BaseColumns;

public class SummaryTable implements BaseColumns {
    public static final String TABLE_NAME = "summary";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_SUBJECT_ID = "subject_id";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_AVAILABLE_AT = "available_at";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_TYPE,
            COLUMN_NAME_SUBJECT_ID,
            COLUMN_NAME_AVAILABLE_AT,
            COLUMN_NAME_NULLABLE
    };
}
