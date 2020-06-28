package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class ContextSentence implements Serializable {
   private String en;
   private String ja;

    public ContextSentence(String en, String ja) {
        this.en = en;
        this.ja = ja;
    }
}
