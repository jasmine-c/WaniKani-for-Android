package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

public class Radical extends Subject {
    public Radical(int id, String object, String url, DateTime data_updated_at, RadicalData data) {
        super(id, object, url, data_updated_at, data);
    }
}
