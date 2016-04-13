package com.wildsmith.jarble.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wildsmith.jarble.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerAdapter<M extends RecyclerModel, V extends RecyclerModelView, P extends RecyclerViewHolder<M, V>> extends
    RecyclerView.Adapter<P> {

    protected List<M> recyclerModels = new ArrayList<>(8);

    @Override
    public abstract P onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(P holder, int position) {
        M recyclerObject = getRecyclerObject(position);
        if (recyclerObject == null) {
            return;
        }

        final int size = getItemCount();
        holder.populateView(recyclerObject, position, size);
    }

    @Override
    public int getItemCount() {
        if (recyclerModels == null) {
            return 0;
        }

        return recyclerModels.size();
    }

    public void addRecyclerObject(M recyclerObject) {
        addRecyclerObject(recyclerModels.size(), recyclerObject);
    }

    public void addRecyclerObject(int position, M recyclerObject) {
        recyclerModels.add(position, recyclerObject);
        if (recyclerModels.size() == 1) {
            //http://stackoverflow.com/a/32535796
            //if the size of the recycler objects is 1, meaning this is now the only item within the list of recyclerModels then call
            // notifyDataSetChanged() vs notifyItemInserted()
            notifyDataSetChanged();
        } else {
            notifyItemInserted(position);
        }
    }

    public void addRecyclerObjects(List<M> recyclerObjects) {
        addRecyclerObjects(this.recyclerModels.size(), recyclerObjects);
    }

    public void addRecyclerObjects(int position, List<M> recyclerObjects) {
        this.recyclerModels.addAll(position, recyclerObjects);
        notifyItemRangeInserted(position, recyclerObjects.size());
    }

    public void removeRecyclerObject(M recyclerObject) {
        int position = recyclerModels.indexOf(recyclerObject);
        if (position == -1) {
            return;
        }

        removeRecyclerObject(position);
    }

    public void removeRecyclerObject(int position) {
        recyclerModels.remove(position);
        notifyItemRemoved(position);
    }

    public void updateRecyclerObject(int position, M recyclerObject) {
        if (CollectionUtils.isValidIndex(recyclerModels, position)) {
            recyclerModels.remove(position);
            recyclerModels.add(position, recyclerObject);
            notifyItemChanged(position);
        }
    }

    public M getRecyclerObject(int position) {
        if (recyclerModels.size() <= position || position < 0) {
            return null;
        }

        return recyclerModels.get(position);
    }

    @NonNull
    public List<M> getRecyclerModels() {
        return recyclerModels;
    }

    public void setRecyclerModels(@NonNull List<M> recyclerModels) {
        this.recyclerModels = recyclerModels;
        notifyDataSetChanged();
    }

    public void clearRecyclerObjects() {
        this.recyclerModels.clear();
        notifyDataSetChanged();
    }
}