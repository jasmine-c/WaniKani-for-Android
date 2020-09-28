package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class KanjiData extends SubjectData implements Serializable {
    public ArrayList<Integer> amalgamation_subject_ids;
    public ArrayList<Integer> component_subject_ids;
    public String meaning_hint;
    public String reading_hint;
    public String reading_mnemonic;
    public ArrayList<KanjiReading> readings;
    public ArrayList<Integer> visually_similar_subject_ids;

    public KanjiData(
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

