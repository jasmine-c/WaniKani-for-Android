package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;
import java.util.ArrayList;

public class StudyMaterialCreate implements Serializable {
    private String meaning_note;
    private ArrayList<String> meaning_synonyms;
    private String reading_note;
    private int subject_id;

    public StudyMaterialCreate(
            String meaning_note, ArrayList<String> meaning_synonyms, String reading_note,
            int subject_id) {
        this.meaning_note = meaning_note;
        this.meaning_synonyms = meaning_synonyms;
        this.reading_note = reading_note;
        this.subject_id = subject_id;
    }
}