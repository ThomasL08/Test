package com.example.trailtrekker;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {
    private Context context;

    // Existing table creation query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + Constants.TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.TITLE + " TEXT, " +
                    Constants.DISTANCE + " INTEGER, " +
                    Constants.STEP_COUNT + " INTEGER, " +
                    Constants.WEIGHT + " FLOAT, " +
                    Constants.HEIGHT + " FLOAT, " +
                    Constants.CALORIES + " INTEGER);" ;

    // New location table creation query
    private static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + Constants.LOCATION_TABLE_NAME + " (" +
                    Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.COLUMN_LATITUDE + " REAL, " +
                    Constants.COLUMN_LONGITUDE + " REAL)";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;

    public MyHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create the existing table
            db.execSQL(CREATE_TABLE);

            // Create the location table
            db.execSQL(CREATE_LOCATION_TABLE);
            Toast.makeText(context, "Database and tables created successfully", Toast.LENGTH_LONG).show();
            Toast.makeText(context, "exec onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }

    }

    // Method to insert location data into the location table
    public long insertLocation(double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_LATITUDE, latitude);
        values.put(Constants.COLUMN_LONGITUDE, longitude);
        long newRowId = db.insert(Constants.LOCATION_TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }
}

