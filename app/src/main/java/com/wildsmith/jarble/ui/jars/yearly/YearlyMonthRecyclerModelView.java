package com.wildsmith.jarble.ui.jars.yearly;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.ui.jars.JarViewRecyclerModelListener;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModelView;

class YearlyMonthRecyclerModelView extends LinearLayout implements
    DynamicRecyclerModelView<YearlyMonthRecyclerModel, JarViewRecyclerModelListener> {

    private TextView monthTextView;

    private GridView monthJarsGridView;

    private YearlyMonthGridViewAdapter adapter;

    private JarViewRecyclerModelListener listener;

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
        monthJarsGridView = (GridView) findViewById(R.id.month_jars);

        adapter = new YearlyMonthGridViewAdapter(context);
    }

    @Override
    public void setListener(JarViewRecyclerModelListener listener) {
        this.listener = listener;
    }

    @Override
    public void populateView(@NonNull YearlyMonthRecyclerModel model, int position, int size) {
        monthTextView.setText(model.getTitle());

        adapter.setJars(model.getJars());
        adapter.setListener(listener);
        monthJarsGridView.setAdapter(adapter);
    }

    @Override
    public void applyAnimation(int position, int size) {
        //no animating necessary
    }
}
