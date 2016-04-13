package com.wildsmith.jarble.ui.jars;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public interface AnimatingFragment {

    void buildTransitionNames(@NonNull FragmentTransaction fragmentTransaction, int newSpanCount, JarsFragment destinationFragment);

    void reBuildTransitionNames(View sceneRoot);
}