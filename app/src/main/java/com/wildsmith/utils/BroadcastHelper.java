package com.wildsmith.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Helper method to allow for 'simpler' registering and unregistering of a {@link BroadcastReceiver}.
 */
public class BroadcastHelper {

    /**
     * Register a {@link BroadcastReceiver} with the {@link LocalBroadcastManager}.
     *
     * @param context       used to get the instance of the {@link LocalBroadcastManager}.
     * @param receiver      to register with the {@link LocalBroadcastManager}.
     * @param intentFilters that the {@link BroadcastReceiver} is 'listening' for.
     */
    public static void registerReceiver(@NonNull Context context, @NonNull BroadcastReceiver receiver,
        @NonNull final String... intentFilters) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, createIntentFilter(intentFilters));
    }

    private static IntentFilter createIntentFilter(String[] intentFilters) {
        IntentFilter filter = new IntentFilter(intentFilters[0]);

        for (int i = 0; i < intentFilters.length; i++) {
            if (i == 0) {
                continue;
            }
            filter.addAction(intentFilters[i]);
        }

        return filter;
    }

    /**
     * Unregister the {@link BroadcastReceiver} with the {@link LocalBroadcastManager}.
     *
     * @param context  used to get the instance of the {@link LocalBroadcastManager}.
     * @param receiver to register with the {@link LocalBroadcastManager}.
     */
    public static void unregisterReceiver(@NonNull Context context, @NonNull BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    /**
     * Sends out a broadcast leveraging {@link LocalBroadcastManager}
     *
     * @param context used to get the instance of the {@link LocalBroadcastManager}.
     * @param intent  the intent to send to the {@link BroadcastReceiver}.
     */
    public static void sendBroadcast(@NonNull Context context, @NonNull Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}