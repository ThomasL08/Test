package com.example.trailtrekker;

public class Constants {
    public static final String DATABASE_NAME = "trailTrekkerDB";
    public static final String TABLE_NAME = "TRAIL_ACTIVITIES";
    public static final String UID = "_id";
    public static final String NAME = "Name";
    public static final String CALORIES = "Calories";
    public static final String DISTANCE = "Distance";
    public static final String STEP_COUNT = "StepCount";
    public static final String WEIGHT = "Weight";
    public static final String HEIGHT = "Height";
    public static final String ACTIVITY_TYPE = "ActivityType";
    public static final int MAX_QUESTIONS = 6; // Maximum number of questions for any activity
    public static final int DATABASE_VERSION = 12; // Database version
    // Prefix for dynamic question columns in the database
    public static final String QUESTION_PREFIX = "Question_";

    // Add more activity-specific questions arrays if needed
    public static final String[] HIKING_QUESTIONS = {
            "Activity Title:",
            "Set calories goal:",
            "Set distance goal:",
            "Set step count goal:",
            "Enter weight:",
            "Enter height:"
    };

    public static final String[] WALK_QUESTIONS = {
            "Activity Title:",
            "Set calories goal:",
            "Set distance goal:",
            "Set step count goal:",
            "Enter weight:",
            "Enter height:"
    };

    public static final String[] RUN_QUESTIONS = {
            "Activity Title:",
            "Set calories goal:",
            "Set distance goal:",
            "Set step count goal:",
            "Enter weight:",
            "Enter height:"
    };

    public static final String[] BIKE_QUESTIONS = {
            "Activity Title:",
            "Set calories goal:",
            "Set distance goal:",
            "Set step count goal:",
            "Enter weight:",
            "Enter height:"
    };
}
