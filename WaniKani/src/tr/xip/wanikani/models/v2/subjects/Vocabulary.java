package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class Vocabulary extends Subject implements Serializable {
    private ArrayList<Integer> component_subject_ids;
    private ArrayList<ContextSentence> context_sentences;
    private String meaning_mnemonic;
    private ArrayList<String> parts_of_speech;
    private ArrayList<PronunciationAudio> pronunciation_audios;
    private ArrayList<Reading> readings;
    private String reading_mnemonic;

    public Vocabulary(
            ArrayList<AuxiliaryMeaning> auxiliary_meanings, String characters, DateTime created_at,
            String document_url, DateTime hidden_at, int lesson_position, int level,
            String meaning_mnemonic, ArrayList<Meaning> meanings, String slug,
            int spaced_repetition_system_id, ArrayList<Integer> component_subject_ids,
            ArrayList<ContextSentence> context_sentences, String meaning_mnemonic1,
            ArrayList<String> parts_of_speech, ArrayList<PronunciationAudio> pronunciation_audios,
            ArrayList<Reading> readings, String reading_mnemonic)
    {
        super(auxiliary_meanings, characters, created_at, document_url, hidden_at, lesson_position,
                level, meaning_mnemonic, meanings, slug, spaced_repetition_system_id);
        this.component_subject_ids = component_subject_ids;
        this.context_sentences = context_sentences;
        this.meaning_mnemonic = meaning_mnemonic1;
        this.parts_of_speech = parts_of_speech;
        this.pronunciation_audios = pronunciation_audios;
        this.readings = readings;
        this.reading_mnemonic = reading_mnemonic;
    }
}

