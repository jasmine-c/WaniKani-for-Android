package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class Lesson implements Serializable {
    public DateTime available_at;
    public ArrayList<Integer> subject_ids;

    public Lesson(DateTime available_at, ArrayList<Integer> subject_ids) {
        this.available_at = available_at;
        this.subject_ids = subject_ids;
    }
}
