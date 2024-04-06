package com.example.trailtrekker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, HikeActivity.class);
        startActivity(intent);
    }
}