package com.wildsmith.jarble.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Base abstract {@link Fragment} class that can be leveraged to extract boiler plate code from child classes if a pattern is noticed.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
}