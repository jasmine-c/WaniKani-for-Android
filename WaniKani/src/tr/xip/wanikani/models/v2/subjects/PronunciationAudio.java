package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class PronunciationAudio implements Serializable {
    private String url;
    private String content_type;
    private PronunciationAudioMetadata metadata;

    public PronunciationAudio(String url, String content_type, PronunciationAudioMetadata metadata) {
        this.url = url;
        this.content_type = content_type;
        this.metadata = metadata;
    }
}

