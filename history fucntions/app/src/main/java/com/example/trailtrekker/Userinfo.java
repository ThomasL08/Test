package com.example.trailtrekker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Userinfo extends AppCompatActivity {

    private EditText nameEditText, heightEditText, weightEditText;
    private Button nextButton;
    private MyDatabase db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        nameEditText = findViewById(R.id.nameEditText);
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        nextButton = findViewById(R.id.nextButton);
        db = new MyDatabase(this);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String heightStr = heightEditText.getText().toString();
                String weightStr = weightEditText.getText().toString();

                if (name.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty()) {
                    // Check if any field is empty
                    return;
                }

                double height = Double.parseDouble(heightStr);
                double weight = Double.parseDouble(weightStr);

                // Insert user info into database
                db.insertUserInfo(name, weight, height);

                // Set userInfoFilled to true in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("userInfoFilled", true);
                editor.apply();

                // Start dashboard activity
                Intent intent = new Intent(Userinfo.this, Dashboard.class);
                startActivity(intent);
                finish(); // Finish this activity to prevent user from going back to it using back button
            }
        });
    }
}
