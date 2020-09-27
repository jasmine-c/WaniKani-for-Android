package tr.xip.wanikani.models.v2.subjects;

import org.apache.commons.lang3.NotImplementedException;

import java.io.Serializable;

import tr.xip.wanikani.models.Storable;

public class VoiceActor implements Serializable, Storable {
    private String description;
    private String gender;
    private String name;

    public VoiceActor(String description, String gender, String name) {
        this.description = description;
        this.gender = gender;
        this.name = name;
    }

    @Override
    public void save() {
        throw new NotImplementedException("save not implemented");
    }
}
