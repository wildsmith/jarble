package com.wildsmith.jarble.provider.jar.provider;

import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.wildsmith.jarble.provider.DeleteProvider;
import com.wildsmith.jarble.provider.InsertProvider;
import com.wildsmith.jarble.provider.QueryProvider;
import com.wildsmith.jarble.provider.TableProvider;
import com.wildsmith.jarble.provider.TypeProvider;
import com.wildsmith.jarble.provider.UpdateProvider;
import com.wildsmith.jarble.provider.jar.JarDatabaseHelper;

public class JarTableProvider extends TableProvider {

    private volatile QueryProvider queryProvider;

    private volatile InsertProvider insertProvider;

    private volatile UpdateProvider updateProvider;

    private volatile DeleteProvider deleteProvider;

    private volatile TypeProvider typeProvider;

    private volatile SQLiteOpenHelper openHelper;

    /**
     * Since we are using lazy loading to initialize our providers we use the 'Double-Check Idiom' approach.
     *
     * @return the instance of {@link QueryProvider} associated with this implementation of {@link TableProvider}.
     */
    @NonNull
    @Override
    protected QueryProvider getQueryProvider() {
        QueryProvider result = queryProvider;
        if (result == null) {
            synchronized (this) {
                result = queryProvider;
                if (result == null) {
                    queryProvider = result = new JarTableQueryProvider();
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
    @Override
    protected InsertProvider getInsertProvider() {
        InsertProvider result = insertProvider;
        if (result == null) {
            synchronized (this) {
                result = insertProvider;
                if (result == null) {
                    insertProvider = result = new JarTableInsertProvider();
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
    @Override
    protected UpdateProvider getUpdateProvider() {
        UpdateProvider result = updateProvider;
        if (result == null) {
            synchronized (this) {
                result = updateProvider;
                if (result == null) {
                    updateProvider = result = new JarTableUpdateProvider();
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
    @Override
    protected DeleteProvider getDeleteProvider() {
        DeleteProvider result = deleteProvider;
        if (result == null) {
            synchronized (this) {
                result = deleteProvider;
                if (result == null) {
                    deleteProvider = result = new JarTableDeleteProvider();
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
    @Override
    protected TypeProvider getTypeProvider() {
        TypeProvider result = typeProvider;
        if (result == null) {
            synchronized (this) {
                result = typeProvider;
                if (result == null) {
                    typeProvider = result = new JarTableTypeProvider();
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
    @Override
    protected SQLiteOpenHelper getSQLiteOpenHelper(@NonNull Uri uri) {
        SQLiteOpenHelper result = openHelper;
        if (result == null) {
            synchronized (this) {
                result = openHelper;
                if (result == null) {
                    openHelper = result = new JarDatabaseHelper(getContext());
                }
            }
        }

        return result;
    }
}