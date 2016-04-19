package com.wildsmith.jarble.jar.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.provider.UpdateProvider;
import com.wildsmith.jarble.jar.JarTableStructure;

public class JarTableUpdateProvider extends JarTableProviderUri implements UpdateProvider {

    @Override
    public int update(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, ContentValues values,
        String selection, String[] selectionArgs) {
        int row = -1;
        SQLiteDatabase database = openHelper.getWritableDatabase();
        if (database == null) {
            return row;
        }

        row = database.updateWithOnConflict(JarTableStructure.TABLE_NAME, values, selection, selectionArgs,
            SQLiteDatabase.CONFLICT_REPLACE);
        database.close();

        return row;
    }
}