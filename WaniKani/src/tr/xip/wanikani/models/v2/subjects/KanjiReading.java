package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class KanjiReading extends Reading implements Serializable {
    private String type;

    public KanjiReading(String reading, boolean primary, boolean accepted_answer, String type) {
        super(reading, primary, accepted_answer);
        this.type = type;
    }
}
