package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;

public class ReviewCreate implements Serializable {
    private int assignment_id;
    private DateTime created_at;
    private int incorrect_meaning_answers;
    private int incorrect_reading_answers;
    private int subject_id;

    public ReviewCreate(
            int assignment_id, DateTime created_at, int incorrect_meaning_answers,
            int incorrect_reading_answers, int subject_id) {
        this.assignment_id = assignment_id;
        this.created_at = created_at;
        this.incorrect_meaning_answers = incorrect_meaning_answers;
        this.incorrect_reading_answers = incorrect_reading_answers;
        this.subject_id = subject_id;
    }
}
