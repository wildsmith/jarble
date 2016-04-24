package com.wildsmith.layoutmanager;

import android.support.v7.widget.OrientationHelper;

public class LayoutManagerHelper {

    public static int buildSpanCount(int columnWidth, int orientation, int width, int height, int paddingLeft, int paddingTop,
        int paddingRight, int paddingBottom) {
        int totalSpace;
        if (orientation == OrientationHelper.VERTICAL) {
            totalSpace = width - paddingRight - paddingLeft;
        } else {
            totalSpace = height - paddingTop - paddingBottom;
        }
        return Math.max(1, totalSpace / columnWidth);
    }
}