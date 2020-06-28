package tr.xip.wanikani.models.v2.user;

import org.joda.time.DateTime;

import java.io.Serializable;

public class User implements Serializable {
    private DateTime current_vacation_started_at;
    private int level;
    private Preferences preferences;
    private String profile_url;
    private DateTime started_at;
    private Subscription subscription;
    private String username;

    public User(
            DateTime current_vacation_started_at, int level, Preferences preferences,
            String profile_url, DateTime started_at, Subscription subscription, String username) {
        this.current_vacation_started_at = current_vacation_started_at;
        this.level = level;
        this.preferences = preferences;
        this.profile_url = profile_url;
        this.started_at = started_at;
        this.subscription = subscription;
        this.username = username;
    }
}
