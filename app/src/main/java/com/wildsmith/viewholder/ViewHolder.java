package com.wildsmith.viewholder;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

class ViewHolder {

    private View root;

    private Map<Integer, View> viewMap = new HashMap<>();

    public ViewHolder(@NonNull View root) {
        this.root = root;
    }

    public void add(@IdRes int id, @NonNull View root) {
        viewMap.put(id, root.findViewById(id));
    }

    public void addAll(@NonNull Map<Integer, View> viewMap) {
        this.viewMap.putAll(viewMap);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <T extends View> T enhancedFindViewById(@IdRes int id) {
        if (!viewMap.containsKey(id)) {
            add(id, root);
        }

        return (T) viewMap.get(id);
    }
}