package com.wildsmith.jarble.ui.jars.monthly;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModelListener;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModelView;

public class MonthlyMonthRecyclerModelView extends LinearLayout implements
    DynamicRecyclerModelView<MonthlyMonthRecyclerModel, DynamicRecyclerModelListener> {

    private TextView monthTextView;

    public MonthlyMonthRecyclerModelView(Context context) {
        this(context, null);
    }

    public MonthlyMonthRecyclerModelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthlyMonthRecyclerModelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.monthly_month_view, this);

        monthTextView = (TextView) findViewById(R.id.month);
    }

    @Override
    public void setListener(DynamicRecyclerModelListener listener) {
        //no listener necessary
    }

    @Override
    public void populateView(@NonNull MonthlyMonthRecyclerModel model, int position, int size) {
        monthTextView.setText(model.getTitle());
    }

    @Override
    public void applyAnimation(int position, int size) {
        //no animating necessary
    }
}
