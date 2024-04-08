package com.example.trailtrekker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {

    private TextView greetingTextView, timeTV;
    private MyDatabase db;
    private String greeting;
    private Button arrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);

        // Initialize views and database
        greetingTextView = findViewById(R.id.greetingTextView);
        db = new MyDatabase(this);
        arrowButton = findViewById(R.id.arrow_button);
        timeTV = findViewById(R.id.timeTV);

        // Update greeting and username
        updateGreetingAndUsername();

        //Current Hike
//        Boolean exercising = Boolean.valueOf(getIntent().getStringExtra("boolean"));
        Log.d("b", String.valueOf(GlobalVariables.exercising));

        if(GlobalVariables.exercising){
            arrowButton.setVisibility(View.VISIBLE);
            showStopWatch();
        }
        else{
            arrowButton.setVisibility(View.GONE);
            timeTV.setText("No Current Hike");

        }
    }

    private void showStopWatch() {
        int hours = GlobalVariables.seconds / 3600;
        int minutes = (GlobalVariables.seconds % 3600) / 60;
        int secs = GlobalVariables.seconds % 60;

        String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
        timeTV.setText(time);
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
            return db.getName();
        }

        @Override
        protected void onPostExecute(String username) {
            // Set the greeting and username in the TextView
            greetingTextView.setText(String.format(Locale.getDefault(), "%s, %s", greeting, username));
        }
    }

    // Method to handle click on Tile 1 (Hike)
    public void onTile1Click(View view) {
        if(GlobalVariables.exercising){
            startActivity(new Intent(this, HikeExerciseActivity.class));
        }
        else {
            startActivity(new Intent(this, HikeActivity.class));
        }
    }

    //mehtod for account
    public void onTile2Click(View view) {
//        startActivity(new Intent(this, WalkActivity.class));
    }

//method for history
    public void onTile3Click(View view) {
        startActivity(new Intent(this, RecyclerActivity.class));

    }

//method for preferences
    public void onTile4Click(View view) {

    }

    //Method to handle click on Settings
    public void onSettingsClick(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}




