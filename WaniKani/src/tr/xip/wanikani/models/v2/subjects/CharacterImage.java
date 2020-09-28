package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class CharacterImage implements Serializable {
    public String url;
    public String content_type;
    public Object metadata;

    public CharacterImage(String url, String content_type, Object metadata) {
        this.url = url;
        this.content_type = content_type;
        this.metadata = metadata;
    }
}
