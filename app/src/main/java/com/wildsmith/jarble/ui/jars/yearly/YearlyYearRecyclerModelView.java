package com.wildsmith.jarble.ui.jars.yearly;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModelListener;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModelView;

class YearlyYearRecyclerModelView extends LinearLayout implements
    DynamicRecyclerModelView<YearlyYearRecyclerModel, DynamicRecyclerModelListener> {

    private TextView yearTextView;

    public YearlyYearRecyclerModelView(Context context) {
        this(context, null);
    }

    public YearlyYearRecyclerModelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearlyYearRecyclerModelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.yearly_year_view, this);

        yearTextView = (TextView) findViewById(R.id.year);
    }

    @Override
    public void setListener(DynamicRecyclerModelListener listener) {
        //no listener necessary
    }

    @Override
    public void populateView(@NonNull YearlyYearRecyclerModel model, int position, int size) {
        yearTextView.setText(model.getTitle());

        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) getLayoutParams();
        layoutParams.setFullSpan(true);
    }

    @Override
    public void applyAnimation(int position, int size) {
        //no animating necessary
    }
}