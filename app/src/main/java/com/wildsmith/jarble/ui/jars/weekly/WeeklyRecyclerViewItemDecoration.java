package com.wildsmith.jarble.ui.jars.weekly;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class WeeklyRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public WeeklyRecyclerViewItemDecoration(@NonNull Resources resources, @DimenRes int spaceResId) {
        this.space = (int) resources.getDimension(spaceResId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = space;
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
    }
}