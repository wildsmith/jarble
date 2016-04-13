package com.wildsmith.jarble.timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wildsmith.jarble.preferences.JarblePreferencesHelper;
import com.wildsmith.jarble.utils.ThreadUtils;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class TimerService extends Service {

    private static final int MILLISECONDS_IN_SECOND = 1000;

    private static final int SECONDS_IN_MINUTE = 60;

    private static final int VIBRATION_ALERT_TIME = 500;

    private static final int NOTIFICATION_ID = TimerService.class.getSimpleName().hashCode();

    private enum TimerRunnableState {
        RUNNING,
        STOPPED,
        PAUSED,
        ALERT_STOPPED
    }

    private final IBinder binder = new TimerBinder();

    private final TimerRunnable timerRunnable = new TimerRunnable();

    private WeakReference<TextView> trackingTextViewWeakReference;

    private int remainingSeconds;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public boolean isRunning() {
        return timerRunnable.isRunning();
    }

    public boolean isPaused() {
        return timerRunnable.isPaused();
    }

    public boolean isAlertStopped() {
        return timerRunnable.isAlertStopped();
    }

    public void startTimer() {
        startTimer(getTrackingTextView());
    }

    public void startTimer(TextView trackingTextView) {
        this.trackingTextViewWeakReference = new WeakReference<>(trackingTextView);

        remainingSeconds = SECONDS_IN_MINUTE * JarblePreferencesHelper.getBehaviorDurationAsInteger(getApplicationContext());

        timerRunnable.start();

        setTrackingTextViewText();
    }

    public void pauseTimer() {
        timerRunnable.pause();

        TimerIntentService.showHaltedNotification(getApplicationContext(), NOTIFICATION_ID, getPassingTimeText(),
            remainingSeconds);
    }

    void resumeTimer() {
        resumeTimer(getTrackingTextView());
    }

    public void resumeTimer(TextView trackingTextView) {
        this.trackingTextViewWeakReference = new WeakReference<>(trackingTextView);

        timerRunnable.start();

        TimerIntentService.showOnGoingTimerNotification(getApplicationContext(), NOTIFICATION_ID, getPassingTimeText(),
            remainingSeconds);
    }

    public void stopTimer() {
        timerRunnable.stop();

        TimerIntentService.showHaltedNotification(getApplicationContext(), NOTIFICATION_ID, getPassingTimeText(),
            remainingSeconds);
    }

    void alertTimer() {
        timerRunnable.stop();

        TimerIntentService.showAlertNotification(getApplicationContext(), NOTIFICATION_ID);

        if (JarblePreferencesHelper.isVibrationEnabled(getApplicationContext(), false)) {
            Vibrator vibrator = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATION_ALERT_TIME);
        }
    }

    public void updateTrackingTextView(TextView trackingTextView) {
        this.trackingTextViewWeakReference = new WeakReference<>(trackingTextView);

        setTrackingTextViewText();
    }

    private class TimerRunnable implements Runnable {

        private TimerRunnableState state;

        public void start() {
            if (state == TimerRunnableState.RUNNING) {
                return;
            }

            state = TimerRunnableState.RUNNING;
            ThreadUtils.postOnUiThread(timerRunnable, MILLISECONDS_IN_SECOND);
        }

        public void pause() {
            state = TimerRunnableState.PAUSED;
        }

        public void stop() {
            state = TimerRunnableState.STOPPED;
            remainingSeconds = SECONDS_IN_MINUTE * JarblePreferencesHelper.getBehaviorDurationAsInteger(getApplicationContext());
        }

        public boolean isRunning() {
            return state == TimerRunnableState.RUNNING;
        }

        public boolean isPaused() {
            return state == TimerRunnableState.PAUSED;
        }

        public boolean isAlertStopped() {
            return state == TimerRunnableState.ALERT_STOPPED;
        }

        @Override
        public void run() {
            if (isRunning()) {
                if (remainingSeconds <= 0) {
                    alertTimer();
                    state = TimerRunnableState.ALERT_STOPPED;
                    return;
                }

                remainingSeconds--;

                setTrackingTextViewText();

                updateTrackingTextView();
            }
        }
    }

    private void updateTrackingTextView() {
        ThreadUtils.postOnUiThread(timerRunnable, MILLISECONDS_IN_SECOND);
    }

    private void setTrackingTextViewText() {
        TextView trackingTextView = getTrackingTextView();
        if (trackingTextView != null) {
            trackingTextView.setText(getPassingTimeText());
        }

        TimerIntentService.showOnGoingTimerNotification(getApplicationContext(), NOTIFICATION_ID, getPassingTimeText(),
            remainingSeconds);
    }

    private String getPassingTimeText() {
        return String.format(Locale.getDefault(), "%02d : %02d", (remainingSeconds / 60) % 60, remainingSeconds % 60);
    }

    @Nullable
    private TextView getTrackingTextView() {
        if (trackingTextViewWeakReference != null && trackingTextViewWeakReference.get() != null) {
            return trackingTextViewWeakReference.get();
        }

        return null;
    }

    public class TimerBinder extends Binder {

        public TimerService getService() {
            return TimerService.this;
        }
    }
}