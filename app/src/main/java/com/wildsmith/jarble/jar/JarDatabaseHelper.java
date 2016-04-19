package com.wildsmith.jarble.jar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class JarDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Jars";

    private static final String TYPE = ".db";

    public JarDatabaseHelper(Context context) {
        super(context, formatDatabaseName(DATABASE_NAME), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        if (database == null) {
            return;
        }

        database.execSQL(JarTableStructure.CREATE_TABLE);
        database.execSQL(JarTableStructure.CREATE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(JarTableStructure.DROP_TABLE);
        onCreate(database);
    }

    @NonNull
    public static String formatDatabaseName(@NonNull String databaseName) {
        return databaseName.replaceAll(" ", "_") + TYPE;
    }
}