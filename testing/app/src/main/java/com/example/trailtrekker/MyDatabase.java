package com.example.trailtrekker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase {

    private Context context;
    private final MyHelper helper;

    public MyDatabase(Context c) {
        context = c;
        helper = new MyHelper(context);
    }

    public long insertData(String activityTitle) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ActivityTitle", activityTitle);
        return db.insert(Constants.TABLE_NAME, null, contentValues);
    }

    public Cursor getData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, "ActivityTitle"};
        return db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
    }

    public Cursor getGoalsData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {"CaloriesGoal", "DistanceGoal", "StepCountGoal"};
        return db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
    }

    public int getCaloriesGoal() {
        SQLiteDatabase db = helper.getReadableDatabase();
        int caloriesGoal = 0;
        Cursor cursor = db.rawQuery("SELECT CaloriesGoal FROM " + Constants.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            caloriesGoal = cursor.getInt(0);
        }
        cursor.close();
        return caloriesGoal;
    }

    public int getDistanceGoal() {
        SQLiteDatabase db = helper.getReadableDatabase();
        int distanceGoal = 0;
        Cursor cursor = db.rawQuery("SELECT DistanceGoal FROM " + Constants.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            distanceGoal = cursor.getInt(0);
        }
        cursor.close();
        return distanceGoal;
    }

    public int getStepCountGoal() {
        SQLiteDatabase db = helper.getReadableDatabase();
        int stepCountGoal = 0;
        Cursor cursor = db.rawQuery("SELECT StepCountGoal FROM " + Constants.TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            stepCountGoal = cursor.getInt(0);
        }
        cursor.close();
        return stepCountGoal;
    }

    private static class MyHelper extends SQLiteOpenHelper {

        private static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " +
                        Constants.TABLE_NAME + " (" +
                        Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ActivityTitle TEXT, " +
                        "CaloriesGoal INTEGER, " +
                        "DistanceGoal REAL, " +
                        "StepCountGoal INTEGER, " +
                        "Weight REAL, " +
                        "Height REAL);";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;

        public MyHelper(Context context) {
            super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLException e) {
                // Handle the exception
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                // Handle the exception
            }
        }
    }
}



