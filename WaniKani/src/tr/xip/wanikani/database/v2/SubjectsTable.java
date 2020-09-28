package tr.xip.wanikani.database.v2;

public class SubjectsTable {
	public static final String TABLE_NAME = "subjects";
	public static final String COLUMN_NAME_ID = "_id";

	// GENERAL
	public static final String COLUMN_NAME_SUBJECT_ID = "subject_id";
	public static final String COLUMN_NAME_SUBJECT_TYPE = "subject_type";
	public static final String COLUMN_NAME_AUXILIARY_MEANINGS = "auxiliary_meanings";
	public static final String COLUMN_NAME_CHARACTERS = "characters";
	public static final String COLUMN_NAME_CREATED_AT = "created_at";
	public static final String COLUMN_NAME_DOCUMENT_URL = "document_url";
	public static final String COLUMN_NAME_HIDDEN_AT = "hidden_at";
	public static final String COLUMN_NAME_LESSON_POSITION = "lesson_position";
	public static final String COLUMN_NAME_LEVEL = "level";
	public static final String COLUMN_NAME_MEANING_MNEMONIC = "meaning_mnemonic";
	public static final String COLUMN_NAME_MEANINGS = "meanings";
	public static final String COLUMN_NAME_SLUG = "slug";
	public static final String COLUMN_NAME_SPACED_REPETITION_SYSTEM_ID = "spaced_repetition_system_id";

	// SPECIFIC
	public static final String COLUMN_NAME_AMALGAMATION_SUBJECT_IDS = "amalgamation_subject_ids";
	public static final String COLUMN_NAME_CHARACTER_IMAGES = "character_images";
	public static final String COLUMN_NAME_COMPONENT_SUBJECT_IDS = "component_subject_ids";
	public static final String COLUMN_NAME_MEANING_HINT = "meaning_hint";
	public static final String COLUMN_NAME_READING_HINT = "reading_hint";
	public static final String COLUMN_NAME_READING_MNEMONIC = "reading_mnemonic";
	public static final String COLUMN_NAME_READINGS = "readings";
	public static final String COLUMN_NAME_VISUALLY_SIMILAR_SUBJECT_IDS = "visually_similar_subject_ids";
	public static final String COLUMN_NAME_CONTEXT_SENTENCES = "context_sentences";
	public static final String COLUMN_NAME_PARTS_OF_SPEECH = "parts_of_speech";
	public static final String COLUMN_NAME_PRONOUNCIATION_AUDIOS = "pronunciation_audios";

	public static final String COLUMN_NAME_NULLABLE = "nullable";

	public static final String[] COLUMNS = {
		COLUMN_NAME_ID,
		COLUMN_NAME_SUBJECT_ID,
		COLUMN_NAME_SUBJECT_TYPE,
		COLUMN_NAME_AUXILIARY_MEANINGS,
		COLUMN_NAME_CHARACTERS,
		COLUMN_NAME_CREATED_AT,
		COLUMN_NAME_DOCUMENT_URL,
		COLUMN_NAME_HIDDEN_AT,
		COLUMN_NAME_LESSON_POSITION,
		COLUMN_NAME_LEVEL,
		COLUMN_NAME_MEANING_MNEMONIC,
		COLUMN_NAME_MEANINGS,
		COLUMN_NAME_SLUG,
		COLUMN_NAME_SPACED_REPETITION_SYSTEM_ID,
		COLUMN_NAME_AMALGAMATION_SUBJECT_IDS,
		COLUMN_NAME_CHARACTER_IMAGES,
		COLUMN_NAME_COMPONENT_SUBJECT_IDS,
		COLUMN_NAME_MEANING_HINT,
		COLUMN_NAME_READING_HINT,
		COLUMN_NAME_READING_MNEMONIC,
		COLUMN_NAME_READINGS,
		COLUMN_NAME_VISUALLY_SIMILAR_SUBJECT_IDS,
		COLUMN_NAME_CONTEXT_SENTENCES,
		COLUMN_NAME_PARTS_OF_SPEECH,
		COLUMN_NAME_PRONOUNCIATION_AUDIOS,
		COLUMN_NAME_NULLABLE
	};
}