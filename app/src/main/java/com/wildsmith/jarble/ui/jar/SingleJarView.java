package com.wildsmith.jarble.ui.jar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.provider.jar.JarTableMarbleModel;
import com.wildsmith.jarble.provider.jar.JarTableModel;

import java.lang.ref.WeakReference;

public class SingleJarView extends RelativeLayout implements View.OnClickListener {

    private JarTableModel model;

    private WeakReference<Listener> listenerWeakReference;

    public SingleJarView(Context context) {
        this(context, null);
    }

    public SingleJarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleJarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.jarble_single_jar_layout, this);

        setupOnClickListener();
    }

    private void setupOnClickListener() {
        findViewById(R.id.marble_zero).setOnClickListener(this);
        findViewById(R.id.marble_one).setOnClickListener(this);
        findViewById(R.id.marble_two).setOnClickListener(this);
        findViewById(R.id.marble_three).setOnClickListener(this);
        findViewById(R.id.marble_four).setOnClickListener(this);
        findViewById(R.id.marble_five).setOnClickListener(this);
        findViewById(R.id.marble_six).setOnClickListener(this);
        findViewById(R.id.marble_seven).setOnClickListener(this);
        findViewById(R.id.marble_eight).setOnClickListener(this);
        findViewById(R.id.marble_nine).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!(view instanceof FloatingActionButton)) {
            return;
        }

        JarTableMarbleModel marbleModel = getMarbleModel(view.getId());
        if (marbleModel == null) {
            return;
        }

        if (listenerWeakReference != null && listenerWeakReference.get() != null) {
            listenerWeakReference.get().onMarbleClicked(marbleModel, (FloatingActionButton) view);
        }
    }

    public void setListener(Listener listener) {
        this.listenerWeakReference = new WeakReference<>(listener);
    }

    public void buildViewUsingModel(JarTableModel model) {
        this.model = model;

        setupDisabledView();
    }

    private void setupDisabledView() {
        for (int index = 0; index < JarTableMarbleModel.MAX_MARBLE_COUNT; index++) {
            JarTableMarbleModel marbleModel = model.getMarbles()[index];
            FloatingActionButton marble = (FloatingActionButton) findViewById(getMarbleViewId(index));
            if (marble == null) {
                continue;
            }

            switch (JarTableMarbleModel.State.fromInt(marbleModel.getState())) {
                case IN_PROGRESS:
                    marble.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_update_black));
                    marble.setEnabled(true);
                    marble.setClickable(true);
                    break;
                case DONE:
                case EDITING:
                    marble.setImageDrawable(null);
                    final String color = marbleModel.getColor();
                    if (!TextUtils.isEmpty(color)) {
                        marble.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                    }
                    marble.setEnabled(true);
                    marble.setClickable(true);
                    break;
                default:
                    marble.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.grey)));
                    marble.setImageResource(R.drawable.icon_puzzle_piece_grey);
                    marble.setEnabled(false);
                    marble.setClickable(false);
                    break;
            }
        }
    }

    @Nullable
    private JarTableMarbleModel getMarbleModel(int viewId) {
        switch (viewId) {
            case R.id.marble_zero:
                return model.getMarbles()[0];
            case R.id.marble_one:
                return model.getMarbles()[1];
            case R.id.marble_two:
                return model.getMarbles()[2];
            case R.id.marble_three:
                return model.getMarbles()[3];
            case R.id.marble_four:
                return model.getMarbles()[4];
            case R.id.marble_five:
                return model.getMarbles()[5];
            case R.id.marble_six:
                return model.getMarbles()[6];
            case R.id.marble_seven:
                return model.getMarbles()[7];
            case R.id.marble_eight:
                return model.getMarbles()[8];
            case R.id.marble_nine:
                return model.getMarbles()[9];
        }

        return null;
    }

    @IdRes
    private static int getMarbleViewId(int marblePosition) {
        switch (marblePosition) {
            case 0:
                return R.id.marble_zero;
            case 1:
                return R.id.marble_one;
            case 2:
                return R.id.marble_two;
            case 3:
                return R.id.marble_three;
            case 4:
                return R.id.marble_four;
            case 5:
                return R.id.marble_five;
            case 6:
                return R.id.marble_six;
            case 7:
                return R.id.marble_seven;
            case 8:
                return R.id.marble_eight;
            case 9:
                return R.id.marble_nine;
        }

        return 0;
    }

    interface Listener {

        void onMarbleClicked(@NonNull JarTableMarbleModel model, @NonNull FloatingActionButton marbleView);
    }
}
