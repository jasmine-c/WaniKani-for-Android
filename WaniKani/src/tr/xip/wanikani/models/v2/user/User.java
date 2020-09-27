package tr.xip.wanikani.models.v2.user;

import org.joda.time.DateTime;

import java.io.Serializable;

import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.Storable;

public class User implements Serializable, Storable {
    public DateTime current_vacation_started_at;
    public int level;
    public Preferences preferences;
    public String profile_url;
    public DateTime started_at;
    public Subscription subscription;
    public String username;

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

    @Override
    public void save() {
        DatabaseManager.saveUser(
                new tr.xip.wanikani.models.User(username, null, level,
                        "", "", "", "", 0, 0,
                        started_at.getMillis() / 1000,
                        current_vacation_started_at == null ?
                                0 : current_vacation_started_at.getMillis() / 1000));
    }
}
