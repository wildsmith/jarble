package com.wildsmith.jarble.ui.jars;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wildsmith.jarble.BaseApplication;
import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableInMemoryCache;
import com.wildsmith.jarble.jar.JarTableInteractionHelper;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.marble.MarbleTableInteractionHelper;
import com.wildsmith.jarble.marble.MarbleTableModel;
import com.wildsmith.jarble.preferences.JarblePreferencesHelper;
import com.wildsmith.jarble.preferences.SettingsActivity;
import com.wildsmith.jarble.timer.TimerService;
import com.wildsmith.jarble.ui.BaseActivity;
import com.wildsmith.jarble.ui.GenericDialogFragment;
import com.wildsmith.bitmap.BitmapLoader;
import com.wildsmith.jarble.ui.info.DescriptionActivity;
import com.wildsmith.jarble.ui.info.InstructionsActivity;
import com.wildsmith.jarble.ui.jars.monthly.MonthlyFragment;
import com.wildsmith.jarble.ui.jars.weekly.WeeklyFragment;
import com.wildsmith.jarble.ui.jars.yearly.YearlyFragment;
import com.wildsmith.utils.BroadcastHelper;
import com.wildsmith.utils.CollectionUtils;

import java.util.Calendar;
import java.util.List;

public class JarsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, GenericDialogFragment.Listener {

    private ActionMenuView actionMenuView;

    private ActionBarDrawerToggle drawerToggle;

    private boolean isAlarmRunning;

    private enum Extra {
        ALARM_RUNNING
    }

    @Override
    protected int getContentLayout() {
        return R.layout.jars_content_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupCurrentView(savedInstanceState);

        setupToolBar();
        setupActionMenuView();
        setupDrawerLayout();
        setupNavigationView();
        setupAlarmManager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupAlarmRunningField();

        handleAlarmRecovery();

        handleResumedView();
    }

    private void setupAlarmRunningField() {
        TimerService timerService = BaseApplication.getTimerService();
        //isAlarmRunning should always be in sync with the timer
        isAlarmRunning = timerService != null && (timerService.isRunning() || timerService.isPaused() || timerService.isAlertStopped());
    }

    private boolean isAlarmRecovery() {
        //We've recovered from some sort of crash
        return !isAlarmRunning && JarTableInMemoryCache.getInProgressJarTableModel(this) != null;
    }

    private void handleAlarmRecovery() {
        if (isAlarmRecovery()) {
            GenericDialogFragment dialogFragment = new GenericDialogFragment();
            dialogFragment.setContent(getString(R.string.alarm_recovery_title), getString(R.string.alarm_recovery_message));
            dialogFragment.setListener(this);
            dialogFragment.setCancelable(false);
            dialogFragment.show(getSupportFragmentManager(), GenericDialogFragment.TAG);
        }
    }

    @Override
    public void onRightButtonClicked() {
        isAlarmRunning = false;
        JarTableModel model = JarTableInMemoryCache.getInProgressJarTableModel(this);
        if (model != null) {
            model.setInProgress(false);
            MarbleTableModel.flipAllToDone(model.getMarbles());
            JarTableInteractionHelper.updateJarTableModel(this, model);
            MarbleTableInteractionHelper.updateMarbleTableModels(this, model.getMarbles());
            BroadcastHelper.sendBroadcast(this, new Intent(JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name()));
        }
    }

    @Override
    public void onLeftButtonClicked() {
        final ImageButton alarmManagerButton = (ImageButton) findViewById(R.id.alarm_manager_button);
        if (alarmManagerButton != null) {
            alarmManagerButton.callOnClick();
        }
    }

    private void handleResumedView() {
        TimerService timerService = BaseApplication.getTimerService();
        if (isAlarmRunning && timerService != null) {
            setupAlarmButtonVisual();
            timerService.updateTrackingTextView((TextView) findViewById(R.id.bottom_bottom_sheet_title));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerLayout() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            return;
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setupCurrentView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            handleCurrentView();
        }
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(null);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void setupActionMenuView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }

        actionMenuView = (ActionMenuView) toolbar.findViewById(R.id.action_menu_view);
        if (actionMenuView == null) {
            return;
        }

