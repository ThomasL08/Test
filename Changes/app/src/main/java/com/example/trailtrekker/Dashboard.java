package com.example.trailtrekker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {

    private TextView greetingTextView;
    private MyDatabase db;
    private String greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views and database
        greetingTextView = findViewById(R.id.greetingTextView);
        db = new MyDatabase(this);

        // Update greeting and username
        updateGreetingAndUsername();
    }

    // Method to update the greeting and username
    private void updateGreetingAndUsername() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Set the appropriate greeting
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greeting = "Good Morning";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        // Execute AsyncTask to retrieve username from the database
        new RetrieveUsernameTask().execute();
    }

    // AsyncTask to retrieve username from the database
    private class RetrieveUsernameTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // Retrieve username from the database
            return db.getTitle();
        }

        @Override
        protected void onPostExecute(String username) {
            // Set the greeting and username in the TextView
            greetingTextView.setText(String.format(Locale.getDefault(), "%s, %s", greeting, username));
        }
    }

    // Method to handle click on Tile 1 (Hike)
    public void onTile1Click(View view) {
        startActivity(new Intent(this, HikeActivity.class));
    }

    // Method to handle click on Tile 2 (Walk)
    public void onTile2Click(View view) {
        startActivity(new Intent(this, WalkActivity.class));
    }

    // Method to handle click on Tile 3 (Run)
    public void onTile3Click(View view) {
        startActivity(new Intent(this, RunActivity.class));
    }

    // Method to handle click on Tile 4 (Bike)
    public void onTile4Click(View view) {
        startActivity(new Intent(this, BikeActivity.class));
    }

    //Method to handle click on Settings
    public void onSettingsClick(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}




