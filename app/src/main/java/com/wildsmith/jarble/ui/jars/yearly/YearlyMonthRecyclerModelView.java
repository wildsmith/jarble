package com.wildsmith.jarble.ui.jars.yearly;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModelListener;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModelView;

class YearlyMonthRecyclerModelView extends LinearLayout implements
    DynamicRecyclerModelView<YearlyMonthRecyclerModel, DynamicRecyclerModelListener> {

    private TextView monthTextView;

    public YearlyMonthRecyclerModelView(Context context) {
        this(context, null);
    }

    public YearlyMonthRecyclerModelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearlyMonthRecyclerModelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.yearly_month_view, this);

        monthTextView = (TextView) findViewById(R.id.month);
    }

    @Override
    public void setListener(DynamicRecyclerModelListener listener) {
        //no listener necessary
    }

    @Override
    public void populateView(@NonNull final YearlyMonthRecyclerModel model, int position, int size) {
        monthTextView.setText(model.getTitle());
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                setMinimumHeight(model.getHeight());
                return true;
            }
        });
    }

    @Override
    public void applyAnimation(int position, int size) {
        //no animating necessary
    }
}
