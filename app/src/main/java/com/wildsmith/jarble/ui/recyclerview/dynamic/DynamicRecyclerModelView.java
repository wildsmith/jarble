package com.wildsmith.jarble.ui.recyclerview.dynamic;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;

import com.wildsmith.jarble.ui.recyclerview.RecyclerModelView;

/**
 * GenericRecyclerViewItemView is an interface that all recycler views should use to ensure they have the mandatory methods for view
 * population by {@link DynamicRecyclerViewHolder}
 */
public interface DynamicRecyclerModelView<M extends DynamicRecyclerModel, L extends DynamicRecyclerModelListener>
    extends RecyclerModelView {

    void populateView(@NonNull M model, int position, int size);

    @TargetApi(21)
    void applyAnimation(int position, int size);

    void setListener(L listener);
}