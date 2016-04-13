package com.wildsmith.jarble.ui.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.ui.BaseFragment;

public class SplashFragment extends BaseFragment {

    public static final String TAG = SplashFragment.class.getSimpleName();

    public static final int DURATION = 1000;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View floatingActionButton = view.findViewById(R.id.floating_action_button);
        if (floatingActionButton != null) {
            floatingActionButton.animate().alphaBy(1).setDuration(DURATION).start();
        }
    }
}
