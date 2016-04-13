package com.wildsmith.jarble.provider.jar.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.jarble.provider.InsertProvider;
import com.wildsmith.jarble.provider.jar.JarTableStructure;

public class JarTableInsertProvider extends JarTableProviderUri implements InsertProvider {

    @Override
    public Uri insert(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, ContentValues values) {
        if (values == null || values.size() == 0) {
            throw new SQLException("Fail to add a new record into " + uri);
        }

        switch (UriCode.fromCode(URI_MATCHER.match(uri))) {
            case JAR:
                long row = insertEntry(openHelper, values);
                if (row > 0) {
                    Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
                    context.getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }
            default:
                throw new SQLException("Fail to add a new record into " + uri);
        }
    }

    private long insertEntry(@NonNull SQLiteOpenHelper openHelper, ContentValues values) {
        long row = -1;
        SQLiteDatabase database = openHelper.getWritableDatabase();
        if (database == null) {
            return row;
        }

        row = database.insertWithOnConflict(JarTableStructure.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        database.close();

        return row;
    }
}
