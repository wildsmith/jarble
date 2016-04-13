package com.wildsmith.jarble.provider.jar.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.jarble.provider.DeleteProvider;

public class JarTableDeleteProvider extends JarTableProviderUri implements DeleteProvider {

    @Override
    public int delete(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri, String selection,
        String[] selectionArgs) {
        return -1;
    }
}