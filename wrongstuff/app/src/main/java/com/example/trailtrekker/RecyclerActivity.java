package com.example.trailtrekker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

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
}
