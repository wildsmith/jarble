package com.wildsmith.jarble.provider.jar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wildsmith.jarble.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class JarTableInMemoryCache {

    private static final List<JarTableModel> jarTableModelsForYear = new ArrayList<>();

    public static void invalidateJarTableModels() {
        synchronized (jarTableModelsForYear) {
            jarTableModelsForYear.clear();
        }
    }

    public static JarTableModel getInProgressJarTableModel(@NonNull Context context) {
        List<JarTableModel> tableModels = getJarTableModelsForYear(context, "");
        if (CollectionUtils.isEmpty(tableModels)) {
            return null;
        }

        for (JarTableModel tableModel : tableModels) {
            if (tableModel.isInProgress()) {
                return tableModel;
            }
        }

        return null;
    }

    @Nullable
    public static JarTableModel getUpdatedJarTableModel(@NonNull Context context, @NonNull JarTableModel model) {
        List<JarTableModel> tableModels = getJarTableModelsForYear(context, "" + model.getYear());
        if (CollectionUtils.isEmpty(tableModels)) {
            return model;
        }

        for (JarTableModel tableModel : tableModels) {
            if (tableModel.getTimestamp() == model.getTimestamp()) {
                return tableModel;
            }
        }

        return model;
    }

    @Nullable
    public static List<JarTableModel> getJarTableModelsForYear(@NonNull Context context, @NonNull String year) {
        synchronized (jarTableModelsForYear) {
            if (CollectionUtils.isEmpty(jarTableModelsForYear)) {
                List<JarTableModel> models = JarTableInteractionHelper.getJarTableModelsForYearInternal(context, year);
                if (models != null) {
                    jarTableModelsForYear.addAll(models);
                }
            }

            return jarTableModelsForYear;
        }
    }
}
