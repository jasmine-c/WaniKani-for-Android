package tr.xip.wanikani.database.v2;

public class AssignmentsTable {
    public static final String TABLE_NAME = "assignments";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_SUBJECT_ID = "subject_id";
    public static final String COLUMN_NAME_SUBJECT_TYPE = "subject_type";
    public static final String COLUMN_NAME_SRS_STAGE = "srs_stage";
    public static final String COLUMN_NAME_CREATED_AT = "created_at";
    public static final String COLUMN_NAME_UNLOCKED_AT = "unlocked_at";
    public static final String COLUMN_NAME_STARTED_AT = "started_at";
    public static final String COLUMN_NAME_PASSED_AT = "passed_at";
    public static final String COLUMN_NAME_BURNED_AT = "burned_at";
    public static final String COLUMN_NAME_AVAILABLE_AT = "available_at";
    public static final String COLUMN_NAME_RESURRECTED_AT = "resurrected_at";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_SUBJECT_ID,
            COLUMN_NAME_SUBJECT_TYPE,
            COLUMN_NAME_SRS_STAGE,
            COLUMN_NAME_CREATED_AT,
            COLUMN_NAME_UNLOCKED_AT,
            COLUMN_NAME_STARTED_AT,
            COLUMN_NAME_PASSED_AT,
            COLUMN_NAME_BURNED_AT,
            COLUMN_NAME_AVAILABLE_AT,
            COLUMN_NAME_RESURRECTED_AT,
            COLUMN_NAME_NULLABLE
    };
}
