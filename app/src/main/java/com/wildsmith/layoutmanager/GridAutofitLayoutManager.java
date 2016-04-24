package com.wildsmith.layoutmanager;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GridAutoFitLayoutManager extends GridLayoutManager {

    private static final int DEFAULT_SPAN_COUNT = 1;

    private int mColumnWidth;

    private boolean mColumnWidthChanged = true;

    public GridAutoFitLayoutManager(Context context, int columnWidth) {
        super(context, DEFAULT_SPAN_COUNT);
        setColumnWidth(columnWidth);
    }

    public void setColumnWidth(int newColumnWidth) {
        if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
            mColumnWidth = newColumnWidth;
            mColumnWidthChanged = true;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (mColumnWidthChanged && mColumnWidth > 0) {
            setSpanCount(LayoutManagerHelper.buildSpanCount(mColumnWidth, getOrientation(), getWidth(), getHeight(), getPaddingLeft(),
                getPaddingTop(), getPaddingRight(), getPaddingBottom()));
            mColumnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}