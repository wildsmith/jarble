package com.wildsmith.jarble.provider.jar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JarDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Jars";

    public JarDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
}