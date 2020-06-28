package tr.xip.wanikani.models.v2.srs;

import java.io.Serializable;

public class Stage implements Serializable {
    private Integer interval;
    private String interval_unit;
    private int position;

    public Stage(Integer interval, String interval_unit, int position) {
        this.interval = interval;
        this.interval_unit = interval_unit;
        this.position = position;
    }
}
