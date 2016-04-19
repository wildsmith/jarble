package com.wildsmith.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Provides the structure for deleting items from a table.
 */
public interface DeleteProvider {

    /**
     * Implement this to handle requests to delete one or more rows. The implementation should apply the selection clause when performing
     * deletion, allowing the operation to affect multiple rows in a directory.
     *
     * @param context       a non null context that can be used to grab items from resources or perform context required tasks.
     * @param openHelper    that can be used to grab readable / writable instances of the database.
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     A selection criteria to apply when filtering rows.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they
     *                      appear in the selection. The values will be bound as Strings.
     * @return The number of rows affected.
     */
    int delete(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, String selection, String[] selectionArgs);
}