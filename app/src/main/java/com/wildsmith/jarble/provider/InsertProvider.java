package com.wildsmith.jarble.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Provides the structure for inserting an item into the table.
 */
public interface InsertProvider {

    /**
     * Implement this to handle requests to insert a new row.
     *
     * @param context    a non null context that can be used to grab items from resources or perform context required tasks.
     * @param openHelper that can be used to grab readable / writable instances of the database.
     * @param uri        The content:// URI of the insertion request. This must not be {@code null}.
     * @param values     A set of column_name/value pairs to add to the database.
     * @return The URI for the newly inserted item.
     */
    Uri insert(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, ContentValues values);
}