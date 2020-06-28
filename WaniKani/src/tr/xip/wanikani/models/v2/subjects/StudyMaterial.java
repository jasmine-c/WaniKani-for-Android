package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class StudyMaterial extends StudyMaterialCreate implements Serializable {
    private DateTime created_at;
    private boolean hidden;
    private String subject_type;

    public StudyMaterial(
            DateTime created_at, boolean hidden, String meaning_note,
            ArrayList<String> meaning_synonyms, String reading_note, int subject_id,
            String subject_type) {
        super(meaning_note, meaning_synonyms, reading_note, subject_id);
        this.created_at = created_at;
        this.hidden = hidden;
        this.subject_type = subject_type;
    }
}
