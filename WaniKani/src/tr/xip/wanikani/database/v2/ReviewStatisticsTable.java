package tr.xip.wanikani.database.v2;

public class ReviewStatisticsTable {
	public static final String TABLE_NAME = "review_statistics";
	public static final String COLUMN_NAME_ID = "_id";

	public static final String COLUMN_NAME_CREATED_AT = "created_at";
	public static final String COLUMN_NAME_HIDDEN = "hidden";
	public static final String COLUMN_NAME_MEANING_CORRECT = "meaning_correct";
	public static final String COLUMN_NAME_MEANING_CURRENT_STREAK = "meaning_current_streak";
	public static final String COLUMN_NAME_MEANING_INCORRECT = "meaning_incorrect";
	public static final String COLUMN_NAME_MEANING_MAX_STREAK = "meaning_max_streak";
	public static final String COLUMN_NAME_PERCENTAGE_CORRECT = "percentage_correct";
	public static final String COLUMN_NAME_READING_CORRECT = "reading_correct";
	public static final String COLUMN_NAME_READING_CURRENT_STREAK = "reading_current_streak";
	public static final String COLUMN_NAME_READING_INCORRECT = "reading_incorrect";
	public static final String COLUMN_NAME_READING_MAX_STREAK = "reading_max_streak";
	public static final String COLUMN_NAME_SUBJECT_ID = "subject_id";
	public static final String COLUMN_NAME_SUBJECT_TYPE = "subject_type";

	public static final String COLUMN_NAME_NULLABLE = "nullable";

	public static final String[] COLUMNS = {
		COLUMN_NAME_ID,
		COLUMN_NAME_CREATED_AT,
		COLUMN_NAME_HIDDEN,
		COLUMN_NAME_MEANING_CORRECT,
		COLUMN_NAME_MEANING_CURRENT_STREAK,
		COLUMN_NAME_MEANING_INCORRECT,
		COLUMN_NAME_MEANING_MAX_STREAK,
		COLUMN_NAME_PERCENTAGE_CORRECT,
		COLUMN_NAME_READING_CORRECT,
		COLUMN_NAME_READING_CURRENT_STREAK,
		COLUMN_NAME_READING_INCORRECT,
		COLUMN_NAME_READING_MAX_STREAK,
		COLUMN_NAME_SUBJECT_ID,
		COLUMN_NAME_SUBJECT_TYPE,
		COLUMN_NAME_NULLABLE
	};
}
