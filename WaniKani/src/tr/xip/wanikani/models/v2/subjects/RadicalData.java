package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class RadicalData extends SubjectData {
    public ArrayList<Integer> amalgamation_subject_ids;
    public ArrayList<CharacterImage> character_images;

    public RadicalData(
            ArrayList<AuxiliaryMeaning> auxiliary_meanings, String characters, DateTime created_at,
            String document_url, DateTime hidden_at, int lesson_position, int level,
            String meaning_mnemonic, ArrayList<Meaning> meanings, String slug,
            int spaced_repetition_system_id, ArrayList<Integer> amalgamation_subject_ids,
            ArrayList<CharacterImage> character_images) {
        super(auxiliary_meanings, characters, created_at, document_url, hidden_at, lesson_position,
                level, meaning_mnemonic, meanings, slug, spaced_repetition_system_id);
        this.amalgamation_subject_ids = amalgamation_subject_ids;
        this.character_images = character_images;
    }
}

