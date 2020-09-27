package tr.xip.wanikani.models.v2.reviews;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;

import java.io.Serializable;

import tr.xip.wanikani.models.Storable;

public class AssignmentData implements Serializable, Storable {
    public int subject_id;
    public String subject_type;
    public int srs_stage;
    public DateTime created_at;
    public DateTime unlocked_at;
    public DateTime started_at;
    public DateTime passed_at;
    public DateTime burned_at;
    public DateTime available_at;
    public DateTime resurrected_at;
    public boolean hidden;

    public AssignmentData(int subject_id, String subject_type, int srs_stage, DateTime created_at,
                          DateTime unlocked_at, DateTime started_at, DateTime passed_at, DateTime burned_at,
                          DateTime available_at, DateTime resurrected_at, boolean hidden)
    {
        this.subject_id = subject_id;
        this.subject_type = subject_type;
        this.srs_stage = srs_stage;
        this.created_at = created_at;
        this.unlocked_at = unlocked_at;
        this.started_at = started_at;
        this.passed_at = passed_at;
        this.burned_at = burned_at;
        this.available_at = available_at;
        this.resurrected_at = resurrected_at;
        this.hidden = hidden;
    }

    @Override
    public void save() {
        throw new NotImplementedException("save not implemented");
    }
}
