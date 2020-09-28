package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

public class CriticalSubject extends Subject {
	public final int percentage;

	public CriticalSubject(int id, String object, String url, DateTime data_updated_at, SubjectData data, int percentage) {
		super(id, object, url, data_updated_at, data);
		this.percentage = percentage;
	}
}
