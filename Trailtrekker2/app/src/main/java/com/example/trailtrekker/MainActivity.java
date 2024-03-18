package com.example.trailtrekker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    MyDatabase db;
    MyHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new MyHelper(this);
        db = new MyDatabase(this);
        SQLiteDatabase myDatabase = helper.getWritableDatabase();


    }
}