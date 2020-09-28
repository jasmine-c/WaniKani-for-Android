package tr.xip.wanikani.models.v2.subjects;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

import tr.xip.wanikani.models.Storable;
import tr.xip.wanikani.models.v2.Resource;

public abstract class SubjectData implements Serializable, Storable {
    public ArrayList<AuxiliaryMeaning> auxiliary_meanings;
    public String characters;
    public DateTime created_at;
    public String document_url;
    public DateTime hidden_at;
    public int lesson_position;
    public int level;
    public String meaning_mnemonic;
    public ArrayList<Meaning> meanings;
    public String slug;
    public int spaced_repetition_system_id;

    public SubjectData(
            ArrayList<AuxiliaryMeaning> auxiliary_meanings, String characters, DateTime created_at,
            String document_url, DateTime hidden_at, int lesson_position, int level,
            String meaning_mnemonic, ArrayList<Meaning> meanings, String slug,
            int spaced_repetition_system_id) {
        this.auxiliary_meanings = auxiliary_meanings;
        this.characters = characters;
        this.created_at = created_at;
        this.document_url = document_url;
        this.hidden_at = hidden_at;
        this.lesson_position = lesson_position;
        this.level = level;
        this.meaning_mnemonic = meaning_mnemonic;
        this.meanings = meanings;
        this.slug = slug;
        this.spaced_repetition_system_id = spaced_repetition_system_id;
    }

    @Override
    public void save() {
        throw new NotImplementedException("save not implemented");
    }
}


