package com.wildsmith.jarble.ui.jars.weekly;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModelListener;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModelView;

class WeeklyDayRecyclerModelView extends LinearLayout implements
    DynamicRecyclerModelView<WeeklyDayRecyclerModel, DynamicRecyclerModelListener> {

    private TextView dayTextView;

    public WeeklyDayRecyclerModelView(Context context) {
        this(context, null);
    }

    public WeeklyDayRecyclerModelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeeklyDayRecyclerModelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.weekly_day_view, this);

        dayTextView = (TextView) findViewById(R.id.day);
    }

    @Override
    public void setListener(DynamicRecyclerModelListener listener) {
        //no listener necessary
    }

    @Override
    public void populateView(@NonNull WeeklyDayRecyclerModel model, int position, int size) {
        dayTextView.setText(model.getTitle());
    }

    @Override
    public void applyAnimation(int position, int size) {
        //no animating necessary
    }
}