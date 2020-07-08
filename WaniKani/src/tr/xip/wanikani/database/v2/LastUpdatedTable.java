package tr.xip.wanikani.database.v2;

public class LastUpdatedTable {
    public static final String TABLE_NAME = "last_updated";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_QUERY_NAME = "query_name";
    public static final String COLUMN_NAME_QUERY_DATE = "query_date";
    public static final String COLUMN_NAME_NULLABLE = "nullable";

    public static final String[] COLUMNS = {
            COLUMN_NAME_ID,
            COLUMN_NAME_QUERY_NAME,
            COLUMN_NAME_QUERY_DATE,
            COLUMN_NAME_NULLABLE
    };
}
