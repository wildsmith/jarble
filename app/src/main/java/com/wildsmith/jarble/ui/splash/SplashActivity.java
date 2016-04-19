package com.wildsmith.jarble.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import com.wildsmith.jarble.BaseApplication;
import com.wildsmith.jarble.preferences.JarblePreferencesHelper;
import com.wildsmith.jarble.timer.TimerService;
import com.wildsmith.jarble.timer.TimerServiceBroadcastReceiver;
import com.wildsmith.jarble.ui.BaseActivity;
import com.wildsmith.jarble.ui.ftux.FTUXActivity;
import com.wildsmith.jarble.ui.jars.JarsActivity;
import com.wildsmith.utils.BroadcastHelper;
import com.wildsmith.utils.ThreadUtils;

public class SplashActivity extends BaseActivity implements TimerServiceBroadcastReceiver.Listener {

    private TimerServiceBroadcastReceiver receiver = new TimerServiceBroadcastReceiver(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createSplashFragment(savedInstanceState);

        TimerService existingService = BaseApplication.getTimerService();
        if (existingService != null) {
            continueFlow();
        } else {
            BroadcastHelper.registerReceiver(this, receiver, TimerServiceBroadcastReceiver.IntentFilter.ON_TIMER_SERVICE_BOUND.name());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        BroadcastHelper.unregisterReceiver(this, receiver);
    }

    private void createSplashFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            replaceContentFragment(SplashFragment.newInstance(), SplashFragment.TAG);
        }
    }

    @Override
    public void onTimerServiceBound() {
        continueFlow();
    }

    private void continueFlow() {
        ThreadUtils.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (JarblePreferencesHelper.needsFTUX(SplashActivity.this)) {
                    startActivity(new Intent(SplashActivity.this, FTUXActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, JarsActivity.class));
                    finish();
                }
            }
        }, SplashFragment.DURATION);
    }
}