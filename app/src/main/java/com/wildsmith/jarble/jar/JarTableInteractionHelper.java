package com.wildsmith.jarble.jar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wildsmith.jarble.jar.provider.JarTableProviderUri;

import java.util.ArrayList;
import java.util.List;

public class JarTableInteractionHelper {

    private static final String TAG = JarTableInteractionHelper.class.getSimpleName();

    static List<JarTableModel> getJarTableModelsForYearInternal(@NonNull Context context, @NonNull String year) {
        Uri responses = Uri.parse(JarTableProviderUri.URL + JarTableProviderUri.UriCode.JARS_FOR_TIME_PERIOD.getPath());

        List<JarTableModel> storedModels = new ArrayList<>(8);
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(responses, null, createQueryWhereClause(), createQueryWhereArgs(year), null);
            if (cursor == null || !cursor.moveToFirst() || cursor.getCount() == 0) {
                return null;
            }

            while (!cursor.isAfterLast()) {
                storedModels.add(new JarTableModel.Builder()
                    .setTimeStamp(cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.TIME_STAMP.title())))
                    .setInProgress(cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.IN_PROGRESS.title())) != 0)
                    .setImage(cursor.getBlob(cursor.getColumnIndex(JarTableStructure.Column.IMAGE.title())))
                    .build(context, 0));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.d(TAG, "Couldn't grab list form database, this is likely due to the database recently being deleted.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return storedModels;
    }

    @NonNull
    private static String createQueryWhereClause() {
        return JarTableStructure.Column.TIME_STAMP + " BETWEEN ? AND ?";
    }

    @NonNull
    private static String[] createQueryWhereArgs(@NonNull String year) {
        return new String[]{year + "-01-01", year + "-12-31"};
    }

    public static void insertJarTableModel(@NonNull Context context, @NonNull JarTableModel tableModel) {
        context.getContentResolver().insert(Uri.parse(JarTableProviderUri.URL + JarTableProviderUri.UriCode.JAR.getPath()),
            tableModel.toContentValues());
    }

    public static void updateJarTableModel(@NonNull Context context, @NonNull JarTableModel tableModel) {
        context.getContentResolver().update(Uri.parse(JarTableProviderUri.URL + JarTableProviderUri.UriCode.JAR.getPath()),
            tableModel.toContentValues(), createUpdateWhereClause(), createUpdateWhereArgs(tableModel));
    }

    private static String createUpdateWhereClause() {
        return JarTableStructure.Column.TIME_STAMP + " = ?";
    }

    private static String[] createUpdateWhereArgs(@NonNull JarTableModel model) {
        return new String[]{model.getTimestamp()};
    }
}
