package com.example.trailtrekker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyDatabase myDatabase;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView = findViewById(R.id.recycler_view_history);
        myDatabase = new MyDatabase(this);

        // Execute AsyncTask to fetch history items from database
        new LoadHistoryItemsTask().execute();

        // Find the backButton and set an OnClickListener
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Dashboard activity
                Intent intent = new Intent(RecyclerActivity.this, Dashboard.class);
                startActivity(intent);
                finish(); // Finish this activity to prevent it from being returned to the stack
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadHistoryItemsTask extends AsyncTask<Void, Void, List<HistoryItem>> {

        @Override
        protected List<HistoryItem> doInBackground(Void... voids) {
            // Perform database operation in the background
            return myDatabase.getAllHistoryItems();
        }

        @Override
        protected void onPostExecute(List<HistoryItem> historyList) {
            super.onPostExecute(historyList);
            // Update UI with fetched history items
            adapter = new HistoryAdapter(historyList, myDatabase, RecyclerActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerActivity.this));
        }
    }
}





