package com.example.trailtrekker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Get shared preferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Check if user info has already been filled
        boolean userInfoFilled = sharedPreferences.getBoolean("userInfoFilled", false);
        if (userInfoFilled) {
            // User info already filled, navigate to the dashboard
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            finish(); // Finish LoginActivity to prevent going back to it
        }

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        // Get the entered username and password
        String enteredUsername = usernameEditText.getText().toString().trim();
        String enteredPassword = passwordEditText.getText().toString().trim();

        // Retrieve saved credentials from SharedPreferences
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        // Check if the entered username and password are empty
        if (TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty(enteredPassword)) {
            Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
        } else {
            // Check if the entered username and password match the saved credentials
            if (enteredUsername.equals(savedUsername) && enteredPassword.equals(savedPassword)) {
                // Successful login
                // Check if user info has already been filled
                boolean userInfoFilled = sharedPreferences.getBoolean("userInfoFilled", false);
                if (!userInfoFilled) {
                    // User info not filled, navigate to UserInfo activity
                    startActivity(new Intent(LoginActivity.this, Userinfo.class));
                    finish(); // Finish LoginActivity to prevent going back to it
                } else {
                    // User info already filled, navigate to the dashboard
                    startActivity(new Intent(LoginActivity.this, Dashboard.class));
                    finish(); // Finish LoginActivity to prevent going back to it
                }
            } else {
                // Invalid credentials
                Toast.makeText(LoginActivity.this, "Invalid username or password, register again", Toast.LENGTH_SHORT).show();
                // Redirect to RegisterActivity for registration
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Clear username and password fields
        usernameEditText.setText("");
        passwordEditText.setText("");
    }
}

