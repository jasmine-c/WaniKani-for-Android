package tr.xip.wanikani.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tr.xip.wanikani.R;
import tr.xip.wanikani.app.activity.ProgressDetailsActivity;
import tr.xip.wanikani.app.fragment.card.AvailableCard;
import tr.xip.wanikani.app.fragment.card.CriticalItemsCard;
import tr.xip.wanikani.app.fragment.card.MessageCard;
import tr.xip.wanikani.app.fragment.card.NotificationsCard;
import tr.xip.wanikani.app.fragment.card.ProgressCard;
import tr.xip.wanikani.app.fragment.card.RecentUnlocksCard;
import tr.xip.wanikani.app.fragment.card.ReviewsCard;
import tr.xip.wanikani.app.fragment.card.SRSCard;
import tr.xip.wanikani.app.fragment.card.VacationModeCard;
import tr.xip.wanikani.client.v2.DataUpdater;
import tr.xip.wanikani.client.v2.Filter;
import tr.xip.wanikani.client.v2.WaniKaniApiV2;
import tr.xip.wanikani.content.receiver.BroadcastIntents;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.database.table.UsersTable;
import tr.xip.wanikani.database.v2.AssignmentsTable;
import tr.xip.wanikani.database.v2.ReviewStatisticsTable;
import tr.xip.wanikani.database.v2.SubjectsTable;
import tr.xip.wanikani.database.v2.SummaryTable;
import tr.xip.wanikani.managers.PrefManager;
import tr.xip.wanikani.models.Notification;
import tr.xip.wanikani.models.User;
import tr.xip.wanikani.models.v2.Resource;
import tr.xip.wanikani.models.v2.reviews.AssignmentCollection;
import tr.xip.wanikani.models.v2.reviews.ReviewStatisticCollection;
import tr.xip.wanikani.models.v2.reviews.Summary;
import tr.xip.wanikani.models.v2.subjects.SubjectCollection;

