package tr.xip.wanikani.models.v2.user;

import java.io.Serializable;

public class Preferences implements Serializable {
    private int default_voice_actor_id;
    private boolean lessons_autoplay_audio;
    private int lessons_batch_size;
    private String lessons_presentation_order;
    private boolean reviews_autoplay_audio;
    private boolean reviews_display_srs_indicator;

    public Preferences(int default_voice_actor_id, boolean lessons_autoplay_audio, int lessons_batch_size, String lessons_presentation_order, boolean reviews_autoplay_audio, boolean reviews_display_srs_indicator) {
        this.default_voice_actor_id = default_voice_actor_id;
        this.lessons_autoplay_audio = lessons_autoplay_audio;
        this.lessons_batch_size = lessons_batch_size;
        this.lessons_presentation_order = lessons_presentation_order;
        this.reviews_autoplay_audio = reviews_autoplay_audio;
        this.reviews_display_srs_indicator = reviews_display_srs_indicator;
    }
}
