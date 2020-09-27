package tr.xip.wanikani.models.v2.srs;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;

import java.io.Serializable;

import tr.xip.wanikani.models.Storable;

public class LevelProgression implements Serializable, Storable {
    private DateTime abandoned_at;
    private DateTime completed_at;
    private DateTime created_at;
    private int level;
    private DateTime passed_at;
    private DateTime started_at;
    private DateTime unlocked_at;

    public LevelProgression(
            DateTime abandoned_at, DateTime completed_at, DateTime created_at, int level,
            DateTime passed_at, DateTime started_at, DateTime unlocked_at) {
        this.abandoned_at = abandoned_at;
        this.completed_at = completed_at;
        this.created_at = created_at;
        this.level = level;
        this.passed_at = passed_at;
        this.started_at = started_at;
        this.unlocked_at = unlocked_at;
    }

    @Override
    public void save() {
        throw new NotImplementedException("save not implemented");
    }
}
