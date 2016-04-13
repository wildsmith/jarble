package com.wildsmith.jarble.ui.jars;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public class JarsModifiedBroadcastReceiver extends BroadcastReceiver {

    private WeakReference<Listener> listenerWeakReference;

    public JarsModifiedBroadcastReceiver(@NonNull Listener listener) {
        this.listenerWeakReference = new WeakReference<>(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Listener listener = getListener();
        if (listener != null) {
            listener.onJarsModified();
        }
    }

    @Nullable
    private Listener getListener() {
        return (listenerWeakReference == null) ? null : listenerWeakReference.get();
    }

    public enum IntentFilter {
        ON_JAR_CREATED,
        ON_JAR_UPDATED
    }

    public interface Listener {

        void onJarsModified();
    }
}