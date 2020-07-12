package tr.xip.wanikani.app.fragment.card;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.fragment.DashboardFragment;
import tr.xip.wanikani.client.WaniKaniApi;
import tr.xip.wanikani.client.task.callback.ThroughDbCallback;
import tr.xip.wanikani.client.task.callback.ThroughDbCallbackV2;
import tr.xip.wanikani.client.v2.WaniKaniApiV2;
import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.Request;
import tr.xip.wanikani.models.StudyQueue;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.models.v2.reviews.Summary;
import tr.xip.wanikani.utils.Utils;
import tr.xip.wanikani.widget.RelativeTimeTextView;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class ReviewsCard extends Fragment {

    Context context;
    Utils utils;

    View rootView;

    ReviewsCardListener mListener;

    RelativeTimeTextView mNextReview;
    TextView mNextHour;
    TextView mNextDay;

    LinearLayout mMoreReviewsHolder;
    TextView mMoreReviews;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WaniKaniApiV2.getSummary().enqueue(new ThroughDbCallbackV2<Summary>() {
                @Override
                public void onResponse(Call<Summary> call, Response<Summary> response) {
                    super.onResponse(call, response);

                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            User user = DatabaseManager.getUser();
                            if (user == null) {
                                user = WaniKaniApi.getUser().execute().body().requested_information;
                                user.save();
                            }

                            displayData(DatabaseManager.getUser(), response.body());
                        } catch (Exception e) {
                            onFailure(call, e);
                        }
                    } else {
                        onFailure(call, null);
                    }
                }

                @Override
                public void onFailure(Call<Summary> call, Throwable t) {
                    super.onFailure(call, t);

                    User user = DatabaseManager.getUser();
                    Summary summary = DatabaseManager.getSummary();

                    if (user != null && summary != null) {
                        displayData(user, summary);
                    } else {
                        mListener.onReviewsCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
                    }
                }
            });
        }
    };

    public void setListener(ReviewsCardListener listener, Context context) {
        mListener = listener;
        LocalBroadcastManager.getInstance(context).registerReceiver(mDoLoad,
                new IntentFilter(BroadcastIntents.SYNC()));
    }

    @Override
    public void onCreate(Bundle state) {
        utils = new Utils(getActivity());
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_reviews, null);

        context = getActivity();

        mNextReview = (RelativeTimeTextView) rootView.findViewById(R.id.card_reviews_next_review);
        mNextHour = (TextView) rootView.findViewById(R.id.card_reviews_next_hour);
        mNextDay = (TextView) rootView.findViewById(R.id.card_reviews_next_day);

        mMoreReviewsHolder = (LinearLayout) rootView.findViewById(R.id.card_reviews_more_reviews_holder);
        mMoreReviews = (TextView) rootView.findViewById(R.id.card_reviews_more_reviews);

        return rootView;
    }

    @SuppressLint("SetTextI18n,SimpleDateFormat")
    private void displayData(User user, Summary summary) {
        if (user == null && summary == null)
        {
            mListener.onReviewsCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
            return;
        }

        if (user.isVacationModeActive())
        {
            mListener.onReviewsCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
            return;
        }

        DateTime now = DateTime.now();

        mNextHour.setText(summary.getNextHourReviewsCount() + "");
        mNextDay.setText(summary.getNextDayReviewsCount() + "");

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM HH:mm");

        if (summary.getAvailableReviewsCount(now) != 0) {
            mNextReview.setText(R.string.card_content_reviews_available_now);
        } else {
            long nextReviewMillis = summary.getNextReviewDateInMillis();

            if (nextReviewMillis == -1L)
                mNextReview.setText("Not Available");
            else if (PrefManager.isUseSpecificDates()) {
                mNextReview.setText(sdf.format(summary.getNextReviewDateInMillis()));
            } else {
                mNextReview.setReferenceTime(summary.getNextReviewDateInMillis());
            }
        }

        mListener.onReviewsCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
    }

    public interface ReviewsCardListener {
        public void onReviewsCardSyncFinishedListener(String result);
    }
}
