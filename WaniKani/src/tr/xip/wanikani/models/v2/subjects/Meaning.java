package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class Meaning implements Serializable {
    private String meaning;
    private boolean primary;
    private boolean accepted_answer;

    public Meaning(String meaning, boolean primary, boolean accepted_answer) {
        this.meaning = meaning;
        this.primary = primary;
        this.accepted_answer = accepted_answer;
    }
}
