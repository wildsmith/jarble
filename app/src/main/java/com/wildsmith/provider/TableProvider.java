package com.wildsmith.provider;

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

    private volatile QueryProvider queryProvider;

    private volatile InsertProvider insertProvider;

    private volatile UpdateProvider updateProvider;

    private volatile DeleteProvider deleteProvider;

    private volatile TypeProvider typeProvider;

    private volatile SQLiteOpenHelper openHelper;

    @Override
    public boolean onCreate() {
        return true;
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return getQueryProviderInternal().query(getContext(), getSQLiteOpenHelperInternal(uri), uri, projection, selection, selectionArgs, sortOrder);
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return getInsertProviderInternal().insert(getContext(), getSQLiteOpenHelperInternal(uri), uri, values);
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return getUpdateProviderInternal().update(getContext(), getSQLiteOpenHelperInternal(uri), uri, values, selection, selectionArgs);
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return getDeleteProviderInternal().delete(getContext(), getSQLiteOpenHelperInternal(uri), uri, selection, selectionArgs);
    }

    //At the time we leverage getContext(), the context will NOT be null, unlike its annotation suggests
    @SuppressWarnings("ConstantConditions")
    @Override
    public String getType(@NonNull Uri uri) {
        return getTypeProviderInternal().getType(getContext(), getSQLiteOpenHelperInternal(uri), uri);
    }

    /**
     * Since we are using lazy loading to initialize our providers we use the 'Double-Check Idiom' approach.
     *
     * @return the instance of {@link QueryProvider} associated with this implementation of {@link TableProvider}.
     */
    @NonNull
    protected QueryProvider getQueryProviderInternal() {
        QueryProvider result = queryProvider;
        if (result == null) {
            synchronized (this) {
                result = queryProvider;
                if (result == null) {
                    queryProvider = result = getQueryProvider();
                }
            }
        }

        return result;
    }

    /**
     * Since we are using lazy loading to initialize our providers we use the 'Double-Check Idiom' approach.
     *
     * @return the instance of {@link InsertProvider} associated with this implementation of {@link TableProvider}.
     */
    @NonNull
    protected InsertProvider getInsertProviderInternal() {
        InsertProvider result = insertProvider;
        if (result == null) {
            synchronized (this) {
                result = insertProvider;
                if (result == null) {
                    insertProvider = result = getInsertProvider();
                }
            }
        }

        return result;
    }

    /**
     * Since we are using lazy loading to initialize our providers we use the 'Double-Check Idiom' approach.
     *
     * @return the instance of {@link UpdateProvider} associated with this implementation of {@link TableProvider}.
     */
    @NonNull
    protected UpdateProvider getUpdateProviderInternal() {
        UpdateProvider result = updateProvider;
        if (result == null) {
            synchronized (this) {
                result = updateProvider;
                if (result == null) {
                    updateProvider = result = getUpdateProvider();
                }
            }
        }

        return result;
    }

    /**
     * Since we are using lazy loading to initialize our providers we use the 'Double-Check Idiom' approach.
     *
     * @return the instance of {@link DeleteProvider} associated with this implementation of {@link TableProvider}.
     */
    @NonNull
    protected DeleteProvider getDeleteProviderInternal() {
        DeleteProvider result = deleteProvider;
        if (result == null) {
            synchronized (this) {
                result = deleteProvider;
                if (result == null) {
                    deleteProvider = result = getDeleteProvider();
                }
            }
        }

        return result;
    }

    /**
     * Since we are using lazy loading to initialize our providers we use the 'Double-Check Idiom' approach.
     *
     * @return the instance of {@link TypeProvider} associated with this implementation of {@link TableProvider}.
     */
    @NonNull
    protected TypeProvider getTypeProviderInternal() {
        TypeProvider result = typeProvider;
        if (result == null) {
            synchronized (this) {
                result = typeProvider;
                if (result == null) {
                    typeProvider = result = getTypeProvider();
                }
            }
        }

        return result;
    }

    /**
     * Since we are using lazy loading to initialize our providers we use the 'Double-Check Idiom' approach.
     *
     * @return the instance of {@link SQLiteOpenHelper} associated with this implementation of {@link TableProvider}.
     */
    @NonNull
    protected SQLiteOpenHelper getSQLiteOpenHelperInternal(@NonNull Uri uri) {
        SQLiteOpenHelper result = openHelper;
        if (result == null) {
            synchronized (this) {
                result = openHelper;
                if (result == null) {
                    openHelper = result = getSQLiteOpenHelper(uri);
                }
            }
        }

        return result;
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