package com.example.trailtrekker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private MyDatabase db;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        // Initialize the RecyclerView and adapter
        recyclerView = findViewById(R.id.recycler_view_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the database
        db = new MyDatabase(this);

        // Retrieve all history items from the database
        List<HistoryItem> historyList = db.getAllHistoryItems();

        // Create the adapter with the retrieved history items
        adapter = new HistoryAdapter(historyList);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RecyclerActivity.this, Dashboard.class);
        startActivity(intent);
    }
}
