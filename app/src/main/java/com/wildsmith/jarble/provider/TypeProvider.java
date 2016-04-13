package com.wildsmith.jarble.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Provides the structure for getting the MIME type for an item from a table.
 */
public interface TypeProvider {

    /**
     * Implement this to handle requests for the MIME type of the data at the given URI.  The returned MIME type should start with
     * <code>vnd.android.cursor.item</code> for a single record, or <code>vnd.android.cursor.dir/</code> for multiple items.
     *
     * @param context    a non null context that can be used to grab items from resources or perform context required tasks.
     * @param openHelper that can be used to grab readable / writable instances of the database.
     * @param uri        the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    String getType(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri);
}