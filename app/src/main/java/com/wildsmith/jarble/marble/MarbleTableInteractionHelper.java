package com.wildsmith.jarble.marble;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.jar.JarTableStructure;
import com.wildsmith.jarble.marble.provider.MarbleTableProviderUri;

public class MarbleTableInteractionHelper {

    private static final String TAG = MarbleTableInteractionHelper.class.getSimpleName();

    public static MarbleTableModel[] getMarbleTableModelsForJarInternal(@NonNull Context context, @NonNull JarTableModel model) {
        Uri responses = Uri.parse(MarbleTableProviderUri.URL + MarbleTableProviderUri.UriCode.MARBLES.getPath());

        MarbleTableModel[] marbles = new MarbleTableModel[MarbleTableModel.MAX_MARBLE_COUNT];
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(responses, null, createQueryWhereClause(), createQueryWhereArgs(model), null);
            if (cursor == null || !cursor.moveToFirst() || cursor.getCount() == 0) {
                return null;
            }

            int index = 0;
            while (!cursor.isAfterLast()) {
                marbles[index] = new MarbleTableModel(
                    cursor.getString(cursor.getColumnIndex(MarbleTableStructure.Column.TIME_STAMP.title())),
                    cursor.getInt(cursor.getColumnIndex(MarbleTableStructure.Column.NUMBER.title())),
                    cursor.getInt(cursor.getColumnIndex(MarbleTableStructure.Column.STATE.title())),
                    cursor.getString(cursor.getColumnIndex(MarbleTableStructure.Column.COLOR.title())),
                    cursor.getString(cursor.getColumnIndex(MarbleTableStructure.Column.PURPOSE_NOTES.title())),
                    cursor.getString(cursor.getColumnIndex(MarbleTableStructure.Column.PERFORMANCE_NOTES.title())));
                cursor.moveToNext();
                index++;
            }
        } catch (Exception e) {
            Log.d(TAG, "Couldn't grab list form database, this is likely due to the database recently being deleted.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return marbles;
    }

    @NonNull
    private static String createQueryWhereClause() {
        return JarTableStructure.Column.TIME_STAMP + " = ?";
    }

    @NonNull
    private static String[] createQueryWhereArgs(@NonNull JarTableModel model) {
        return new String[]{model.getTimestamp()};
    }

    public static void insertMarbleTableModel(@NonNull Context context, @NonNull MarbleTableModel marbleModel) {
        context.getContentResolver().insert(Uri.parse(MarbleTableProviderUri.URL + MarbleTableProviderUri.UriCode.MARBLE.getPath()),
            marbleModel.toContentValues());
    }

    public static void insertMarbleTableModels(@NonNull Context context, @NonNull MarbleTableModel[] marbleModels) {
        for (MarbleTableModel marbleModel : marbleModels) {
            if (marbleModel != null) {
                insertMarbleTableModel(context, marbleModel);
            }
        }
    }

    public static void updateMarbleTableModel(@NonNull Context context, @NonNull MarbleTableModel marbleModel) {
        context.getContentResolver().update(Uri.parse(MarbleTableProviderUri.URL + MarbleTableProviderUri.UriCode.MARBLE.getPath()),
            marbleModel.toContentValues(), createUpdateWhereClause(), createUpdateWhereArgs(marbleModel));
    }

    public static void updateMarbleTableModels(@NonNull Context context, @NonNull MarbleTableModel[] marbleModels) {
        for (MarbleTableModel marbleModel : marbleModels) {
            if (marbleModel != null) {
                updateMarbleTableModel(context, marbleModel);
            }
        }
    }

    private static String createUpdateWhereClause() {
        return MarbleTableStructure.Column.TIME_STAMP + " = ? AND " + MarbleTableStructure.Column.NUMBER + " = ?";
    }

    private static String[] createUpdateWhereArgs(@NonNull MarbleTableModel model) {
        return new String[]{model.getTimestamp(), "" + model.getNumber()};
    }
}