package com.wildsmith.jarble.jar.provider;

import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.jarble.jar.JarDatabaseHelper;
import com.wildsmith.provider.DeleteProvider;
import com.wildsmith.provider.InsertProvider;
import com.wildsmith.provider.QueryProvider;
import com.wildsmith.provider.TableProvider;
import com.wildsmith.provider.TypeProvider;
import com.wildsmith.provider.UpdateProvider;

public class JarTableProvider extends TableProvider {

    @NonNull
    @Override
    protected QueryProvider getQueryProvider() {
        return new JarTableQueryProvider();
    }

    @NonNull
    @Override
    protected InsertProvider getInsertProvider() {
        return new JarTableInsertProvider();
    }

    @NonNull
    @Override
    protected UpdateProvider getUpdateProvider() {
        return new JarTableUpdateProvider();
    }

    @NonNull
    @Override
    protected DeleteProvider getDeleteProvider() {
        return new JarTableDeleteProvider();
    }

    @NonNull
    @Override
    protected TypeProvider getTypeProvider() {
        return new JarTableTypeProvider();
    }

    @NonNull
    @Override
    protected SQLiteOpenHelper getSQLiteOpenHelper(@NonNull Uri uri) {
        return new JarDatabaseHelper(getContext());
    }
}