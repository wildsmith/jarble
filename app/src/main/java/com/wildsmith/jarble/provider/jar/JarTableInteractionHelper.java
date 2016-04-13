package com.wildsmith.jarble.provider.jar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wildsmith.jarble.provider.jar.provider.JarTableProviderUri;

import java.util.ArrayList;
import java.util.List;

public class JarTableInteractionHelper {

    private static final String TAG = JarTableInteractionHelper.class.getSimpleName();

    static List<JarTableModel> getJarTableModelsForYearInternal(@NonNull Context context, @NonNull String year) {
        Uri responses = Uri.parse(JarTableProviderUri.URL + JarTableProviderUri.UriCode.JARS_FOR_YEAR.getPath());

        List<JarTableModel> storedModels = new ArrayList<>(8);
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(responses, null, createYearSelection(), createYearSelectionArgs(year), null);
            if (cursor == null || !cursor.moveToFirst() || cursor.getCount() == 0) {
                return null;
            }

            while (!cursor.isAfterLast()) {
                storedModels.add(new JarTableModel.Builder()
                    .setYear(cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.YEAR.title())))
                    .setMonth(cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MONTH.title())))
                    .setWeekOfMonth(cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.WEEK_OF_MONTH.title())))
                    .setDayOfMonth(cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.DAY_OF_MONTH.title())))
                    .setDayOfWeek(cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.DAY_OF_WEEK.title())))
                    .setTimestamp(cursor.getLong(cursor.getColumnIndex(JarTableStructure.Column.TIME_STAMP.title())))
                    .setInProgress(cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.IN_PROGRESS.title())) != 0)
                    .setImage(cursor.getBlob(cursor.getColumnIndex(JarTableStructure.Column.IMAGE.title())))
                    .setMarbles(buildMarblesArray(cursor))
                    .build(0));

                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.d(TAG, "Couldn't grab account list form database, this is likely due to the database recently being deleted.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return storedModels;
    }

    static JarTableMarbleModel[] buildMarblesArray(Cursor cursor) {
        JarTableMarbleModel[] marbles = new JarTableMarbleModel[JarTableMarbleModel.MAX_MARBLE_COUNT];

        marbles[0] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ZERO_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ZERO_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ZERO_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ZERO_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ZERO_PERFORMANCE_NOTES.title())));
        marbles[1] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ONE_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ONE_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ONE_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ONE_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_ONE_PERFORMANCE_NOTES.title())));
        marbles[2] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_TWO_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_TWO_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_TWO_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_TWO_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_TWO_PERFORMANCE_NOTES.title())));
        marbles[3] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_THREE_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_THREE_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_THREE_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_THREE_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_THREE_PERFORMANCE_NOTES.title())));
        marbles[4] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FOUR_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FOUR_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FOUR_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FOUR_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FOUR_PERFORMANCE_NOTES.title())));
        marbles[5] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FIVE_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FIVE_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FIVE_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FIVE_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_FIVE_PERFORMANCE_NOTES.title())));
        marbles[6] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SIX_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SIX_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SIX_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SIX_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SIX_PERFORMANCE_NOTES.title())));
        marbles[7] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SEVEN_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SEVEN_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SEVEN_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SEVEN_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_SEVEN_PERFORMANCE_NOTES.title())));
        marbles[8] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_EIGHT_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_EIGHT_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_EIGHT_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_EIGHT_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_EIGHT_PERFORMANCE_NOTES.title())));
        marbles[9] = new JarTableMarbleModel(
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_NINE_ID.title())),
            cursor.getInt(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_NINE_STATE.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_NINE_COLOR.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_NINE_PURPOSE_NOTES.title())),
            cursor.getString(cursor.getColumnIndex(JarTableStructure.Column.MARBLE_NINE_PERFORMANCE_NOTES.title())));

        return marbles;
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
        return new String[]{Long.toString(model.getTimestamp())};
    }

    @NonNull
    private static String createYearSelection() {
        return JarTableStructure.Column.YEAR + " = ?";
    }

    @NonNull
    private static String[] createYearSelectionArgs(@NonNull String year) {
        return new String[]{year};
    }
}
