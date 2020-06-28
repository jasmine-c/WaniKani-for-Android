package tr.xip.wanikani.models.v2.subjects;

import java.io.Serializable;

public class PronunciationAudioMetadata implements Serializable {
    private String gender;
    private int source_id;
    private String pronunciation;
    private int voice_actor_id;
    private String voice_actor_name;
    private String voice_description;

    public PronunciationAudioMetadata(String gender, int source_id, String pronunciation, int voice_actor_id, String voice_actor_name, String voice_description) {
        this.gender = gender;
        this.source_id = source_id;
        this.pronunciation = pronunciation;
        this.voice_actor_id = voice_actor_id;
        this.voice_actor_name = voice_actor_name;
        this.voice_description = voice_description;
    }
}
