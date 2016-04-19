package com.wildsmith.jarble;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wildsmith.jarble.jar.JarTableInMemoryCache;
import com.wildsmith.jarble.timer.TimerService;
import com.wildsmith.jarble.timer.TimerServiceBroadcastReceiver;
import com.wildsmith.jarble.ui.jars.JarsModifiedBroadcastReceiver;
import com.wildsmith.utils.BroadcastHelper;

import java.util.Calendar;
import java.util.Locale;

public class BaseApplication extends Application implements ServiceConnection, JarsModifiedBroadcastReceiver.Listener {

    private static BaseApplication application;

    private static Calendar calendar;

    private static TimerService timerService;

    private JarsModifiedBroadcastReceiver jarReceiver = new JarsModifiedBroadcastReceiver(this);

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        calendar = Calendar.getInstance(Locale.US);

        startTimerService();

        jarReceiver = new JarsModifiedBroadcastReceiver(this);
        BroadcastHelper.registerReceiver(this, jarReceiver, JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_CREATED.name(),
            JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        BroadcastHelper.unregisterReceiver(this, jarReceiver);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (service instanceof TimerService.TimerBinder) {
            timerService = ((TimerService.TimerBinder) service).getService();

            BroadcastHelper.sendBroadcast(getApplicationContext(),
                new Intent(TimerServiceBroadcastReceiver.IntentFilter.ON_TIMER_SERVICE_BOUND.name()));
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        timerService = null;
    }

    public static void startTimerService() {
        application.bindService(new Intent(application, TimerService.class), application, Context.BIND_AUTO_CREATE);
    }

    @NonNull
    public static Calendar getCalendar(boolean clone) {
        return (clone) ? (Calendar) calendar.clone() : calendar;
    }

    @Nullable
    public static TimerService getTimerService() {
        return timerService;
    }

    @Override
    public void onJarsModified() {
        JarTableInMemoryCache.invalidateJarTableModels();
    }
}