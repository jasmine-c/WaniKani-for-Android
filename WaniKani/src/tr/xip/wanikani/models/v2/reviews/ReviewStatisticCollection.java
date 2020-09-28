package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.util.ArrayList;

import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.v2.Collection;
import tr.xip.wanikani.models.v2.Pages;

public class ReviewStatisticCollection extends Collection<ReviewStatistic> {
	public ReviewStatisticCollection(String object, String url, Pages pages, int total_count, DateTime data_updated_at, ArrayList<ReviewStatistic> data) {
		super(object, url, pages, total_count, data_updated_at, data);
	}

	@Override
	public void save() {
		DatabaseManager.saveReviewStatistics(this.data);
	}
}
