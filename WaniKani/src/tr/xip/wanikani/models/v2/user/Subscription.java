package tr.xip.wanikani.models.v2.user;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Subscription implements Serializable {
    private boolean active;
    private int max_level_granted;
    private DateTime period_ends_at;
    private String type;

    public Subscription(boolean active, int max_level_granted, DateTime period_ends_at, String type) {
        this.active = active;
        this.max_level_granted = max_level_granted;
        this.period_ends_at = period_ends_at;
        this.type = type;
    }
}
