package com.wildsmith.jarble.marble;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class MarbleDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Marbles";

    private static final String TYPE = ".db";

    public MarbleDatabaseHelper(Context context) {
        super(context, formatDatabaseName(DATABASE_NAME), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        if (database == null) {
            return;
        }

        database.execSQL(MarbleTableStructure.CREATE_TABLE);
        database.execSQL(MarbleTableStructure.CREATE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(MarbleTableStructure.DROP_TABLE);
        onCreate(database);
    }

    @NonNull
    public static String formatDatabaseName(@NonNull String databaseName) {
        return databaseName.replaceAll(" ", "_") + TYPE;
    }
}