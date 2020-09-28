package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class VocabularyData extends SubjectData implements Serializable {
	public ArrayList<Integer> component_subject_ids;
	public ArrayList<ContextSentence> context_sentences;
	public ArrayList<String> parts_of_speech;
	public ArrayList<PronunciationAudio> pronunciation_audios;
	public ArrayList<Reading> readings;
	public String reading_mnemonic;

	public VocabularyData(
		ArrayList<AuxiliaryMeaning> auxiliary_meanings, String characters, DateTime created_at,
		String document_url, DateTime hidden_at, int lesson_position, int level,
		String meaning_mnemonic, ArrayList<Meaning> meanings, String slug,
		int spaced_repetition_system_id, ArrayList<Integer> component_subject_ids,
		ArrayList<ContextSentence> context_sentences,
		ArrayList<String> parts_of_speech, ArrayList<PronunciationAudio> pronunciation_audios,
		ArrayList<Reading> readings, String reading_mnemonic) {
		super(auxiliary_meanings, characters, created_at, document_url, hidden_at, lesson_position,
			level, meaning_mnemonic, meanings, slug, spaced_repetition_system_id);
		this.component_subject_ids = component_subject_ids;
		this.context_sentences = context_sentences;
		this.parts_of_speech = parts_of_speech;
		this.pronunciation_audios = pronunciation_audios;
		this.readings = readings;
		this.reading_mnemonic = reading_mnemonic;
	}
}

