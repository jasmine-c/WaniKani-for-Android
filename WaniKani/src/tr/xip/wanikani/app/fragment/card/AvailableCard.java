package tr.xip.wanikani.app.fragment.card;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.Browser;
import tr.xip.wanikani.app.activity.WebReviewActivity;
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
import tr.xip.wanikani.models.v2.BaseResponse;
import tr.xip.wanikani.models.v2.reviews.Summary;
import tr.xip.wanikani.utils.Utils;

/**
 * Created by xihsa_000 on 3/13/14.
 */
public class AvailableCard extends Fragment {

    public static final int BROWSER_REQUEST = 1;

    View rootView;
    Context context;

    Utils utils;

    ImageView mLessonsGo;
    ImageView mReviewsGo;
    TextView mLessonsAvailable;
    TextView mReviewsAvailable;
    LinearLayout mCard;

    AvailableCardListener mListener;

    private BroadcastReceiver mDoLoad = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WaniKaniApiV2.getSummary().enqueue(new ThroughDbCallbackV2<Summary>() {
                @Override
                public void onResponse(Call<Summary> call, Response<Summary> response) {
                    super.onResponse(call, response);

                    if (response.isSuccessful() && response.body() != null) {
                        displayData(DatabaseManager.getUser(), response.body());
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
                        mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_FAILED);
                    }
                }
            });
        }
    };

    public void setListener(AvailableCardListener listener, Context context) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.card_available, parent, false);

        context = getActivity();

        mLessonsGo = (ImageView) rootView.findViewById(R.id.card_available_lessons_go);
        mReviewsGo = (ImageView) rootView.findViewById(R.id.card_available_reviews_go);

        mLessonsGo.setColorFilter(getResources().getColor(R.color.text_gray),
                PorterDuff.Mode.SRC_ATOP);
        mReviewsGo.setColorFilter(getResources().getColor(R.color.text_gray),
                PorterDuff.Mode.SRC_ATOP);

        mLessonsAvailable = (TextView) rootView.findViewById(R.id.card_available_lessons);
        mReviewsAvailable = (TextView) rootView.findViewById(R.id.card_available_reviews);

        mCard = (LinearLayout) rootView.findViewById(R.id.card_available_card);

        setUpParentOnClicks();

        return rootView;
    }

    private void setUpParentOnClicks() {

        mLessonsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PrefManager.getWebViewIntent(context);
                intent.setAction(WebReviewActivity.OPEN_ACTION);
                intent.setData(Uri.parse(Browser.LESSON_URL));
                getActivity().startActivityForResult(intent, BROWSER_REQUEST);
            }
        });

        mReviewsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PrefManager.getWebViewIntent(context);
                intent.setAction(WebReviewActivity.OPEN_ACTION);
                intent.setData(Uri.parse(Browser.REVIEW_URL));
                getActivity().startActivityForResult(intent, BROWSER_REQUEST);
            }
        });
    }

    private void displayData(User user, Summary summary) {
        if (!user.isVacationModeActive()) {
            if (isAdded()) {
                DateTime now = DateTime.now();
                int lessonsAvailable = summary.getAvailableLessonsCount(now);
                int reviewsAvailable = summary.getAvailableReviewsCount(now);
                Resources res = getResources();
                mLessonsAvailable.setText(res.getQuantityString(R.plurals.card_content_available_lessons_capital, lessonsAvailable, lessonsAvailable));
                mReviewsAvailable.setText(res.getQuantityString(R.plurals.card_content_available_reviews_capital, reviewsAvailable, reviewsAvailable));
            }
            mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
        } else {
            // Vacation mode is handled in DashboardFragment
            mListener.onAvailableCardSyncFinishedListener(DashboardFragment.SYNC_RESULT_SUCCESS);
        }
    }

    public interface AvailableCardListener {
        public void onAvailableCardSyncFinishedListener(String result);
    }
}
