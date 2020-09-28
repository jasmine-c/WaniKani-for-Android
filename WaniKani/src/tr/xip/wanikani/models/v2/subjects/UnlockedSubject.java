package tr.xip.wanikani.models.v2.subjects;

import org.joda.time.DateTime;

public class UnlockedSubject extends Subject {
	public final DateTime unlocked_at;

	public UnlockedSubject(int id, String object, String url, DateTime data_updated_at, SubjectData data, DateTime unlocked_at) {
		super(id, object, url, data_updated_at, data);
		this.unlocked_at = unlocked_at;
	}
}
