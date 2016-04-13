package com.wildsmith.jarble.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Provides the structure for updating items in the table.
 */
public interface UpdateProvider {

    /**
     * Implement this to handle requests to update one or more rows. The implementation should update all rows matching the selection to
     * set
     * the columns according to the provided values map.
     *
     * @param context       a non null context that can be used to grab items from resources or perform context required tasks.
     * @param openHelper    that can be used to grab readable / writable instances of the database.
     * @param uri           The URI to query. This can potentially have a record ID if this is an update request for a specific record.
     * @param values        A set of column_name/value pairs to update in the database.
     * @param selection     An optional filter to match rows to update.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they
     *                      appear in the selection. The values will be bound as Strings.
     * @return the number of rows affected.
     */
    int update(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, ContentValues values, String selection,
        String[] selectionArgs);
}