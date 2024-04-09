package com.example.trailtrekker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class UIPreferences extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener{

    public static final int DEFAULT_FONT_SIZE_SP = 18;
    private CheckBox darkModeCheckBox;
    private RadioGroup fontSizeRadioGroup, colorButton;

    private ColorManager colorManager;

    private RadioButton smallFontSizeRadioButton, mediumFontSizeRadioButton, largeFontSizeRadioButton;

    private SeekBar seekBar;

    private TextView magnifyTextView, magTV;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    int percentage;

    String color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uipreferences);

        darkModeCheckBox = findViewById(R.id.darkModeCheckBox);
        fontSizeRadioGroup = findViewById(R.id.fontSizeRadioGroup);

        smallFontSizeRadioButton = findViewById(R.id.smallFontSizeRadioButton);
        mediumFontSizeRadioButton = findViewById(R.id.mediumFontSizeRadioButton);
        largeFontSizeRadioButton = findViewById(R.id.largeFontSizeRadioButton);


        magnifyTextView = (TextView)findViewById(R.id.magnifyTextView);
        magTV = (TextView)findViewById(R.id.magTV);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
//        imageView = findViewById(R.id.imageView);

        colorButton = (RadioGroup) findViewById(R.id.radioGroupColors);
        colorButton.setOnCheckedChangeListener(this);

        colorManager = ColorManager.getInstance(this);
        colorManager.setSelectedColor(color);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Restore preferences
        darkModeCheckBox.setChecked(sharedPreferences.getBoolean("darkMode", false));
        int fontSizeId = sharedPreferences.getInt("fontSize", R.id.mediumFontSizeRadioButton);
        RadioButton selectedRadioButton = findViewById(fontSizeId);
        selectedRadioButton.setChecked(true);

//        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        percentage = seekBar.getProgress();
//        magnifyTextView.setText(percentage+ "% ");

//        float newScaleFactor = 0.1f + (float) progress / 100;
//        scaleFactor = newScaleFactor;
//        imageView.setScaleX(scaleFactor);
//        imageView.setScaleY(scaleFactor);

//        scaleFactor = 1f + (float) progress / 100;
//
//        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//        layoutParams.width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * scaleFactor);
//        layoutParams.height = (int) (Resources.getSystem().getDisplayMetrics().heightPixels * scaleFactor);
//        getWindow().setAttributes(layoutParams);
//
//        magnifyTextView.setText(String.format(Locale.getDefault(), "%.1fx", scaleFactor));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//
//            return true;
//        }
//    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String color = null;

        if (checkedId == R.id.radioRed) {
            color = "#ff0000";
        } else if (checkedId == R.id.radioBlue) {
            color = "#0000ff";
        } else if (checkedId == R.id.radioGreen) {
            color = "#146714";
        } else if (checkedId == R.id.radioBrown) {
            color = "#372121";
        }
        if (color != null) {
            int parsedColor = Color.parseColor(color);
            applyTextColorToViews(parsedColor);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("selectedColor", color);
            editor.apply();
            saveTextColorPreference(color);
        }
    }

    private void applyTextColorToViews(int textColor) {
        magnifyTextView.setTextColor(textColor);
        magTV.setTextColor(textColor);
        darkModeCheckBox.setTextColor(textColor);
        smallFontSizeRadioButton.setTextColor(textColor);
        mediumFontSizeRadioButton.setTextColor(textColor);
        largeFontSizeRadioButton.setTextColor(textColor);
    }

    private void saveTextColorPreference(String color) {
        editor.putString("textColor", color);
        editor.apply();
    }

    private int retrieveTextColorPreference() {
        String colorString = sharedPreferences.getString("textColor", "#000000"); // Default to black
        return Color.parseColor(colorString);
    }

    public void setTextViewColor(ViewGroup viewGroup, int color) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(color);
            } else if (view instanceof ViewGroup) {
                setTextViewColor((ViewGroup) view, color);
            }
        }
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
            boolean isDarkModeEnabled = darkModeCheckBox.isChecked();

            if (isDarkModeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
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
            int selectedFontSizeId = sharedPreferences.getInt("fontSize", R.id.mediumFontSizeRadioButton);
            int fontSize;

            if (selectedFontSizeId == R.id.smallFontSizeRadioButton) {
                fontSize = 14;
            } else if (selectedFontSizeId == R.id.mediumFontSizeRadioButton) {
                fontSize = 18;
            } else if (selectedFontSizeId == R.id.largeFontSizeRadioButton) {
                fontSize = 24;
            } else {
                fontSize = 18;
            }


            applyFontSizeToViews(fontSize);
        }

        private void applyFontSizeToViews(int fontSize) {
            magnifyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            magTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            darkModeCheckBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            smallFontSizeRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            mediumFontSizeRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            largeFontSizeRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }

    }

    public void onClickBack(View v){
        Intent intent = new Intent(UIPreferences.this,SettingsActivity.class);
        startActivity(intent);
    }
}
