package com.example.trailtrekker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + Constants.TABLE_NAME + " ("
                + Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.NAME + " TEXT, "
                + Constants.ACTIVITY_TYPE + " TEXT, ";

        // Add dynamic question columns to the create table query
        for (int i = 1; i <= Constants.MAX_QUESTIONS; i++) {
            createTableQuery += Constants.QUESTION_PREFIX + i + " TEXT, ";
        }

        createTableQuery = createTableQuery.substring(0, createTableQuery.length() - 2) + ")";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }
}


