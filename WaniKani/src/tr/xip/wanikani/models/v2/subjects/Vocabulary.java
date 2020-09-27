package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

public class Vocabulary extends Subject {
    public Vocabulary(int id, String object, String url, DateTime data_updated_at, VocabularyData data) {
        super(id, object, url, data_updated_at, data);
    }
}
