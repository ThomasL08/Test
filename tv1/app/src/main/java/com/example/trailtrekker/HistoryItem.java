package com.example.trailtrekker;

import android.view.View;
import android.widget.Button;

public class HistoryItem {
    private int uid; // Unique identifier
    private String title;
    private String destination, distance, calories, steps;
    private Button deleteButton;
    private MyDatabase myDatabase;

    public HistoryItem(int uid, String title, String destination, String distance, String calories, String steps) {
        this.uid = uid;
        this.title = title;
        this.destination = destination;
        this.distance = distance;
        this.calories = calories;
        this.steps = steps;
    }

    public int getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }

    public String getDestination() {
        return destination;
    }

    public String getDistance() {
        return distance;
    }

    public String getCalories() {
        return calories;
    }

    public String getSteps() {
        return steps;
    }


}