package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;

public class ReviewStatistic implements Serializable {
    private DateTime created_at;
    private boolean hidden;
    private int meaning_correct;
    private int meaning_current_streak;
    private int meaning_incorrect;
    private int meaning_max_streak;
    private int percentage_correct;
    private int reading_correct;
    private int reading_current_streak;
    private int reading_incorrect;
    private int reading_max_streak;
    private int subject_id;
    private String subject_type;

    public ReviewStatistic(
            DateTime created_at, boolean hidden, int meaning_correct, int meaning_current_streak,
            int meaning_incorrect, int meaning_max_streak, int percentage_correct,
            int reading_correct, int reading_current_streak, int reading_incorrect,
            int reading_max_streak, int subject_id, String subject_type) {
        this.created_at = created_at;
        this.hidden = hidden;
        this.meaning_correct = meaning_correct;
        this.meaning_current_streak = meaning_current_streak;
        this.meaning_incorrect = meaning_incorrect;
        this.meaning_max_streak = meaning_max_streak;
        this.percentage_correct = percentage_correct;
        this.reading_correct = reading_correct;
        this.reading_current_streak = reading_current_streak;
        this.reading_incorrect = reading_incorrect;
        this.reading_max_streak = reading_max_streak;
        this.subject_id = subject_id;
        this.subject_type = subject_type;
    }
}
