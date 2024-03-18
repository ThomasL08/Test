package com.example.trailtrekker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDatabase(Context c) {
        context = c;
        helper = new MyHelper(context);
    }

    public long insertData(String name, String activityType, String[] questions) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.ACTIVITY_TYPE, activityType);

        // Add questions to content values dynamically
        for (int i = 0; i < questions.length; i++) {
            contentValues.put(Constants.QUESTION_PREFIX + (i + 1), questions[i]);
        }

        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }

    public Cursor getData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.ACTIVITY_TYPE};
        // Dynamically add question columns to the query
        for (int i = 1; i <= Constants.MAX_QUESTIONS; i++) {
            columns[i + 2] = Constants.QUESTION_PREFIX + i;
        }
        return db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
    }
}



