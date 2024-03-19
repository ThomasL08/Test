package com.example.trailtrekker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
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
}
