package com.wildsmith.jarble.ui.recyclerview.dynamic;

import android.support.annotation.LayoutRes;

import com.wildsmith.jarble.ui.recyclerview.RecyclerModel;

/**
 * GenericRecyclerViewItem interface used to enforce a generic structure onto the implementers of this interface which is used by the {@link
 * DynamicRecyclerAdapter}
 */
public interface DynamicRecyclerModel extends RecyclerModel {

    @LayoutRes
    int getLayoutId();
}