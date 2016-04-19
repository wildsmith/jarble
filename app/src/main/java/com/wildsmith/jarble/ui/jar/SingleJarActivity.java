package com.wildsmith.jarble.ui.jar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.wildsmith.jarble.BaseApplication;
import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableInteractionHelper;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.marble.MarbleTableInteractionHelper;
import com.wildsmith.jarble.marble.MarbleTableModel;
import com.wildsmith.jarble.preferences.JarblePreferencesHelper;
import com.wildsmith.jarble.timer.TimerService;
import com.wildsmith.jarble.ui.BaseActivity;
import com.wildsmith.jarble.ui.GenericDialogFragment;
import com.wildsmith.jarble.ui.jars.JarsModifiedBroadcastReceiver;
import com.wildsmith.utils.BroadcastHelper;

import java.io.ByteArrayOutputStream;

public class SingleJarActivity extends BaseActivity implements SingleJarFragment.Listener, GenericDialogFragment.Listener,
    MarbleInfoDialogFragment.Listener, AdapterView.OnItemSelectedListener {

    public enum Extra {
        SELECTED_COLOR,
        MODEL,
        MODE
    }

    public enum Mode {
        EDIT,
        VIEW;

        public static Mode fromInt(int stateInt) {
            for (Mode mode : Mode.values()) {
                if (mode.ordinal() == stateInt) {
                    return mode;
                }
            }

            return VIEW;
        }
    }

    private enum ColorCategories {
        RED,
        ORANGE,
        YELLOW,
        GREEN,
        BLUE,
        PURPLE;

        public static ColorCategories fromInt(int stateInt) {
            for (ColorCategories colorCategories : ColorCategories.values()) {
                if (colorCategories.ordinal() == stateInt) {
                    return colorCategories;
                }
            }

            return RED;
        }
    }

    private int selectedColor;

    private JarTableModel model;

    private Mode mode = Mode.VIEW;

    @Override
    protected int getContentLayout() {
        return R.layout.jarble_activity_layout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (mode) {
            case EDIT:
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.create_jar_menu, menu);
                setupToolbarSaveIcon(menu);
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupFieldsFromIntent();

        updateMarbleInfo();

        setupStatusBar();
        setupToolbar();

        createSingleJarFragment(savedInstanceState);
    }

    private void setupFieldsFromIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        model = intent.getParcelableExtra(Extra.MODEL.name());
        mode = (Mode) intent.getSerializableExtra(Extra.MODE.name());
    }

    private void createSingleJarFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            final Bundle bundle = (getIntent() != null) ? getIntent().getExtras() : null;
            replaceContentFragment(SingleJarFragment.newInstance(bundle), SingleJarFragment.TAG);
        }
    }

    private void updateMarbleInfo() {
        final TimerService timerService = BaseApplication.getTimerService();
        if (mode != Mode.EDIT || timerService == null || !timerService.isAlertStopped()) {
            return;
        }

        GenericDialogFragment dialogFragment = new GenericDialogFragment();
        dialogFragment.setContent(getString(R.string.achievement_title), getString(R.string.achievement_subtitle));
        dialogFragment.setListener(this);
        dialogFragment.show(getSupportFragmentManager(), GenericDialogFragment.TAG);
    }

    @Override
    public void onRightButtonClicked() {
        displayInfoDialog(MarbleTableModel.getInProgressMarble(model.getMarbles()));
    }

    private void displayInfoDialog(MarbleTableModel model) {
        MarbleInfoDialogFragment dialogFragment = new MarbleInfoDialogFragment();
        dialogFragment.setModel(model);
        dialogFragment.setListener(this);
        dialogFragment.show(getSupportFragmentManager(), MarbleInfoDialogFragment.TAG);
    }

    @Override
    public void onJarTableMarbleModelUpdated(MarbleTableModel model) {
        final boolean hadEditingMarbles = MarbleTableModel.hasEditingMarbles(this.model.getMarbles());

        this.model.update(model);

        //TODO check to make sure that when the marbles here are flipped the model pointer here is also flipped
        MarbleTableModel.flipInProgressOrEditingToDone(this.model.getMarbles(),
            JarblePreferencesHelper.getMarbleAchievementsAsInteger(this));

        MarbleTableInteractionHelper.updateMarbleTableModel(this, model);

        BroadcastHelper.sendBroadcast(this, new Intent(JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name()));

        if (hadEditingMarbles) {
            saveAndFinishActivity();
        }
    }

    @Override
    public void onLeftButtonClicked() {
        MarbleTableModel.flipInProgressToEditing(model.getMarbles());
        MarbleTableInteractionHelper.updateMarbleTableModels(this, model.getMarbles());

        BroadcastHelper.sendBroadcast(this, new Intent(JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name()));

        setupBottomSheet();
    }

    private void setupStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.medium_purple));
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            switch (mode) {
                case VIEW:
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setTitle(getString(R.string.toolbar_title_view));
                    break;
                case EDIT:
                    actionBar.setTitle(getString(R.string.toolbar_title_edit));
                    setupToolbarClearIcon(toolbar);
                    break;
            }
        }
    }

    private void setupToolbarClearIcon(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_clear_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleJarActivity.this);
                builder.setMessage(getString(R.string.remove_all_updates_message));
                builder.setPositiveButton(getString(R.string.remove_all_updates_continue_button),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                builder.setNegativeButton(getString(R.string.remove_all_updates_dismiss_button),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    private void setupToolbarSaveIcon(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.menu_save);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (MarbleTableModel.hasEditingMarbles(model.getMarbles())) {
                    displayInfoDialog(MarbleTableModel.getEditingMarble(model.getMarbles()));
                } else {
                    saveAndFinishActivity();
                }
                return true;
            }
        });
    }

    private void updateModelImage() {
        SingleJarView view = (SingleJarView) findViewById(R.id.single_jar_view);
        if (view != null) {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bm = view.getDrawingCache();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0, stream);
            byte[] byteArray = stream.toByteArray();
            view.setDrawingCacheEnabled(false);
            model.setImage(byteArray);
        }
    }

    private void saveAndFinishActivity() {
        updateModelImage();

        if (!MarbleTableModel.hasInProgressMarbles(this.model.getMarbles())) {
            this.model.setInProgress(false);
        } else {
            TimerService timerService = BaseApplication.getTimerService();
            if (timerService != null && !timerService.isRunning() && !timerService.isPaused()) {
                timerService.startTimer();
            }
        }
        JarTableInteractionHelper.updateJarTableModel(SingleJarActivity.this, this.model);

        BroadcastHelper.sendBroadcast(SingleJarActivity.this,
            new Intent(JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name()));
        finish();
    }

    @Override
    public void setupBottomSheetViews() {
        setupColorPaletteSpinner();
        setupBottomSheetHeader();
        setupBottomSheet();
    }

    private void setupBottomSheet() {
        final View bottomSheet = findViewById(R.id.bottom_sheet);
        final View toolbar = findViewById(R.id.toolbar);
        if (bottomSheet == null || toolbar == null) {
            return;
        }

        //Delay the initial setup until the toolbar has a chance to measure
        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                if (behavior != null) {
                    switch (mode) {
                        case EDIT:
                            if (MarbleTableModel.hasEditingMarbles(model.getMarbles())) {
                                behavior.setHideable(false);
                                behavior.setPeekHeight(toolbar.getHeight());
                                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                                    @Override
                                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                            bottomSheet.setBackgroundColor(ContextCompat.getColor(SingleJarActivity.this,
                                                android.R.color.white));
                                        }
                                    }

                                    @Override
                                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                                    }
                                });
                            } else {
                                behavior.setHideable(true);
                                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            }
                            break;
                        case VIEW:
                            behavior.setHideable(true);
                            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            break;
                    }
                }
            }
        });
    }

    private void setupBottomSheetHeader() {
        View bottomSheetHeader = findViewById(R.id.bottom_sheet_header);
        if (bottomSheetHeader == null) {
            return;
        }

        bottomSheetHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View bottomSheet = findViewById(R.id.bottom_sheet);
                if (bottomSheet == null) {
                    return;
                }

                final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                if (behavior != null) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    private void setupColorPaletteSpinner() {
        Spinner colorPaletteSpinner = (Spinner) findViewById(R.id.color_palette);
        if (colorPaletteSpinner == null) {
            return;
        }

        String[] colorCategories = getResources().getStringArray(R.array.colorCategories);
        colorPaletteSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colorCategories));

        colorPaletteSpinner.setOnItemSelectedListener(this);
    }

    private View.OnClickListener swatchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setViewImageDrawable(R.id.swatch_zero, null);
            setViewImageDrawable(R.id.swatch_one, null);
            setViewImageDrawable(R.id.swatch_two, null);
            setViewImageDrawable(R.id.swatch_three, null);
            setViewImageDrawable(R.id.swatch_four, null);
            setViewImageDrawable(R.id.swatch_five, null);
            setViewImageDrawable(R.id.swatch_six, null);
            setViewImageDrawable(R.id.swatch_seven, null);
            setViewImageDrawable(R.id.swatch_eight, null);

            if (selectedColor == (int) view.getTag()) {
                setViewImageDrawable(view.getId(), null);
                selectedColor = 0;
            } else {
                ((ImageView) view).setImageResource(R.drawable.ic_done_white_24dp);
                selectedColor = (int) view.getTag();
            }
        }
    };

    private void setViewColor(int resourceId, @ColorInt int color) {
        View view = findViewById(resourceId);
        if (view != null) {
            view.setBackgroundColor(color);
            view.setOnClickListener(swatchOnClickListener);
            view.setTag(color);

            if (selectedColor == color) {
                ((ImageView) view).setImageResource(R.drawable.ic_done_white_24dp);
            } else {
                setViewImageDrawable(resourceId, null);
            }
        }
    }

    private void setViewImageDrawable(int resouceId, Drawable drawable) {
        ImageView view = (ImageView) findViewById(resouceId);
        if (view != null) {
            view.setImageDrawable(drawable);
        }
    }

    @Override
    public void onEditableMarbleClicked(@NonNull MarbleTableModel model, @NonNull FloatingActionButton marbleView) {
        if (selectedColor != 0) {
            model.setColor(String.format("#%06X", (0xFFFFFF & selectedColor)));
            MarbleTableInteractionHelper.updateMarbleTableModel(this, model);
            this.model.update(model);
            BroadcastHelper.sendBroadcast(this, new Intent(JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name()));

            marbleView.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            return;
        }

        View bottomSheet = findViewById(R.id.bottom_sheet);
        View toolbar = findViewById(R.id.toolbar);
        if (bottomSheet == null || toolbar == null) {
            return;
        }

        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        if (behavior != null) {
            switch (mode) {
                case EDIT:
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    behavior.setPeekHeight(toolbar.getHeight());
                    behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                                bottomSheet.setBackgroundColor(ContextCompat.getColor(SingleJarActivity.this, android.R.color.white));
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int[] swatches = null;
        switch (ColorCategories.fromInt(position)) {
            case RED:
                swatches = getResources().getIntArray(R.array.red_swatches);
                break;
            case ORANGE:
                swatches = getResources().getIntArray(R.array.orange_swatches);
                break;
            case YELLOW:
                swatches = getResources().getIntArray(R.array.yellow_swatches);
                break;
            case GREEN:
                swatches = getResources().getIntArray(R.array.green_swatches);
                break;
            case BLUE:
                swatches = getResources().getIntArray(R.array.blue_swatches);
                break;
            case PURPLE:
                swatches = getResources().getIntArray(R.array.purple_swatches);
                break;
        }

        if (swatches != null && swatches.length == 9) {
            setViewColor(R.id.swatch_zero, swatches[0]);
            setViewColor(R.id.swatch_one, swatches[1]);
            setViewColor(R.id.swatch_two, swatches[2]);
            setViewColor(R.id.swatch_three, swatches[3]);
            setViewColor(R.id.swatch_four, swatches[4]);
            setViewColor(R.id.swatch_five, swatches[5]);
            setViewColor(R.id.swatch_six, swatches[6]);
            setViewColor(R.id.swatch_seven, swatches[7]);
            setViewColor(R.id.swatch_eight, swatches[8]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        selectedColor = savedInstanceState.getInt(Extra.SELECTED_COLOR.name());
        model = savedInstanceState.getParcelable(Extra.MODEL.name());
        mode = Mode.fromInt(savedInstanceState.getInt(Extra.MODE.name()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Extra.SELECTED_COLOR.name(), selectedColor);
        outState.putParcelable(Extra.MODEL.name(), model);
        outState.putInt(Extra.MODE.name(), mode.ordinal());

        super.onSaveInstanceState(outState);
    }
}