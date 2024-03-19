package com.example.trailtrekker;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {
    private Context context;

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
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            Toast.makeText(context, "Table created successfully", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(context, "Error creating table: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("MyHelper", "Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            Toast.makeText(context, "Error upgrading table: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("MyHelper", "Error upgrading table: " + e.getMessage());
        }
    }

    // Add a method to check if the table exists
    public boolean isTableExists() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + Constants.TABLE_NAME + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            boolean exists = cursor.getCount() > 0;
            cursor.close();
            return exists;
        }
        return false;
    }
}