        actionMenuView.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_jars_menu, menu);
        inflater.inflate(R.menu.view_jars_menu, actionMenuView.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final JarblePreferencesHelper.CurrentView currentView;
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerToggle.onOptionsItemSelected(item);
                return true;
            case R.id.yearly_view:
                currentView = JarblePreferencesHelper.getCurrentView(this);
                JarblePreferencesHelper.setCurrentView(this, JarblePreferencesHelper.CurrentView.YEARLY);
                handleCurrentViewWithAnimation(currentView);
                return true;
            case R.id.monthly_view:
                currentView = JarblePreferencesHelper.getCurrentView(this);
                JarblePreferencesHelper.setCurrentView(this, JarblePreferencesHelper.CurrentView.MONTHLY);
                handleCurrentViewWithAnimation(currentView);
                return true;
            case R.id.weekly_view:
                currentView = JarblePreferencesHelper.getCurrentView(this);
                JarblePreferencesHelper.setCurrentView(this, JarblePreferencesHelper.CurrentView.WEEKLY);
                handleCurrentViewWithAnimation(currentView);
                return true;
            case R.id.new_jar:
                if (isAlarmRunning) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JarsActivity.this);
                    builder.setMessage(getString(R.string.alarm_already_running));
                    builder.setPositiveButton(getString(R.string.alarm_already_running_dismiss_button),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    if (JarTableInMemoryCache.getInProgressJarTableModel(this) != null) {
                        GenericDialogFragment dialogFragment = new GenericDialogFragment();
                        dialogFragment.setContent(getString(R.string.jar_in_progress_title), getString(R.string.jar_in_progress_message));
                        dialogFragment.setListener(this);
                        dialogFragment.setCancelable(false);
                        dialogFragment.show(getSupportFragmentManager(), GenericDialogFragment.TAG);
                    } else {
                        final ImageButton alarmManagerButton = (ImageButton) findViewById(R.id.alarm_manager_button);
                        if (alarmManagerButton != null) {
                            alarmManagerButton.callOnClick();
                        }

                        Calendar calendar = Calendar.getInstance();

                        JarTableModel model = new JarTableModel.Builder().setInProgress(true).build(this, calendar.getTimeInMillis());
                        JarTableInteractionHelper.insertJarTableModel(this, model);
                        MarbleTableInteractionHelper.insertMarbleTableModels(this, model.getMarbles());

                        BroadcastHelper.sendBroadcast(this, new Intent(JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_CREATED.name()));
                    }
                }

                return true;
            case R.id.nav_home:
                if (drawerLayout != null) {
                    drawerLayout.closeDrawers();
                }
                currentView = JarblePreferencesHelper.getCurrentView(this);
                JarblePreferencesHelper.setCurrentView(this, JarblePreferencesHelper.CurrentView.YEARLY);
                handleCurrentViewWithAnimation(currentView);
                return true;
            case R.id.nav_instructions:
                if (drawerLayout != null) {
                    drawerLayout.closeDrawers();
                }
                startActivity(new Intent(this, InstructionsActivity.class));
                return true;
            case R.id.nav_description:
                if (drawerLayout != null) {
                    drawerLayout.closeDrawers();
                }
                startActivity(new Intent(this, DescriptionActivity.class));
                return true;
            case R.id.nav_settings:
                if (drawerLayout != null) {
                    drawerLayout.closeDrawers();
                }
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.left_drawer);
        if (navigationView == null) {
            return;
        }

        View headerView = navigationView.getHeaderView(0);
        if (headerView == null) {
            return;
        }

        new BitmapLoader.Builder().setResources(getResources()).setResourceId(R.drawable.mountains_background)
            .setImageView((ImageView) headerView.findViewById(R.id.nav_header_image_view)).loadBitmap();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return onOptionsItemSelected(item);
    }

    private void setupAlarmManager() {
        final ImageButton alarmManagerButton = (ImageButton) findViewById(R.id.alarm_manager_button);
        if (alarmManagerButton != null) {
            alarmManagerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAlarmRunning = !isAlarmRunning;
                    setupAlarmButtonVisual();
                    setupTimerService();
                }
            });
        }
    }

    private void setupTimerService() {
        if (isAlarmRunning) {
            TimerService timerService = BaseApplication.getTimerService();
            if (timerService != null && !timerService.isRunning()) {
                timerService.startTimer((TextView) findViewById(R.id.bottom_bottom_sheet_title));
            }
        } else {
            TimerService timerService = BaseApplication.getTimerService();
            if (timerService != null && timerService.isRunning()) {
                timerService.pauseTimer();
            }
        }
    }

    private void setupAlarmButtonVisual() {
        final ImageButton alarmManagerButton = (ImageButton) findViewById(R.id.alarm_manager_button);
        final View bottomSheet = findViewById(R.id.bottom_sheet);
        if (alarmManagerButton == null || bottomSheet == null) {
            return;
        }

        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                if (isAlarmRunning) {
                    alarmManagerButton.setImageDrawable(ContextCompat.getDrawable(JarsActivity.this, R.drawable.ic_pause_white));

                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    behavior.setHideable(false);
                    behavior.setPeekHeight(bottomSheet.getHeight());
                } else {
                    alarmManagerButton.setImageDrawable(ContextCompat.getDrawable(JarsActivity.this, R.drawable.ic_play_arrow_white));
                    behavior.setHideable(true);
                }
            }
        });
    }

    private void handleCurrentView() {
        Fragment currentFragment = getFragmentFromFragmentManager(JarblePreferencesHelper.getCurrentView(this));
        switch (JarblePreferencesHelper.getCurrentView(this)) {
            case YEARLY:
                if (currentFragment instanceof YearlyFragment) {
                    return;
                }

                addContentFragment(WeeklyFragment.newInstance(), WeeklyFragment.TAG);
                addContentFragment(MonthlyFragment.newInstance(), MonthlyFragment.TAG);
                addContentFragment(YearlyFragment.newInstance(), YearlyFragment.TAG);
                break;
            case MONTHLY:
                if (currentFragment instanceof MonthlyFragment) {
                    return;
                }

                addContentFragment(YearlyFragment.newInstance(), YearlyFragment.TAG);
                addContentFragment(WeeklyFragment.newInstance(), WeeklyFragment.TAG);
                addContentFragment(MonthlyFragment.newInstance(), MonthlyFragment.TAG);
                break;
            case WEEKLY:
                if (currentFragment instanceof WeeklyFragment) {
                    return;
                }

                addContentFragment(MonthlyFragment.newInstance(), MonthlyFragment.TAG);
                addContentFragment(YearlyFragment.newInstance(), YearlyFragment.TAG);
                addContentFragment(WeeklyFragment.newInstance(), WeeklyFragment.TAG);
                break;
        }
    }

    private void handleCurrentViewWithAnimation(JarblePreferencesHelper.CurrentView previousView) {
        JarsFragment currentFragment = (JarsFragment) getFragmentFromFragmentManager(previousView);
        JarsFragment destinationFragment = (JarsFragment) getFragmentFromFragmentManager(JarblePreferencesHelper.getCurrentView(this));
        if (currentFragment == null || destinationFragment == null) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        switch (JarblePreferencesHelper.getCurrentView(this)) {
            case YEARLY:
                if (currentFragment instanceof YearlyFragment) {
                    return;
                }

                detachDestinationFragment(destinationFragment);

                fragmentTransaction = fragmentManager.beginTransaction();

                setupFragmentTransition(fragmentTransaction, destinationFragment, currentFragment);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    currentFragment.buildTransitionNames(fragmentTransaction,
                        JarsFragment.getSpanCount(YearlyFragment.getSpanSize(getResources())), destinationFragment);
                }

                showContentFragment(fragmentTransaction, currentFragment, destinationFragment);
                break;
            case MONTHLY:
                if (currentFragment instanceof MonthlyFragment) {
                    return;
                }

                detachDestinationFragment(destinationFragment);

                fragmentTransaction = fragmentManager.beginTransaction();

                setupFragmentTransition(fragmentTransaction, destinationFragment, currentFragment);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    currentFragment.buildTransitionNames(fragmentTransaction,
                        JarsFragment.getSpanCount(YearlyFragment.getSpanSize(getResources())), destinationFragment);
                }

                showContentFragment(fragmentTransaction, currentFragment, destinationFragment);
                break;
            case WEEKLY:
                if (currentFragment instanceof WeeklyFragment) {
                    return;
                }

                detachDestinationFragment(destinationFragment);

                fragmentTransaction = fragmentManager.beginTransaction();

                setupFragmentTransition(fragmentTransaction, destinationFragment, currentFragment);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    currentFragment.buildTransitionNames(fragmentTransaction,
                        JarsFragment.getSpanCount(YearlyFragment.getSpanSize(getResources())), destinationFragment);
                }

                showContentFragment(fragmentTransaction, currentFragment, destinationFragment);
                break;
        }
    }

    private void setupFragmentTransition(FragmentTransaction fragmentTransaction, Fragment newFragment, Fragment currentFragment) {
        if (fragmentTransaction == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition sharedElementTransition = TransitionInflater.from(this).inflateTransition(R.transition.enter_move);
            sharedElementTransition.setDuration(getResources().getInteger(R.integer.four_fifty_ms_duration));

            if (currentFragment != null) {
                currentFragment.setSharedElementReturnTransition(sharedElementTransition);
            }
            newFragment.setSharedElementEnterTransition(sharedElementTransition);
        }
    }

    @Nullable
    protected Fragment getFragmentFromFragmentManager(JarblePreferencesHelper.CurrentView previousView) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (CollectionUtils.isEmpty(fragments)) {
            return null;
        }

        for (Fragment fragment : fragments) {
            if (fragment != null && previousViewMatch(previousView, fragment)) {
                return fragment;
            }
        }
        return null;
    }

    private boolean previousViewMatch(JarblePreferencesHelper.CurrentView previousView, Fragment fragment) {
        switch (previousView) {
            case YEARLY:
                return fragment instanceof YearlyFragment;
            case MONTHLY:
                return fragment instanceof MonthlyFragment;
            case WEEKLY:
                return fragment instanceof WeeklyFragment;
        }
        return false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        isAlarmRunning = savedInstanceState.getBoolean(Extra.ALARM_RUNNING.name());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Extra.ALARM_RUNNING.name(), isAlarmRunning);

        super.onSaveInstanceState(outState);
    }
}