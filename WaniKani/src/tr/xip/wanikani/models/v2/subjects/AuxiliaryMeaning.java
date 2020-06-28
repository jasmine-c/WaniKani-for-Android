package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class AuxiliaryMeaning implements Serializable {
    private String meaning;
    private String type;

    public AuxiliaryMeaning(String meaning, String type) {
        this.meaning = meaning;
        this.type = type;
    }
}
