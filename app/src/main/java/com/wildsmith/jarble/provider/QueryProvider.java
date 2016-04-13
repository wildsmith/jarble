package com.wildsmith.jarble.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Provides the structure for query items in the table.
 */
public interface QueryProvider {

    /**
     * Implement this to handle query requests from clients.
     *
     * @param uri           The URI to query. This will be the full URI sent by the client; if the client is requesting a specific record,
     *                      the URI will end in a record number that the implementation should parse and add to a WHERE or HAVING clause,
     *                      specifying that _id value.
     * @param projection    The list of columns to put into the cursor.
     * @param selection     A selection criteria to apply when filtering rows.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they
     *                      appear in the selection. The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     * @return a Cursor or {@code null}.
     */
    @Nullable
    Cursor query(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, String[] projection, String selection,
        String[] selectionArgs, String sortOrder);
}