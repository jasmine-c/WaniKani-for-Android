package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import tr.xip.wanikani.models.v2.Resource;

public class Assignment extends Resource<AssignmentData> {
    public Assignment(int id, String object, String url, DateTime data_updated_at, AssignmentData data) {
        super(id, object, url, data_updated_at, data);
    }
}
