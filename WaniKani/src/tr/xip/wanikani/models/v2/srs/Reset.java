package tr.xip.wanikani.models.v2.srs;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Reset implements Serializable {
    private DateTime confirmed_at;
    private DateTime created_at;
    private int original_level;
    private int target_level;

    public Reset(DateTime confirmed_at, DateTime created_at, int original_level, int target_level) {
        this.confirmed_at = confirmed_at;
        this.created_at = created_at;
        this.original_level = original_level;
        this.target_level = target_level;
    }
}
