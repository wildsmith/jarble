package com.wildsmith.jarble.marble.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.provider.DeleteProvider;

public class MarbleTableDeleteProvider extends MarbleTableProviderUri implements DeleteProvider {

    @Override
    public int delete(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, String selection,
        String[] selectionArgs) {
        return -1;
    }
}