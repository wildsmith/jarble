package com.wildsmith.jarble.provider.jar.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.jarble.provider.TypeProvider;

public class JarTableTypeProvider extends JarTableProviderUri implements TypeProvider {

    private static final String TAG = JarTableTypeProvider.class.getSimpleName();

    @Override
    public String getType(@NonNull Context context, @NonNull SQLiteOpenHelper openHelper, @NonNull Uri uri) {
        return null;
    }
}