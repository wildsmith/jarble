package com.wildsmith.recyclerview.dynamic;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wildsmith.recyclerview.RecyclerAdapter;

import java.lang.ref.WeakReference;

/**
 * GenericRecyclerAdapter handles the inflation of {@link DynamicRecyclerModelView}s
 */
public class DynamicRecyclerAdapter<M extends DynamicRecyclerModel, V extends DynamicRecyclerModelView> extends
    RecyclerAdapter<M, V, DynamicRecyclerViewHolder<M, V>> {

    protected WeakReference<DynamicRecyclerModelListener> listenerWeakReference;

    public DynamicRecyclerAdapter() {

    }

    public DynamicRecyclerAdapter(DynamicRecyclerModelListener listener) {
        this.listenerWeakReference = new WeakReference<>(listener);
    }

    @Override
    public DynamicRecyclerViewHolder<M, V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DynamicRecyclerViewHolder<>(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false),
            getListener());
    }

    @Override
    public int getItemViewType(int position) {
        DynamicRecyclerModel viewItem = getRecyclerObject(position);
        return (viewItem == null) ? -1 : viewItem.getLayoutId();
    }

    @Nullable
    public DynamicRecyclerModelListener getListener() {
        if (listenerWeakReference == null) {
            return null;
        }

        return listenerWeakReference.get();
    }
}