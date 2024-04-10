package com.example.trailtrekker;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike);

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);

        db = new MyDatabase(this);

        GlobalVariables.dataIndex = db.getRowCount()+1;
//        GlobalVariables.dataIndex = db.getRowCount();
        Log.d("count", String.valueOf(GlobalVariables.dataIndex));

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
        showNextQuestion();
    }

    private void showNextQuestion() {
        String[] questions = null;
        switch (activityType) {
            case "Hike":
                questions = Constants.HIKING_QUESTIONS;
                break;
//            case "Walk":
//                questions = Constants.WALK_QUESTIONS;
//                break;
//            case "Run":
//                questions = Constants.RUN_QUESTIONS;
//                break;
//            case "Bike":
//                questions = Constants.BIKE_QUESTIONS;
//                break;
        }

        if (questions != null && currentQuestionIndex < questions.length) {
            String question = questions[currentQuestionIndex];
            saveAnswer(currentQuestionIndex);

            questionTextView.setText(question);
            answerEditText.setText(""); // Clear previous answer
//            currentQuestionIndex++;
        } else {
            // Last question is displayed, start new activity
            saveAnswer(currentQuestionIndex);
            startActivity(new Intent(this, HikeExerciseActivity.class));
        }

        currentQuestionIndex++;
    }

    private void saveAnswer(int index) {
        String answer = answerEditText.getText().toString().trim();
        if (!answer.isEmpty()) {
            switch (index) {
                case 1:
//                    Toast.makeText(this, "title saved: " + answer, Toast.LENGTH_SHORT).show();
                    db.insertData(Constants.TITLE, answer);
                    break;
                case 2:
//                    Toast.makeText(this, "calories saved: " + answer, Toast.LENGTH_SHORT).show();
                    ContentValues caloriesContent = new ContentValues();
                    caloriesContent.put(Constants.CALORIES, answer);
                    db.updateRow(GlobalVariables.dataIndex, caloriesContent);
                    break;
                case 3:
//                    Toast.makeText(this, "distance saved: " + answer, Toast.LENGTH_SHORT).show();
                    ContentValues distanceContent = new ContentValues();
                    distanceContent.put(Constants.DISTANCE, answer);
                    db.updateRow(GlobalVariables.dataIndex, distanceContent);
                    break;
                case 4:
//                    Toast.makeText(this, "step count saved: " + answer, Toast.LENGTH_SHORT).show();
                    ContentValues stepCountContent = new ContentValues();
                    stepCountContent.put(Constants.STEP_COUNT, answer);
                    db.updateRow(GlobalVariables.dataIndex, stepCountContent);

//                    GlobalVariables.dataIndex++;
                    break;
            }
        } else {
            Toast.makeText(this, "Please provide an answer", Toast.LENGTH_SHORT).show();
        }
    }
}




