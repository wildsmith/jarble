package com.wildsmith.jarble.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Defines the structure for {@link ContentProvider}s that need to communicate with tables in the database. By defining differing
 * structures
 * for each provider action, ie {@link QueryProvider}/{@link InsertProvider}/etc, we can keep code that performs completely different tasks
 * separated; preventing clutter and making code easier to read.
 */
public abstract class TableProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return getQueryProvider().query(getContext(), getSQLiteOpenHelper(uri), uri, projection, selection, selectionArgs, sortOrder);
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return getInsertProvider().insert(getContext(), getSQLiteOpenHelper(uri), uri, values);
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return getUpdateProvider().update(getContext(), getSQLiteOpenHelper(uri), uri, values, selection, selectionArgs);
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return getDeleteProvider().delete(getContext(), getSQLiteOpenHelper(uri), uri, selection, selectionArgs);
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public String getType(@NonNull Uri uri) {
        return getTypeProvider().getType(getContext(), getSQLiteOpenHelper(uri), uri);
    }

    /**
     * @return the implemented {@link QueryProvider} instance.
     */
    @NonNull
    protected abstract QueryProvider getQueryProvider();

    /**
     * @return the implemented {@link InsertProvider} instance.
     */
    @NonNull
    protected abstract InsertProvider getInsertProvider();

    /**
     * @return the implemented {@link UpdateProvider} instance.
     */
    @NonNull
    protected abstract UpdateProvider getUpdateProvider();

    /**
     * @return the implemented {@link DeleteProvider} instance.
     */
    @NonNull
    protected abstract DeleteProvider getDeleteProvider();

    /**
     * @return the implemented {@link TypeProvider} instance.
     */
    @NonNull
    protected abstract TypeProvider getTypeProvider();

    /**
     * @return the implemented {@link SQLiteOpenHelper} instance.
     */
    @NonNull
    protected abstract SQLiteOpenHelper getSQLiteOpenHelper(@NonNull Uri uri);
}