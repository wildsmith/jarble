package com.wildsmith.jarble.ui.jars.monthly;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.wildsmith.jarble.ui.viewholder.ViewHolderWrapper;

import java.util.Map;

class MonthlyViewWrapper extends ViewHolderWrapper {

    private static final Class<?> CLAZZ = MonthlyViewWrapper.class;

    private MonthlyViewWrapper() {
        //Singleton pattern
    }

    public static View onCreateView(@NonNull View root) {
        return onCreateView(CLAZZ, root);
    }

    public static void onDestroyView() {
        onDestroyView(CLAZZ);
    }

    public static void add(@IdRes int id, @NonNull View root) {
        add(CLAZZ, id, root);
    }

    public static void addAll(@NonNull Map<Integer, View> viewMap) {
        addAll(CLAZZ, viewMap);
    }

    @Nullable
    public static <T extends View> T findViewById(@IdRes int id) {
        return findViewById(CLAZZ, id);
    }
}