package tr.xip.wanikani.models.v2.reviews;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;

import java.io.Serializable;

import tr.xip.wanikani.models.Storable;

public class ReviewStatisticData implements Serializable, Storable {
    public DateTime created_at;
    public boolean hidden;
    public int meaning_correct;
    public int meaning_current_streak;
    public int meaning_incorrect;
    public int meaning_max_streak;
    public int percentage_correct;
    public int reading_correct;
    public int reading_current_streak;
    public int reading_incorrect;
    public int reading_max_streak;
    public int subject_id;
    public String subject_type;

    public ReviewStatisticData(
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

    @Override
    public void save() {
        throw new NotImplementedException("save not implemented");
    }
}
