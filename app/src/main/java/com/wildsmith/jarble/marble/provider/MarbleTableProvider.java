package com.wildsmith.jarble.marble.provider;

import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.jarble.marble.MarbleDatabaseHelper;
import com.wildsmith.provider.DeleteProvider;
import com.wildsmith.provider.InsertProvider;
import com.wildsmith.provider.QueryProvider;
import com.wildsmith.provider.TableProvider;
import com.wildsmith.provider.TypeProvider;
import com.wildsmith.provider.UpdateProvider;

public class MarbleTableProvider extends TableProvider {

    @NonNull
    @Override
    protected QueryProvider getQueryProvider() {
        return new MarbleTableQueryProvider();
    }

    @NonNull
    @Override
    protected InsertProvider getInsertProvider() {
        return new MarbleTableInsertProvider();
    }

    @NonNull
    @Override
    protected UpdateProvider getUpdateProvider() {
        return new MarbleTableUpdateProvider();
    }

    @NonNull
    @Override
    protected DeleteProvider getDeleteProvider() {
        return new MarbleTableDeleteProvider();
    }

    @NonNull
    @Override
    protected TypeProvider getTypeProvider() {
        return new MarbleTableTypeProvider();
    }

    @NonNull
    @Override
    protected SQLiteOpenHelper getSQLiteOpenHelper(@NonNull Uri uri) {
        return new MarbleDatabaseHelper(getContext());
    }
}