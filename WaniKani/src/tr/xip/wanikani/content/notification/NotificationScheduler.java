package tr.xip.wanikani.content.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Response;
import tr.xip.wanikani.client.task.callback.ThroughDbCallbackV2;
import tr.xip.wanikani.client.v2.WaniKaniApiV2;
import tr.xip.wanikani.database.DatabaseManager;
import tr.xip.wanikani.models.v2.reviews.Summary;

public class NotificationScheduler {
    private Context context;

    private NotificationPreferences prefs;

    public NotificationScheduler(Context context) {
        this.context = context;
        prefs = new NotificationPreferences(context);
    }

    public void schedule() {
        /*WaniKaniApiV2.getSummary().enqueue(new ThroughDbCallbackV2<Summary>() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onResponse(Call<Summary> call, Response<Summary> response) {
                super.onResponse(call, response);

                if (response.isSuccessful() && response.body() != null) {
                    load(response.body());
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<Summary> call, Throwable t) {
                super.onFailure(call, t);

                Summary summary = DatabaseManager.getSummary();
                if (summary != null) {
                    load(summary);
                }
            }

            void load(Summary summary) {
                if (summary.getNextReviewDateInMillis() <= System.currentTimeMillis()) {
                    new NotificationPublisher().publish(context);
                    return;
                }

                if (!prefs.isAlarmSet()) {
                    PendingIntent pendingIntent = getPendingIntent();
                    AlarmManager alarmManager = getAlarmManager();
                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            summary.getNextReviewDateInMillis() + NotificationPreferences.NOTIFICATION_CHECK_DELAY,
                            pendingIntent
                    );

                    Log.d("NOTIFICATION SCHEDULER", "SCHEDULED NOTIFICATION FOR " + new SimpleDateFormat("HH:mm:ss").format(summary.getNextReviewDateInMillis() + NotificationPreferences.NOTIFICATION_CHECK_DELAY));

                    prefs.setAlarmSet(true);
                }
            }
        });*/
    }

    public void cancelNotifications() {
        PendingIntent pendingIntent = getPendingIntent();
        AlarmManager alarmManager = getAlarmManager();
        alarmManager.cancel(pendingIntent);
        prefs.setAlarmSet(false);
    }

    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        return PendingIntent.getBroadcast(
                context,
                NotificationPublisher.REQUEST_CODE,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}