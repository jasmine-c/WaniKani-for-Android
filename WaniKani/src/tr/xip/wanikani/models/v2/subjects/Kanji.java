package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class Kanji extends Subject implements Serializable {
    private ArrayList<Integer> amalgamation_subject_ids;
    private ArrayList<Integer> component_subject_ids;
    private String meaning_hint;
    private String reading_hint;
    private String reading_mnemonic;
    private ArrayList<KanjiReading> readings;
    private ArrayList<Integer> visually_similar_subject_ids;

    public Kanji(
            ArrayList<AuxiliaryMeaning> auxiliary_meanings, String characters, DateTime created_at,
            String document_url, DateTime hidden_at, int lesson_position, int level,
            String meaning_mnemonic, ArrayList<Meaning> meanings, String slug,
            int spaced_repetition_system_id, ArrayList<Integer> amalgamation_subject_ids,
            ArrayList<Integer> component_subject_ids, String meaning_hint, String reading_hint,
            String reading_mnemonic, ArrayList<KanjiReading> readings, ArrayList<Integer> visually_similar_subject_ids)
    {
        super(auxiliary_meanings, characters, created_at, document_url, hidden_at, lesson_position,
                level, meaning_mnemonic, meanings, slug, spaced_repetition_system_id);
        this.amalgamation_subject_ids = amalgamation_subject_ids;
        this.component_subject_ids = component_subject_ids;
        this.meaning_hint = meaning_hint;
        this.reading_hint = reading_hint;
        this.reading_mnemonic = reading_mnemonic;
        this.readings = readings;
        this.visually_similar_subject_ids = visually_similar_subject_ids;
    }
}

