package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

import java.util.ArrayList;

import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.Storable;
import tr.xip.wanikani.models.v2.Collection;
import tr.xip.wanikani.models.v2.Pages;

public class SubjectCollection extends Collection<Subject> implements Storable {
    public SubjectCollection(String object, String url, Pages pages, int total_count, DateTime data_updated_at, ArrayList<Subject> data) {
        super(object, url, pages, total_count, data_updated_at, data);
    }

    @Override
    public void save() {

    }
}
