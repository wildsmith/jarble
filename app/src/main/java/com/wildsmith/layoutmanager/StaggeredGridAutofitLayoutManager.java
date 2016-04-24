package com.wildsmith.layoutmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewTreeObserver;

public class StaggeredGridAutoFitLayoutManager extends StaggeredGridLayoutManager {

    private static final int DEFAULT_SPAN_COUNT = 1;

    private int mColumnWidth;

    private boolean mColumnWidthChanged = true;

    public StaggeredGridAutoFitLayoutManager(@NonNull final Listener listener, @NonNull final RecyclerView recyclerView, int columnWidth) {
        super(DEFAULT_SPAN_COUNT, VERTICAL);
        setColumnWidth(columnWidth);
        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (mColumnWidthChanged && mColumnWidth > 0) {
                    final int spanCount = LayoutManagerHelper.buildSpanCount(mColumnWidth, getOrientation(), getWidth(), getHeight(),
                        getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
                    setSpanCount(spanCount);
                    listener.onSpanCountSet(spanCount);
                    mColumnWidthChanged = false;
                }
                return false;
            }
        });
    }

    public void setColumnWidth(int newColumnWidth) {
        if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
            mColumnWidth = newColumnWidth;
            mColumnWidthChanged = true;
        }
    }

    public interface Listener {

        void onSpanCountSet(int spanCount);
    }
}