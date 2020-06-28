package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class Reading implements Serializable {
    private String reading;
    private boolean primary;
    private boolean accepted_answer;

    public Reading(String reading, boolean primary, boolean accepted_answer) {
        this.reading = reading;
        this.primary = primary;
        this.accepted_answer = accepted_answer;
    }
}
