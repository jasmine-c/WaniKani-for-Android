package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class Summary implements Serializable {
    private ArrayList<Lesson> lessons;
    private DateTime next_reviews_at;
    private ArrayList<Lesson> reviews;

    public Summary(
            ArrayList<Lesson> lessons, DateTime next_reviews_at, ArrayList<Lesson> reviews) {
        this.lessons = lessons;
        this.next_reviews_at = next_reviews_at;
        this.reviews = reviews;
    }
}

