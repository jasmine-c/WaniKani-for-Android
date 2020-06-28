package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class VoiceActor implements Serializable {
    private String description;
    private String gender;
    private String name;

    public VoiceActor(String description, String gender, String name) {
        this.description = description;
        this.gender = gender;
        this.name = name;
    }
}
