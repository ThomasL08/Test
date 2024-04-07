package com.example.trailtrekker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class HikeExerciseActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, SensorEventListener, LocationListener {

    MyHelper dbHelper;
    MyDatabase db;
    Cursor cursor;

    private FusedLocationProviderClient fusedLocationClient;
    GoogleMap myMap;

    private static final double SURREY_LAT = 49.189592;
    private static final double SURREY_LONG = -122.847834;

    //Buttons
    private Button backButton;
    private Button startButton;
    private Button stopButton;
    private TextView durationTV;

    //Details textviews
    private TextView titleTV;
    private TextView calTV;
    public TextView disTV;
    private TextView stepTV;

    //Goal textviews
    private TextView gCalTV;
    public TextView gDisTV;
    private TextView gStepTV;

    //Duration stopwatch
    private static Handler handler = new Handler();
    private boolean isStopWatchRunning = false;

    //Step Counter
    private SensorManager mySensorManager;
    private Sensor stepCountSensor;
    private TextView stepCountTV;

    //Distance
    private LocationManager myLocationManger;
    private Location prevLocation;
    private float distanceToLast = 0;

    //Calories kcal = time [minutes] × ((MET × 3.5) × weight [kg] ÷ 200)
    private float walkMET = 3.0f;
    private int calories = 0;
    private int weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_exercise);

        dbHelper = new MyHelper(this);
        db = new MyDatabase(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //buttons
        backButton = findViewById(R.id.backButton);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        if(GlobalVariables.exercising){
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
        }
        else{
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);
        }

        //textViews
        titleTV = findViewById(R.id.titleTV);
        durationTV = findViewById(R.id.durationTV);
        calTV = findViewById(R.id.calTV);
        disTV = findViewById(R.id.disTV);
        stepTV = findViewById(R.id.stepTV);
        gCalTV = findViewById(R.id.gCalTV);
        gDisTV = findViewById(R.id.gDisTV);
        gStepTV = findViewById(R.id.gStepTV);

        titleTV.setText(db.getTitle(GlobalVariables.historyIndex));
        gCalTV.setText("Goal:" + "\n" + db.getCalories(GlobalVariables.historyIndex));
        gStepTV.setText("Goal:" + "\n" + db.getStepCount(GlobalVariables.historyIndex));
        gDisTV.setText("Goal:" + "\n" + db.getDistance(GlobalVariables.historyIndex));


        //STEP COUNTER/////////////////////////////////////////
        stepCountTV = findViewById(R.id.stepTV);
        stepCountTV.setText("-" + "\n" + "\n" + "STEPS");

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        if (stepCountSensor == null) {
            //Toast message if device has no step counter
            Toast.makeText(this, "Device has no step counter sensor", Toast.LENGTH_SHORT).show();
        }


        //DISTANCE COUNTER/////////////////////////////////////////
        myLocationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (myLocationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            myLocationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        } else {
            Toast.makeText(HikeExerciseActivity.this, "GPS provided not enable", Toast.LENGTH_SHORT).show();
        }

        disTV.setText("-" + "\n" + "\n" + "M");


        //CALORIE COUNTER/////////////////////////////////////////
        calTV.setText("-" + "\n" + "\n" + "KCAL");
        weight = Integer.parseInt(db.getWeight());
        countCalories(walkMET, weight);
        prevLocation = myLocationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    //ACTIVITY/////////////////////////////////////////
    public void onClickStart(View view) {
        GlobalVariables.historyIndex++;

        //Check if physical activity permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            requestPhysActPermission();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            prevLocation = myLocationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            GlobalVariables.seconds = 0;
            GlobalVariables.exercising = true;
            GlobalVariables.distance = 0;
            GlobalVariables.stepCount = 0;

            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            showCurrentLocation();

            stepTV.setText("0" + "\n" + "\n" + "STEPS");
            disTV.setText("0" + "\n" + "\n" + "M");
            calTV.setText("0" + "\n" + "\n" + "KCAL");
        }
    }

    public void onClickStop(View view) {
        GlobalVariables.exercising = false;
        isStopWatchRunning = false;

        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(HikeExerciseActivity.this, Dashboard.class);
        startActivity(intent);
    }

    public void onClickRec(View view) {
        openWebPageBasedOnLocation();
    }

    private void runStopWatch() {
        if (!isStopWatchRunning) {
            isStopWatchRunning = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int hours = GlobalVariables.seconds / 3600;
                    int minutes = (GlobalVariables.seconds % 3600) / 60;
                    int secs = GlobalVariables.seconds % 60;
                    String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                    durationTV.setText(time);

                    if (GlobalVariables.exercising) {
                        GlobalVariables.seconds++;
                    }
                    handler.postDelayed(this, 1000);
                }
            });
        }
    }

    private void stopStopWatch() {
        handler.removeCallbacksAndMessages(null);
        isStopWatchRunning = false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
        runStopWatch();
        countCalories(walkMET, weight);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mySensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopStopWatch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLocationManger.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && GlobalVariables.exercising) {
            GlobalVariables.stepCount++;
            stepCountTV.setText(String.valueOf(GlobalVariables.stepCount) + "\n" + "\n" + "STEPS");
        } else {
            GlobalVariables.stepCount = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //GOOGLE MAP/////////////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        else {
            showCurrentLocation();
            myMap.setMyLocationEnabled(true);
            myMap.setOnMyLocationButtonClickListener(this);
            myMap.setOnMyLocationClickListener(this);
        }
    }

    private void requestLocationPermission() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_location_permission)
                .setMessage(R.string.text_location_permission)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(HikeExerciseActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                })
                .create()
                .show();
    }

    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCurrentLocation();
                myMap.setMyLocationEnabled(true);
                myMap.setOnMyLocationButtonClickListener(this);
                myMap.setOnMyLocationClickListener(this);
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPhysActPermission() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_activity_permission)
                .setMessage(R.string.text_activity_permission)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(HikeExerciseActivity.this,
                                new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                                MY_PERMISSIONS_REQUEST_ACTIVITY);
                    }
                })
                .create()
                .show();
    }

    public void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, 15);
                        myMap.animateCamera(update);
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_ACTIVITY = 1;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (prevLocation == null) {
                    prevLocation = location;
                }
                updateLocation(location);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (GlobalVariables.exercising) {
                            disTV.setText(String.format(Locale.getDefault(), "%.2f", GlobalVariables.distance) + "\n" + "\n" + "M");
                        }
                    }
                });
            }
        }).start();
    }

    public void updateLocation(Location location) {
        distanceToLast = location.distanceTo(prevLocation);
        GlobalVariables.distance += distanceToLast;
        prevLocation = location;
    }

    private void countCalories(Float walkMET, Integer weight) {
        if (!isStopWatchRunning) {
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (GlobalVariables.exercising) {
                        GlobalVariables.calories = (int) (GlobalVariables.seconds / 60 * (walkMET * 3.5) * weight / 200);
                    }
                    calTV.setText(Integer.toString(GlobalVariables.calories) + "\n" + "\n" + "KCAL");
                    handler.postDelayed(this, 5000);
                }
            });
        }
    }

    public void openWebPageBasedOnLocation() {
        Location currentLocation = new Location("");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = myLocationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location targetLocation = new Location("");
        targetLocation.setLatitude(SURREY_LAT);
        targetLocation.setLongitude(SURREY_LONG);
        float distance = currentLocation.distanceTo(targetLocation);
        float distThreshold = 5000;
        if (distance <= distThreshold) {
            String url = "https://www.alltrails.com/canada/british-columbia/surrey";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else {
            Toast.makeText(this, "No hiking spots nearby", Toast.LENGTH_SHORT).show();
        }
    }
}
