package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Assignment implements Serializable
{
    private int subject_id;
    private String subject_type;
    private int srs_stage;
    private DateTime created_at;
    private DateTime unlocked_at;
    private DateTime started_at;
    private DateTime passed_at;
    private DateTime burned_at;
    private DateTime available_at;
    private DateTime resurrected_at;

    public Assignment(int subject_id, String subject_type, int srs_stage, DateTime created_at,
                      DateTime unlocked_at, DateTime started_at, DateTime passed_at, DateTime burned_at,
                      DateTime available_at, DateTime resurrected_at)
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
    }
}
