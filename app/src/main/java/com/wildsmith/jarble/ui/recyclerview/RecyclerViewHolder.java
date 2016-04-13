package com.wildsmith.jarble.ui.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RecyclerViewHolder<M extends RecyclerModel, V extends RecyclerModelView> extends RecyclerView.ViewHolder {

    protected V container;

    protected Context context;

    @SuppressWarnings("unchecked")
    public RecyclerViewHolder(@NonNull View view) {
        super(view);

        //This is the most we can do. An exception will be thrown if a view was inflated that implements ModelView BUT does
        // not implement the correct type as set by the type variable V.
        if (view instanceof RecyclerModelView) {
            container = (V) view;
        }

        context = view.getContext();
    }

    public View getView() {
        return (View) container;
    }

    /**
     * populateView populates the holders view using
     *
     * @param recyclerObject view object that should be used to populate the view
     * @param position       current item position within the adapter
     * @param size           number of items in the adapter
     */
    public abstract void populateView(M recyclerObject, int position, int size);
}