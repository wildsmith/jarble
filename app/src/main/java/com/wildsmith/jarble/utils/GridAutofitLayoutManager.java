package com.wildsmith.jarble.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GridAutofitLayoutManager extends GridLayoutManager {

    private static final int DEFAULT_SPAN_COUNT = 1;

    private int mColumnWidth;

    private boolean mColumnWidthChanged = true;

    public GridAutofitLayoutManager(Context context, int columnWidth) {
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
            setSpanCount(buildSpanCount(mColumnWidth, getOrientation(), getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(),
                getPaddingRight(), getPaddingBottom()));
            mColumnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }

    public static int buildSpanCount(int columnWidth, int orientation, int width, int height, int paddingLeft, int paddingTop,
        int paddingRight, int paddingBottom) {
        int totalSpace;
        if (orientation == VERTICAL) {
            totalSpace = width - paddingRight - paddingLeft;
        } else {
            totalSpace = height - paddingTop - paddingBottom;
        }
        return Math.max(1, totalSpace / columnWidth);
    }
}