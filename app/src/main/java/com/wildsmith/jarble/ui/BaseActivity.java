package com.wildsmith.jarble.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.utils.CollectionUtils;

import java.util.List;

/**
 * Base abstract {@link AppCompatActivity} class that can be leveraged to extract boiler plate code from child classes if a pattern is
 * noticed.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentLayout());
    }

    protected void addContentFragment(Fragment newFragment, String newFragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        addContentFragment(fragmentTransaction, newFragment, newFragmentTag);
    }

    protected void addContentFragment(FragmentTransaction fragmentTransaction, Fragment newFragment, String newFragmentTag) {
        fragmentTransaction.add(R.id.content, newFragment, newFragmentTag);
        fragmentTransaction.commit();
    }

    protected void replaceContentFragment(Fragment newFragment, String newFragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        replaceContentFragment(fragmentTransaction, newFragment, newFragmentTag);
    }

    protected void replaceContentFragment(FragmentTransaction fragmentTransaction, Fragment newFragment, String newFragmentTag) {
        fragmentTransaction.replace(R.id.content, newFragment, newFragmentTag);
        fragmentTransaction.commit();
    }

    protected void detachDestinationFragment(Fragment destinationFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (CollectionUtils.isEmpty(fragments)) {
            return;
        }

        for (Fragment fragment : fragments) {
            if (fragment == destinationFragment) {
                fragmentTransaction.detach(fragment);
                break;
            }
        }

        fragmentTransaction.commit();
    }

    protected void showContentFragment(FragmentTransaction fragmentTransaction, Fragment oldFragment, Fragment newFragment) {
        fragmentTransaction.detach(oldFragment);
        fragmentTransaction.attach(newFragment);
        fragmentTransaction.commit();
    }

    protected int getContentLayout() {
        return R.layout.content_layout;
    }
}