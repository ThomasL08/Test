package com.example.trailtrekker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RunActivity extends AppCompatActivity {

    private TextView questionTextView;
    private EditText answerEditText;
    private int currentQuestionIndex = 0;
    private String activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike); // Assuming the layout file for RunActivity is the same as HikeActivity

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);

        // Get the activity type from intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("activityType")) {
            activityType = intent.getStringExtra("activityType");
        } else {
            // Default to "Run" if activity type is not provided
            activityType = "Run";
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
        if (currentQuestionIndex == questions.length) {
            // Last question is displayed, start new activity
            startActivity(new Intent(this, HikeExerciseActivity.class));
        }
    }
}
