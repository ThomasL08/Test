package com.example.trailtrekker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HikeActivity extends AppCompatActivity {

    private TextView questionTextView;
    private EditText answerEditText;
    private int currentQuestionIndex = 0;
    private String activityType;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        db = new MyDatabase(this); // Initialize MyDatabase instance

        // Get the activity type from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("activityType")) {
            activityType = intent.getStringExtra("activityType");
        } else {
            // Default to "Hike" if activity type is not provided
            activityType = "Hike";
        }

        // Display the first question
        showNextQuestion();
    }

    public void onPreviousQuestionClick(View view) {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showNextQuestion();
        }
    }

    public void onNextQuestionClick(View view) {
        saveAnswerToDatabase(); // Save answer to database before proceeding to next question
        showNextQuestion();
    }

    private void showNextQuestion() {
        String[] questions = null;
        switch (activityType) {
            case "Hike":
                questions = Constants.HIKING_QUESTIONS;
                break;
            case "Walk":
                questions = Constants.WALK_QUESTIONS;
                break;
            case "Run":
                questions = Constants.RUN_QUESTIONS;
                break;
            case "Bike":
                questions = Constants.BIKE_QUESTIONS;
                break;
        }

        if (questions != null && currentQuestionIndex < questions.length) {
            String question = questions[currentQuestionIndex];
            questionTextView.setText(question);
            answerEditText.setText(""); // Clear previous answer
            currentQuestionIndex++;
        }

        // Check if all questions have been displayed
        if (currentQuestionIndex == questions.length) {
            // All questions have been displayed, start new activity
            startActivity(new Intent(this, HikeExerciseActivity.class));
        }
    }

    private void saveAnswerToDatabase() {
        String answer = answerEditText.getText().toString().trim();
        if (!answer.isEmpty()) {
            long id = db.insertData(answer); // Insert the answer into the database
            if (id != -1) {
                // Insertion successful, show a toast message
                Toast.makeText(this, "Answer saved to database", Toast.LENGTH_SHORT).show();
            } else {
                // Insertion failed, show an error message
                Toast.makeText(this, "Failed to save answer to database", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




