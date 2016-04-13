package com.wildsmith.jarble.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public class TimerServiceBroadcastReceiver extends BroadcastReceiver {

    private WeakReference<Listener> listenerWeakReference;

    public TimerServiceBroadcastReceiver(@NonNull Listener listener) {
        this.listenerWeakReference = new WeakReference<>(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Listener listener = getListener();
        if (listener != null) {
            listener.onTimerServiceBound();
        }
    }

    @Nullable
    private Listener getListener() {
        return (listenerWeakReference == null) ? null : listenerWeakReference.get();
    }

    public enum IntentFilter {
        ON_TIMER_SERVICE_BOUND,
    }

    public interface Listener {

        void onTimerServiceBound();
    }
}