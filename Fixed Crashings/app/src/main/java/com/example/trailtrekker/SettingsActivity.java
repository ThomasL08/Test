package com.example.trailtrekker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEditText, passwordEditText, weightEditText, heightEditText;

    private SharedPreferences sharedPreferences;

    MyHelper dbHelper;
    MyDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = new MyHelper(this);
        db = new MyDatabase(this);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        heightEditText = (EditText) findViewById(R.id.heightEditText);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        //display current account username and pw data here
        usernameEditText.setText(sharedPreferences.getString("username", savedUsername));
        passwordEditText.setText(sharedPreferences.getString("password", savedPassword));

        //display saved weight and height data here
        weightEditText.setText(db.getWeight());
        heightEditText.setText(db.getHeight());

    }

    @Override
    public void onClick(View v) {

        String enteredUsername = usernameEditText.getText().toString().trim();
        String enteredPassword = passwordEditText.getText().toString().trim();
        String enteredWeight = weightEditText.getText().toString().trim();
        String enteredHeight = heightEditText.getText().toString().trim();


        // Validate input fields
        if (TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty(enteredPassword) || TextUtils.isEmpty(enteredWeight) || TextUtils.isEmpty(enteredHeight)) {
            Toast.makeText(SettingsActivity.this, "Please fill in missing information", Toast.LENGTH_SHORT).show();
        } else {
            // Save username and password account information
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", enteredUsername);
            editor.putString("password", enteredPassword);
            Toast.makeText(SettingsActivity.this, "Account information updated", Toast.LENGTH_SHORT).show();
            editor.apply();

            // Save weight and height account information here (sqlite database)
            //updateWeight
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put(Constants.WEIGHT, enteredWeight);
            db.updateRow(1, contentValues1);

            //updateHeight
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put(Constants.HEIGHT, enteredHeight);
            db.updateRow(1, contentValues2);

        }
    }

    public void onClickBack(View v){
        //method for back button
        Intent intent = new Intent(SettingsActivity.this, Dashboard.class);
        startActivity(intent);
    }

}
