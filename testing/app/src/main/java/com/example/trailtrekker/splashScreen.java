package com.example.trailtrekker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class splashScreen extends AppCompatActivity {

    private static final int SPLASH_DELAY = 5000; // 5 seconds
    private SharedPreferences sharedPreferences;

    private boolean isFirstTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Check if the user has previously registered
        isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        // Delay for 5 seconds and then redirect to appropriate activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstTime) {
                    // If it's the first time, redirect to RegisterActivity
                    Intent intent = new Intent(splashScreen.this, RegisterActivity.class);
                    startActivity(intent);
                } else {
                    // If not the first time, redirect to LoginActivity
                    Intent intent = new Intent(splashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish(); // Finish the splash activity to prevent the user from coming back to it using the back button
            }
        }, SPLASH_DELAY);
    }
}