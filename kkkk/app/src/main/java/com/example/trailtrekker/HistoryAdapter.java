package com.example.trailtrekker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryItem> historyList;
    private MyDatabase myDatabase;
    private Context context;

    public HistoryAdapter(List<HistoryItem> historyList, MyDatabase myDatabase, Context context) {
        this.historyList = historyList;
        this.myDatabase = myDatabase;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem historyItem = historyList.get(position);
        holder.titleTextView.setText(historyItem.getTitle());
        holder.destinationTextView.setText(historyItem.getDestination());
        holder.distanceTextView.setText(historyItem.getDistance());
        holder.caloriesTextView.setText(historyItem.getCalories());
        holder.stepsTextView.setText(historyItem.getSteps());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(historyItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepsTextView;
        TextView caloriesTextView;
        TextView destinationTextView;
        TextView distanceTextView;
        TextView titleTextView;
        TextView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            destinationTextView = itemView.findViewById(R.id.destination_text_view);
            distanceTextView = itemView.findViewById(R.id.distance_text_view);
            caloriesTextView = itemView.findViewById(R.id.calories_text_view);
            stepsTextView = itemView.findViewById(R.id.steps_text_view);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    private void showDeleteConfirmationDialog(HistoryItem historyItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Update the item in the database
                        myDatabase.updateItem(historyItem.getUid());
                        // Remove the item from the list
                        historyList.remove(historyItem);
                        // Notify the adapter that the data set has changed
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog, do nothing
                    }
                });
        builder.create().show();
    }
}

