package com.wildsmith.jarble.timer;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.wildsmith.jarble.BaseApplication;
import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableInMemoryCache;
import com.wildsmith.jarble.ui.jar.SingleJarActivity;
import com.wildsmith.jarble.ui.jars.JarsActivity;
import com.wildsmith.notification.NotificationHelper;

public class TimerIntentService extends IntentService {

    enum Extras {
        ACTION,
        NOTIFICATION_ID,
        PASSING_TIME_TEXT,
        PASSED_SECONDS
    }

    enum Action {
        PLAY,
        STOP;

        static Action fromInt(int actionInt) {
            for (Action action : Action.values()) {
                if (action.ordinal() == actionInt) {
                    return action;
                }
            }

            return PLAY;
        }
    }

    private static final String TAG = TimerIntentService.class.getSimpleName();

    private static Intent timerIntent = null;

    @NonNull
    private static Intent buildTimerIntent(@NonNull Context context, int actionOrdinal, int notificationId,
        @NonNull String passingTimeText, int passedSeconds) {
        if (timerIntent == null) {
            timerIntent = new Intent(context, TimerIntentService.class);
        }

        timerIntent.putExtra(Extras.ACTION.name(), actionOrdinal);
        timerIntent.putExtra(Extras.NOTIFICATION_ID.name(), notificationId);
        timerIntent.putExtra(Extras.PASSING_TIME_TEXT.name(), passingTimeText);
        timerIntent.putExtra(Extras.PASSED_SECONDS.name(), passedSeconds);

        return timerIntent;
    }

    @NonNull
    private static Intent buildEditJarIntent(@NonNull Context context) {
        Intent intent = new Intent(context, SingleJarActivity.class);
        intent.putExtra(SingleJarActivity.Extra.MODEL.name(), JarTableInMemoryCache.getInProgressJarTableModel(context));
        intent.putExtra(SingleJarActivity.Extra.MODE.name(), SingleJarActivity.Mode.EDIT);
        return intent;
    }

    @NonNull
    private static Intent buildViewJarsIntent(@NonNull Context context) {
        return new Intent(context, JarsActivity.class);
    }

    public TimerIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TimerService timerService = BaseApplication.getTimerService();
        switch (Action.fromInt(intent.getIntExtra(Extras.ACTION.name(), 0))) {
            case PLAY:
                if (timerService != null) {
                    timerService.resumeTimer();
                }
                break;
            case STOP:
                if (timerService != null) {
                    timerService.pauseTimer();
                }
                break;
        }
    }

    static void showOnGoingTimerNotification(@NonNull Context context, int notificationId, @NonNull String passingTimeText,
        int passedSeconds) {
        Intent intent = buildTimerIntent(context, Action.STOP.ordinal(), notificationId, passingTimeText, passedSeconds);
        Intent contentIntent = buildViewJarsIntent(context);

        new NotificationHelper.Builder()
            .setId(notificationId)
            .setIcon(R.drawable.icon_puzzle_piece)
            .setColor(ContextCompat.getColor(context, R.color.dark_green))
            .setTitle(context.getString(R.string.app_name))
            .setText(passingTimeText)
            .setNotificationFlags(Notification.FLAG_ONGOING_EVENT)
            .addAction(new NotificationCompat.Action(R.drawable.ic_pause_black, context.getString(R.string.notification_pause),
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)))
            .setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0))
            .show(context);
    }

    static void showHaltedNotification(@NonNull Context context, int notificationId, @NonNull String passingTimeText,
        int passedSeconds) {
        Intent intent = buildTimerIntent(context, Action.PLAY.ordinal(), notificationId, passingTimeText, passedSeconds);
        Intent contentIntent = buildViewJarsIntent(context);

        new NotificationHelper.Builder()
            .setId(notificationId)
            .setIcon(R.drawable.icon_puzzle_piece)
            .setColor(ContextCompat.getColor(context, R.color.dark_green))
            .setTitle(context.getString(R.string.app_name))
            .setText(passingTimeText)
            .setNotificationFlags(Notification.FLAG_AUTO_CANCEL)
            .addAction(new NotificationCompat.Action(R.drawable.ic_play_arrow_black,
                ((passedSeconds > 0) ? context.getString(R.string.notification_resume) : context.getString(R.string.notification_play)),
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)))
            .setContentIntent(PendingIntent.getActivity(context, 0, contentIntent, 0))
            .show(context);
    }

    static void showAlertNotification(@NonNull Context context, int notificationId) {
        Intent intent = buildEditJarIntent(context);

        new NotificationHelper.Builder()
            .setId(notificationId)
            .setIcon(R.drawable.icon_puzzle_piece)
            .setColor(ContextCompat.getColor(context, R.color.red))
            .setTitle(context.getString(R.string.app_name))
            .setText("Behavior check in.")
            .setNotificationFlags(Notification.FLAG_ONGOING_EVENT)
            .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
            .show(context);
    }
}