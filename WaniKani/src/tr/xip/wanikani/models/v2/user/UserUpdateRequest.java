package tr.xip.wanikani.models.v2.user;

import java.io.Serializable;

public class UserUpdateRequest implements Serializable {
    private UpdateRequest user;

    public UserUpdateRequest(UpdateRequest user) {
        this.user = user;
    }
}

