package com.example.trailtrekker;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UIPreferences extends AppCompatActivity {

    private CheckBox darkModeCheckBox;
    private RadioGroup fontSizeRadioGroup;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uipreferences);

        darkModeCheckBox = findViewById(R.id.darkModeCheckBox);
        fontSizeRadioGroup = findViewById(R.id.fontSizeRadioGroup);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Restore preferences
        darkModeCheckBox.setChecked(sharedPreferences.getBoolean("darkMode", false));
        int fontSizeId = sharedPreferences.getInt("fontSize", R.id.mediumFontSizeRadioButton);
        RadioButton selectedRadioButton = findViewById(fontSizeId);
        selectedRadioButton.setChecked(true);

        // Dark Mode CheckBox listener
        darkModeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new SaveDarkModePreferenceTask().execute(isChecked);
            }
        });

        // Font Size RadioGroup listener
        fontSizeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                new SaveFontSizePreferenceTask().execute(checkedId);
            }
        });
    }

    private class SaveDarkModePreferenceTask extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected Void doInBackground(Boolean... booleans) {
            boolean isChecked = booleans[0];
            editor.putBoolean("darkMode", isChecked);
            editor.apply();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(UIPreferences.this, "Dark Mode preference saved", Toast.LENGTH_SHORT).show();
            // Implement dark mode switching logic here
        }
    }

    private class SaveFontSizePreferenceTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            int checkedId = integers[0];
            editor.putInt("fontSize", checkedId);
            editor.apply();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(UIPreferences.this, "Font Size preference saved", Toast.LENGTH_SHORT).show();
            // Implement font size changing logic here
        }
    }
}
