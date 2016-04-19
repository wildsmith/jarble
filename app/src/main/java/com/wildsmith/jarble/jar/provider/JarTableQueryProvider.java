package com.wildsmith.jarble.jar.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wildsmith.jarble.jar.JarTableStructure;
import com.wildsmith.provider.QueryProvider;

public class JarTableQueryProvider extends JarTableProviderUri implements QueryProvider {

    @Nullable
    @Override
    public Cursor query(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, String[] projection,
        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(JarTableStructure.TABLE_NAME);

        switch (UriCode.fromCode(URI_MATCHER.match(uri))) {
            case JARS_FOR_TIME_PERIOD:
                break;
            case JAR:
                break;
        }

        sortOrder = JarTableStructure.Column.TIME_STAMP.title() + " DESC";

        Cursor cursor = query(openHelper, queryBuilder, projection, selection, selectionArgs, sortOrder);
        if (cursor == null) {
            return null;
        }

        cursor.setNotificationUri(context.getContentResolver(), uri);

        return cursor;
    }

    /**
     * Queries the readable database returned by {@link SQLiteOpenHelper} using the given {@link SQLiteQueryBuilder}, projection,
     * selection, selectionArgs and sort order.
     *
     * @return a {@link Cursor} with the information found/not found during the query.
     */
    @Nullable
    private Cursor query(@NonNull SQLiteOpenHelper openHelper, @NonNull SQLiteQueryBuilder queryBuilder, String[] projection,
        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = openHelper.getReadableDatabase();
        if (database == null) {
            return null;
        }

        return queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
    }
}