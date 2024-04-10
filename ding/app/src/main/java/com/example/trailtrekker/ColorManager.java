//package com.example.trailtrekker;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.preference.PreferenceManager;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class ColorManager {
//    private static final String COLOR_KEY = "selected_color";
//
//    private static ColorManager instance;
//    private SharedPreferences sharedPreferences;
//
//    private ColorManager(Context context) {
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//    }
//
//    public static ColorManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new ColorManager(context.getApplicationContext());
//        }
//        return instance;
//    }
//
//    public void setSelectedColor(String color) {
//        sharedPreferences.edit().putString(COLOR_KEY, color).apply();
//    }
//
//    public String getSelectedColor() {
//        return sharedPreferences.getString(COLOR_KEY, "#000000"); // Default color
//    }
//}