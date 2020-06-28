package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;

import tr.xip.wanikani.models.v2.Resource;

public class ReviewCreateResponse extends Resource<Review> implements Serializable {
    private ResourceUpdates resources_updated;

    public ReviewCreateResponse(
            int id, String object, String url, DateTime data_updated_at, Review data,
            ResourceUpdates resources_updated) {
        super(id, object, url, data_updated_at, data);
        this.resources_updated = resources_updated;
    }
}

