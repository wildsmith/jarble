package com.wildsmith.jarble.ui.ftux;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.ui.BaseActivity;

public class FTUXActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createFTUXFragment(savedInstanceState);

        setupStatusBar();
    }

    private void createFTUXFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            replaceContentFragment(FTUXFragment.newInstance(), FTUXFragment.TAG);
        }
    }

    private void setupStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_green));
    }
}