public class DashboardFragment extends Fragment
    implements SwipeRefreshLayout.OnRefreshListener,
    AvailableCard.AvailableCardListener,
    ReviewsCard.ReviewsCardListener,
    SRSCard.StatusCardListener,
    ProgressCard.ProgressCardListener,
    RecentUnlocksCard.RecentUnlocksCardListener,
    CriticalItemsCard.CriticalItemsCardListener,
    MessageCard.MessageCardListener,
    View.OnClickListener {

    public static final String SYNC_RESULT_SUCCESS = "success";
    public static final String SYNC_RESULT_FAILED = "failed";
    View rootView;
    AppCompatActivity activity;
    boolean isAvailableCardSynced = false;
    boolean isReviewsCardSynced = false;
    boolean isStatusCardSynced = false;
    boolean isProgressCardSynced = false;
    boolean isRecentUnlocksCardSynced = false;
    boolean isCriticalItemsCardSynced = false;
    boolean isAvailableCardSyncedSuccess = false;
    boolean isReviewsCardSyncedSuccess = false;
    boolean isStatusCardSyncedSuccess = false;
    boolean isProgressCardSyncedSuccess = false;
    boolean isRecentUnlocksCardSyncedSuccess = false;
    boolean isCriticalItemsCardSyncedSuccess = false;
    LinearLayout mAvailableHolder;
    CardView mReviewsHolder;
    CardView mProgressHolder;
    LinearLayout mCriticalItemsFragmentHolder;
    LinearLayout mRecentUnlocksFragmentHolder;
    CardView mMessageCardHolder;
    CardView mNotificationsCardHolder;
    CardView mVacationModeCardHolder;
    FrameLayout mVacationModeCard;
    FrameLayout mReviewsCard;
    FrameLayout mProgressCard;
    private Context context;
    private SwipeRefreshLayout mSwipeToRefreshLayout;

    private BroadcastReceiver mRetrofitConnectionErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showMessage(MESSAGE_TYPE.ERROR_NO_CONNECTION);
        }
    };
    private BroadcastReceiver mRetrofitUnknownErrorReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showMessage(MESSAGE_TYPE.ERROR_UNKNOWN);
        }
    };
    private BroadcastReceiver mNotificationsReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            showNotificationIfExists();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceivers();
    }

    @Override
    public void onPause() {
        unregisterReceivers();
        super.onPause();
    }

    @Override
    public void onCreate(Bundle paramBundle) {
        this.context = getActivity();
        super.onCreate(paramBundle);
    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {

        rootView = paramLayoutInflater.inflate(R.layout.fragment_dashboard, paramViewGroup, false);

        activity = (AppCompatActivity) getActivity();

        mSwipeToRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.dashboard_swipe_refresh);
        mSwipeToRefreshLayout.setOnRefreshListener(this);
        mSwipeToRefreshLayout.setColorSchemeResources(R.color.swipe_refresh);

        mAvailableHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_available_holder);
        mReviewsHolder = (CardView) rootView.findViewById(R.id.fragment_dashboard_reviews_holder);
        mProgressHolder = (CardView) rootView.findViewById(R.id.fragment_dashboard_progress_holder);
        mRecentUnlocksFragmentHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_recent_unlocks_holder);
        mCriticalItemsFragmentHolder = (LinearLayout) rootView.findViewById(R.id.fragment_dashboard_critical_items_holder);

        mMessageCardHolder = (CardView) rootView.findViewById(R.id.fragment_dashboard_message_card_holder);
        mNotificationsCardHolder = (CardView) rootView.findViewById(R.id.fragment_dashboard_notifications_card_holder);
        mVacationModeCardHolder = (CardView) rootView.findViewById(R.id.fragment_dashboard_vacation_mode_card_holder);

        mVacationModeCard = (FrameLayout) rootView.findViewById(R.id.fragment_dashboard_vacation_mode_card);
        mReviewsCard = (FrameLayout) rootView.findViewById(R.id.fragment_dashboard_reviews_card);
        mProgressCard = (FrameLayout) rootView.findViewById(R.id.fragment_dashboard_progress_card);

        mProgressHolder.setOnClickListener(this);

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        VacationModeCard vacationModeCard = new VacationModeCard();
        AvailableCard availableCard = new AvailableCard();
        ReviewsCard reviewsCard = new ReviewsCard();
        SRSCard statusCard = new SRSCard();
        ProgressCard progressCard = new ProgressCard();
        RecentUnlocksCard recentUnlocksCard = new RecentUnlocksCard();
        CriticalItemsCard criticalItemsCard = new CriticalItemsCard();

        availableCard.setListener(this, getActivity());
        reviewsCard.setListener(this, getActivity());
        statusCard.setListener(this, getActivity());
        progressCard.setListener(this, getActivity());
        recentUnlocksCard.setListener(this, getActivity());
        criticalItemsCard.setListener(this, getActivity());

        transaction.replace(R.id.fragment_dashboard_vacation_mode_card, vacationModeCard);
        transaction.replace(R.id.fragment_dashboard_available_card, availableCard);
        transaction.replace(R.id.fragment_dashboard_reviews_card, reviewsCard);
        transaction.replace(R.id.fragment_dashboard_status_card, statusCard);
        transaction.replace(R.id.fragment_dashboard_progress_card, progressCard);
        transaction.replace(R.id.fragment_dashboard_recent_unlocks_card, recentUnlocksCard);
        transaction.replace(R.id.fragment_dashboard_critical_items_card, criticalItemsCard);
        transaction.commit();

        sync();

        return rootView;
    }

    private void sync() {
        setRefreshing();

        showNotificationIfExists();

        Completable.merge(
            new DataUpdater(new Func1<Filter, Observable<Resource<tr.xip.wanikani.models.v2.user.User>>>() {
                @Override
                public Observable<Resource<tr.xip.wanikani.models.v2.user.User>> call(Filter filter) {
                    return WaniKaniApiV2.getUser();
                }
            }, UsersTable.TABLE_NAME).updateData(),
            new DataUpdater(new Func1<Filter, Observable<AssignmentCollection>>() {
                @Override
                public Observable<AssignmentCollection> call(Filter filter) {
                    return WaniKaniApiV2.getAssignments(filter);
                }
            }, AssignmentsTable.TABLE_NAME).updateData(),
            new DataUpdater(new Func1<Filter, Observable<Summary>>() {
                @Override
                public Observable<Summary> call(Filter filter) {
                    return WaniKaniApiV2.getSummary();
                }
            }, SummaryTable.TABLE_NAME).updateData(),
            new DataUpdater(new Func1<Filter, Observable<SubjectCollection>>() {
                @Override
                public Observable<SubjectCollection> call(Filter filter) {
                    return WaniKaniApiV2.getSubjects(filter);
                }
            }, SubjectsTable.TABLE_NAME).updateData(),
            new DataUpdater(new Func1<Filter, Observable<ReviewStatisticCollection>>() {
                @Override
                public Observable<ReviewStatisticCollection> call(Filter filter) {
                    return WaniKaniApiV2.getReviewStatistics(filter);
                }
            }, ReviewStatisticsTable.TABLE_NAME).updateData()
        )
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(new Action0() {
                @Override
                public void call() {
                    Intent intent = new Intent(BroadcastIntents.SYNC());
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    checkVacationMode();
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    showMessage(MESSAGE_TYPE.ERROR_UNKNOWN);
                }
            });
    }

    private void setRefreshing() {
        if (mSwipeToRefreshLayout != null)
            mSwipeToRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeToRefreshLayout.setRefreshing(true);
                }
            });
    }

    private void updateSyncStatus() {
        if (isAvailableCardSynced && isReviewsCardSynced && isStatusCardSynced && isProgressCardSynced && isRecentUnlocksCardSynced && isCriticalItemsCardSynced) {
            mSwipeToRefreshLayout.setRefreshing(false);

            if (isAvailableCardSyncedSuccess && isReviewsCardSyncedSuccess && isStatusCardSyncedSuccess && isRecentUnlocksCardSyncedSuccess
                && isCriticalItemsCardSyncedSuccess) {
                PrefManager.setDashboardLastUpdateDate(System.currentTimeMillis());
                onMessageCardOkButtonClick();
            }
        }
    }

    private void registerReceivers() {
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitConnectionErrorReceiver,
            new IntentFilter(BroadcastIntents.RETROFIT_ERROR_CONNECTION()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRetrofitUnknownErrorReceiver,
            new IntentFilter(BroadcastIntents.RETROFIT_ERROR_UNKNOWN()));
        LocalBroadcastManager.getInstance(activity).registerReceiver(mNotificationsReceiver,
            new IntentFilter(BroadcastIntents.NOTIFICATION()));
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRetrofitConnectionErrorReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRetrofitUnknownErrorReceiver);
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mNotificationsReceiver);
    }

    private void showMessage(MESSAGE_TYPE msgType) {
        if (getActivity() == null) return;

        try {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            MessageCard fragment = new MessageCard();
            fragment.setListener(this);

            String title = "";
            String prefix = "";

            if (msgType == MESSAGE_TYPE.ERROR_NO_CONNECTION) {
                title = getString(R.string.error_no_connection);
                prefix = getString(R.string.content_last_updated) + " ";
            }

            if (msgType == MESSAGE_TYPE.ERROR_UNKNOWN) {
                title = getString(R.string.error_unknown_error);
                prefix = getString(R.string.content_last_updated) + " ";
            }

            Bundle args = new Bundle();
            args.putString(MessageCard.ARG_TITLE, title);
            args.putString(MessageCard.ARG_PREFIX, prefix);
            args.putLong(MessageCard.ARG_TIME, PrefManager.getDashboardLastUpdateTime());
            fragment.setArguments(args);

            transaction.replace(R.id.fragment_dashboard_message_card, fragment).commit();

            mMessageCardHolder.setVisibility(View.VISIBLE);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            // Probably http://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html
            // Ignore. No need to show message if Activity has been killed.
        }
    }

    private void showNotificationIfExists() {
        List<Notification> notifications = DatabaseManager.getNotifications();

        if (notifications != null && notifications.size() != 0) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            Bundle args = new Bundle();
            args.putSerializable(NotificationsCard.ARG_NOTIFICATIONS, (Serializable) notifications);
            NotificationsCard card = new NotificationsCard();
            card.setArguments(args);
            transaction.replace(R.id.fragment_dashboard_notifications_card, card).commit();
            mNotificationsCardHolder.setVisibility(View.VISIBLE);
        } else {
            mNotificationsCardHolder.setVisibility(View.GONE);
        }
    }

    private void setCriticalItemsFragmentHeight(int height) {
        ViewGroup.LayoutParams params = mCriticalItemsFragmentHolder.getLayoutParams();
        params.height = height;
        mCriticalItemsFragmentHolder.setLayoutParams(params);
    }

    private void setRecentUnlocksFragmentHeight(int height) {
        ViewGroup.LayoutParams params = mRecentUnlocksFragmentHolder.getLayoutParams();
        params.height = height;
        mRecentUnlocksFragmentHolder.setLayoutParams(params);
    }

    private void checkVacationMode() {
        User user = DatabaseManager.getUser();
        if (user == null) return;

        if (user.isVacationModeActive()) {
            mAvailableHolder.setVisibility(View.GONE);
            mReviewsHolder.setVisibility(View.GONE);
            mVacationModeCardHolder.setVisibility(View.VISIBLE);
        } else {
            mAvailableHolder.setVisibility(View.VISIBLE);
            mReviewsHolder.setVisibility(View.VISIBLE);
            mVacationModeCardHolder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAvailableCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            isAvailableCardSyncedSuccess = true;

        if (result.equals(SYNC_RESULT_FAILED))
            isAvailableCardSyncedSuccess = false;

        isAvailableCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onReviewsCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            isReviewsCardSyncedSuccess = true;

        if (result.equals(SYNC_RESULT_FAILED))
            isReviewsCardSyncedSuccess = false;

        isReviewsCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onStatusCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            isStatusCardSyncedSuccess = true;

        if (result.equals(SYNC_RESULT_FAILED))
            isStatusCardSyncedSuccess = false;

        isStatusCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onProgressCardSyncFinishedListener(String result) {
        if (result.equals(SYNC_RESULT_SUCCESS))
            isProgressCardSyncedSuccess = true;

        if (result.equals(SYNC_RESULT_FAILED))
            isProgressCardSyncedSuccess = false;

        isProgressCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onRecentUnlocksCardSyncFinishedListener(int height, String result) {
        if (result.equals(SYNC_RESULT_SUCCESS)) {
            isRecentUnlocksCardSyncedSuccess = true;
        }

        if (result.equals(SYNC_RESULT_FAILED)) {
            isRecentUnlocksCardSyncedSuccess = false;
        }

        setRecentUnlocksFragmentHeight(height);
        isRecentUnlocksCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onCriticalItemsCardSyncFinishedListener(int height, String result) {
        if (result.equals(SYNC_RESULT_SUCCESS)) {
            isCriticalItemsCardSyncedSuccess = true;
        }

        if (result.equals(SYNC_RESULT_FAILED)) {
            isCriticalItemsCardSyncedSuccess = false;
        }

        setCriticalItemsFragmentHeight(height);
        isCriticalItemsCardSynced = true;
        updateSyncStatus();
    }

    @Override
    public void onMessageCardOkButtonClick() {
        mMessageCardHolder.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        isAvailableCardSynced = false;
        isReviewsCardSynced = false;
        isStatusCardSynced = false;
        isProgressCardSynced = false;
        isRecentUnlocksCardSynced = false;
        isCriticalItemsCardSynced = false;

        isAvailableCardSyncedSuccess = false;
        isReviewsCardSyncedSuccess = false;
        isStatusCardSyncedSuccess = false;
        isProgressCardSyncedSuccess = false;
        isRecentUnlocksCardSyncedSuccess = false;
        isCriticalItemsCardSyncedSuccess = false;

        sync();
    }

    @Override
    public void onClick(View view) {
        if (view == mReviewsHolder) {
            // TODO - Handle reviews card stuff
        }
        if (view == mProgressHolder) {
            startActivity(new Intent(getActivity(), ProgressDetailsActivity.class));
        }
    }

    enum MESSAGE_TYPE {
        ERROR_NO_CONNECTION,
        ERROR_UNKNOWN
    }
}