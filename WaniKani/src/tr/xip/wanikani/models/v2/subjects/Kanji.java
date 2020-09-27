package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

public class Kanji extends Subject {
    public Kanji(int id, String object, String url, DateTime data_updated_at, KanjiData data) {
        super(id, object, url, data_updated_at, data);
    }
}
