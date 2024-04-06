package com.example.trailtrekker;

import android.annotation.SuppressLint;
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

    public void deleteAllData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, null, null);
    }

    public void updateRow(long rowId, ContentValues contentValues) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = Constants.UID + "=?";
        String[] selectionArgs = {String.valueOf(rowId)};
        db.update(Constants.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public void insertData(long rowId, String columnName, String newValue) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, newValue);
        db.insert(Constants.TABLE_NAME, null, contentValues);
    }

    public void insertUserInfo(String name, double weight, double height) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.WEIGHT, weight);
        contentValues.put(Constants.HEIGHT, height);
        db.insert(Constants.TABLE_NAME, null, contentValues);
    }

    // Retrieving data from a specific column and row
    @SuppressLint("Range")
    public String getTitle() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT " + Constants.NAME + " FROM " + Constants.TABLE_NAME + " WHERE " + Constants.UID + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Constants.NAME));
            cursor.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public String getCalories() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT " + Constants.CALORIES + " FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.UID + " = ?";
        String[] selectionArgs = { "1" }; // ID 3
        Cursor cursor = db.rawQuery(query, selectionArgs);

        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Constants.CALORIES));
            cursor.close();
        }
        return result;
    }
    @SuppressLint("Range")
    public String getStepCount() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT " + Constants.STEP_COUNT + " FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.UID + " = ?";
        String[] selectionArgs = { "1" }; // ID 3
        Cursor cursor = db.rawQuery(query, selectionArgs);

        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Constants.STEP_COUNT));
            cursor.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public String getDistance() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT " + Constants.DISTANCE + " FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.UID + " = ?";
        String[] selectionArgs = { "1" }; // ID 3
        Cursor cursor = db.rawQuery(query, selectionArgs);

        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Constants.DISTANCE));
            cursor.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public String getWeight() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT " + Constants.WEIGHT + " FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.UID + " = ?";
        String[] selectionArgs = { "1" }; // ID 3
        Cursor cursor = db.rawQuery(query, selectionArgs);

        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Constants.WEIGHT));
            cursor.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public String getHeight() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT " + Constants.HEIGHT + " FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.UID + " = ?";
        String[] selectionArgs = { "1" }; // ID 3
        Cursor cursor = db.rawQuery(query, selectionArgs);

        String result = null;
        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(Constants.HEIGHT));
            cursor.close();
        }
        return result;
    }
}
