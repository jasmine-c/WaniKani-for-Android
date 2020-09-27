package tr.xip.wanikani.client.task.callback;

import android.arch.core.util.Function;
import android.net.Uri;

import retrofit2.Call;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import tr.xip.wanikani.client.v2.Filter;
import tr.xip.wanikani.client.v2.WaniKaniApiV2;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.v2.AssignmentsTable;
import tr.xip.wanikani.models.SRSDistribution;
import tr.xip.wanikani.models.v2.reviews.AssignmentCollection;

public class AssignmentsCallback extends ThroughDbCallbackV2<AssignmentCollection> {
    private Function<SRSDistribution, Void> callback;

    public AssignmentsCallback(Function<SRSDistribution, Void> callback) {
        this.callback = callback;
    }

    public void enqueueQuery()
    {
        WaniKaniApiV2.getAssignments(new Filter()
                .updated_after(DatabaseManager.getLastUpdated(AssignmentsTable.TABLE_NAME)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<AssignmentCollection>() {
                            @Override
                            public void call(AssignmentCollection assignmentCollection) {
                                onResponse(assignmentCollection);
                            }
                        },

                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                onFailure(throwable);
                            }
                        }
                );
    }

    @Override
    public void onResponse(Call<AssignmentCollection> call, Response<AssignmentCollection> response) {
        super.onResponse(call, response);

        if (response.isSuccessful() && response.body() != null) {
            if (response.body().pages.next_url == null)
            {
                DatabaseManager.saveLastUpdated(AssignmentsTable.TABLE_NAME, response.body().data_updated_at);

                SRSDistribution distribution = DatabaseManager.getSrsDistribution();
                if (distribution != null) {
                    callback.apply(distribution);
                }
            }
            else
            {
                Uri url = Uri.parse(response.body().pages.next_url);
                WaniKaniApiV2.getAssignments(new Filter()
                        .page_after_id(Integer.parseInt(url.getQueryParameter("page_after_id")))
                        .updated_after(DatabaseManager.getLastUpdated(AssignmentsTable.TABLE_NAME)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Action1<AssignmentCollection>() {
                                    @Override
                                    public void call(AssignmentCollection assignmentCollection) {
                                        onResponse(assignmentCollection);
                                    }
                                },

                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        onFailure(throwable);
                                    }
                                }
                        );
            }
        } else {
            onFailure(call, null);
        }
    }

    private void onResponse(AssignmentCollection assignmentCollection)
    {
        if (assignmentCollection.pages.next_url == null)
        {
            DatabaseManager.saveLastUpdated(AssignmentsTable.TABLE_NAME, assignmentCollection.data_updated_at);

            SRSDistribution distribution = DatabaseManager.getSrsDistribution();
            if (distribution != null) {
                callback.apply(distribution);
            }
        }
        else
        {
            Uri url = Uri.parse(assignmentCollection.pages.next_url);

            assignmentCollection.save();

            WaniKaniApiV2.getAssignments(new Filter()
                    .page_after_id(Integer.parseInt(url.getQueryParameter("page_after_id")))
                    .updated_after(DatabaseManager.getLastUpdated(AssignmentsTable.TABLE_NAME)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Action1<AssignmentCollection>() {
                                @Override
                                public void call(AssignmentCollection assignmentCollection) {
                                    onResponse(assignmentCollection);
                                }
                            },

                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    onFailure(throwable);
                                }
                            }
                    );
        }
    }

    private void onFailure(Throwable t)
    {
        SRSDistribution distribution = DatabaseManager.getSrsDistribution();
        if (distribution != null) {
            callback.apply(distribution);
        }
    }

    @Override
    public void onFailure(Call<AssignmentCollection> call, Throwable t) {
        super.onFailure(call, t);

        SRSDistribution distribution = DatabaseManager.getSrsDistribution();
        if (distribution != null) {
            callback.apply(distribution);
        }
    }
}
