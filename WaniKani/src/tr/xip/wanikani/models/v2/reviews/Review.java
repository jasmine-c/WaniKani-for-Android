package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Review extends ReviewCreate implements Serializable {

    private int ending_srs_stage;
    private int spaced_repetition_system_id;
    private int starting_srs_stage;

    public Review(
            int assignment_id, DateTime created_at, int ending_srs_stage,
            int incorrect_meaning_answers, int incorrect_reading_answers,
            int spaced_repetition_system_id, int starting_srs_stage, int subject_id) {
        super(assignment_id, created_at, incorrect_meaning_answers, incorrect_reading_answers, subject_id);
        this.ending_srs_stage = ending_srs_stage;
        this.spaced_repetition_system_id = spaced_repetition_system_id;
        this.starting_srs_stage = starting_srs_stage;
    }
}
