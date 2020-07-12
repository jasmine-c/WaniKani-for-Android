package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.util.ArrayList;

import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.Storable;
import tr.xip.wanikani.models.v2.Collection;
import tr.xip.wanikani.models.v2.Pages;

public class AssignmentCollection extends Collection<Assignment> implements Storable {
    public AssignmentCollection(String object, String url, Pages pages, int total_count, DateTime data_updated_at, ArrayList<Assignment> data) {
        super(object, url, pages, total_count, data_updated_at, data);
    }

    @Override
    public void save() {
        DatabaseManager.saveAssignments(this.data);
    }
}
