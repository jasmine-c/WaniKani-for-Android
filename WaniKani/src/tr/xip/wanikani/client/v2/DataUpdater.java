package tr.xip.wanikani.client.v2;

import org.joda.time.DateTime;

import java.io.Serializable;

import rx.Completable;
import rx.Notification;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.Storable;
import tr.xip.wanikani.models.v2.BaseResponse;

public class DataUpdater<R, T extends BaseResponse<R> & Storable> {
    private final Func1<Filter, Observable<T>> getter;
    private final String queryName;
    private DateTime updatedAt;

    public DataUpdater(Func1<Filter, Observable<T>> getter, String queryName) {

        this.getter = getter;
        this.queryName = queryName;
    }

    public Completable updateData() {
        DateTime lastUpdated = DatabaseManager.getLastUpdated(queryName);

        return getter.call(new Filter().updated_after(lastUpdated))
                .doOnEach(new Action1<Notification<? super T>>() {
                    @Override
                    public void call(rx.Notification<? super T> notification) {
                        if (notification.getKind() == rx.Notification.Kind.OnNext) {
                            T value = (T) notification.getValue();
                            value.save();
                            updatedAt = value.data_updated_at;
                        }
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        if (updatedAt != null)
                            DatabaseManager.saveLastUpdated(queryName, updatedAt);
                    }
                })
                .toCompletable();
    }
}
