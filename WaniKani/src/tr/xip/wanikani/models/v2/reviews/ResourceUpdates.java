package tr.xip.wanikani.models.v2.reviews;

import java.io.Serializable;

import tr.xip.wanikani.models.v2.Resource;

public class ResourceUpdates implements Serializable {
    private Resource<Assignment> assignment;
    private Resource<ReviewStatistic> review_statistic;

    public ResourceUpdates(Resource<Assignment> assignment, Resource<ReviewStatistic> review_statistic) {
        this.assignment = assignment;
        this.review_statistic = review_statistic;
    }
}
