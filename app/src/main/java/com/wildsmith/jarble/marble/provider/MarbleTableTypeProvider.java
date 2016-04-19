package com.wildsmith.jarble.marble.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.provider.TypeProvider;

public class MarbleTableTypeProvider extends MarbleTableProviderUri implements TypeProvider {

    @Override
    public String getType(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri) {
        return null;
    }
}