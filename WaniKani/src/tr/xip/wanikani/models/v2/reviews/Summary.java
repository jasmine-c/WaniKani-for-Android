package tr.xip.wanikani.models.v2.reviews;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.v2.BaseResponse;

public class Summary extends BaseResponse<SummaryData> {

    public Summary(String object, String url, DateTime data_updated_at, SummaryData data) {
        super(object, url, data_updated_at, data);
    }

    @Override
    public void save() {
        DatabaseManager.saveSummary(this);
    }

    public int getAvailableLessonsCount(DateTime until)
    {
        int count = 0;

        for (Lesson lesson :
                data.lessons) {
            if (!lesson.available_at.isAfter(until))
                count += lesson.subject_ids.size();
        }

        return count;
    }

    public int getAvailableReviewsCount(DateTime until)
    {
        int count = 0;

        for (Lesson lesson :
                data.reviews) {
            if (!lesson.available_at.isAfter(until))
                count += lesson.subject_ids.size();
        }

        return count;
    }

    public long getNextReviewDateInMillis()
    {
        DateTime next = data.reviews.get(0).available_at;
        boolean hasContent = data.reviews.get(0).subject_ids.size() > 0;

        for (Lesson lesson :
                data.reviews) {
            if (lesson.available_at.isBefore(next) && lesson.subject_ids.size() > 0)
            {
                next = lesson.available_at;
                hasContent = true;
            }
        }

        if (!hasContent)
            return -1;

        return next.getMillis();
    }
}

