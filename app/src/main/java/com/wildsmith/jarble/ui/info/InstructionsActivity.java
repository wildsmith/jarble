package com.wildsmith.jarble.ui.info;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.ui.BaseActivity;
import com.wildsmith.jarble.ui.bitmap.BitmapLoader;

public class InstructionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createInfoFragment(savedInstanceState);

        setupBackdrop();
        setupCollapsingToolbarLayout();
        setupToolbar();
    }

    private void createInfoFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString(InfoFragment.Extras.INFO.name(), getString(R.string.instructions_info));
            replaceContentFragment(InfoFragment.newInstance(bundle), InfoFragment.TAG);
        }
    }

    private void setupBackdrop() {
        ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
        if (backdrop == null) {
            return;
        }

        new BitmapLoader.Builder().setResources(getResources()).setResourceId(R.drawable.marbles_background).setImageView(backdrop)
            .loadBitmap();
    }

    private void setupCollapsingToolbarLayout() {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbar != null) {
            collapsingToolbar.setTitle(getString(R.string.instructions_title));
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.info_activity_layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}