package tr.xip.wanikani.models.v2.user;

import java.io.Serializable;

public class UpdateRequest implements Serializable {
    private Preferences preferences;

    public UpdateRequest(Preferences preferences) {
        this.preferences = preferences;
    }
}
