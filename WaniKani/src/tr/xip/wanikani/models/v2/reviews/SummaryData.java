package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class SummaryData implements Serializable {
    public ArrayList<Lesson> lessons;
    public DateTime next_reviews_at;
    public ArrayList<Lesson> reviews;

    public SummaryData(
            ArrayList<Lesson> lessons, DateTime next_reviews_at, ArrayList<Lesson> reviews) {
        this.lessons = lessons;
        this.next_reviews_at = next_reviews_at;
        this.reviews = reviews;
    }
}
