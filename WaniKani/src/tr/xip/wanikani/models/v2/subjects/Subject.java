package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Subject implements Serializable {
    private ArrayList<AuxiliaryMeaning> auxiliary_meanings;
    private String characters;
    private DateTime created_at;
    private String document_url;
    private DateTime hidden_at;
    private int lesson_position;
    private int level;
    private String meaning_mnemonic;
    private ArrayList<Meaning> meanings;
    private String slug;
    private int spaced_repetition_system_id;

    public Subject(
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
}


