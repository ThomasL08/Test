package com.example.trailtrekker;

import android.Manifest;
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
    private int seconds = 0;
    private boolean exercising = false;

    //Step Counter
    private SensorManager mySensorManager;
    private Sensor stepCountSensor;
    private Integer stepCount = 0;
    private TextView stepCountTV;

    //Distance
    private LocationManager myLocationManger;
    private Location prevLocation;
    private double distance = 0;
    private float distanceToLast = 0;

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
        backButton = (Button) findViewById(R.id.backButton);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        //textViews
        titleTV = (TextView) findViewById(R.id.titleTV);
        durationTV = (TextView) findViewById(R.id.durationTV);
        calTV = (TextView) findViewById(R.id.calTV);
        disTV = (TextView) findViewById(R.id.disTV);
        stepTV = (TextView) findViewById(R.id.stepTV);
        gCalTV = (TextView) findViewById(R.id.gCalTV);
        gDisTV = (TextView) findViewById(R.id.gDisTV);
        gStepTV = (TextView) findViewById(R.id.gStepTV);

        titleTV.setText(db.getTitle());
        gCalTV.setText("Goal:" + "\n" + db.getCalories());
        gStepTV.setText("Goal:" + "\n" + db.getStepCount());
        gDisTV.setText("Goal:" + "\n" + db.getDistance());
//        Toast.makeText(HikeExerciseActivity.this, db.getTitle(), Toast.LENGTH_SHORT).show();


        //STOPWATCH/////////////////////////////////////////
        //Resource: https://www.geeksforgeeks.org/how-to-create-a-stopwatch-app-using-android-studio/
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            exercising = savedInstanceState.getBoolean("exercising");
        }
        runStopWatch();

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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                return;
            }
            myLocationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        } else {
            Toast.makeText(HikeExerciseActivity.this, "GPS provided not enable", Toast.LENGTH_SHORT).show();
            // GPS provider is not enabled, handle accordingly (e.g., show alert dialog)
        }

        disTV = findViewById(R.id.disTV);
        disTV.setText("-" + "\n" + "\n" + "M");


        //CALORIE COUNTER/////////////////////////////////////////
        calTV = findViewById(R.id.calTV);
        calTV.setText("-" + "\n" + "\n" + "KCAL");
    }

    //ACTIVITY/////////////////////////////////////////
    public void onClickStart(View view) {
        seconds = 0;
        exercising = true;

        //swap start and stop buttons
        startButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);
        showCurrentLocation();

        stepTV.setText("0" + "\n" + "\n" + "STEPS");
        distance = 0;
        disTV.setText(String.format(Locale.getDefault(), "%.2f", distance) + "\n" + "\n" + "M");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        prevLocation = myLocationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public void onClickStop(View view) {
        exercising = false;

        //swap start and stop buttons
        startButton.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);
    }

    public void onClickBack(View view) {
        //go back to dashboard
        Intent intent = new Intent(HikeExerciseActivity.this, Dashboard.class);
        startActivity(intent);
    }

    public void onClickRec(View view) {
        openWebPageBasedOnLocation();
    }


    //start stopwatch
    private void runStopWatch() {
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override

            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                //format to readable time
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                durationTV.setText(time);

                //increment time when start button has been pressed
                if (exercising) {
                    seconds++;
                }
                // post the code again with 1 sec delay
                handler.postDelayed(this, 1000);
            }
        });
    }

    //Save exercise duration
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("exercising", exercising);
    }

    //STOPWATCH/////////////////////////////////////////

    protected void onResume() {
        super.onResume();
        //register SensorEventListeners
        mySensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //dont unregister because app should keep working

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLocationManger.removeUpdates(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && exercising) {
            stepCount++;
            stepCountTV.setText(String.valueOf(stepCount) + "\n" + "\n" + "STEPS");
        } else {
            stepCount = 0;

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    //GOOGLE MAP/////////////////////////////////////////
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        if (checkLocationPermission()) {
            myMap.setMyLocationEnabled(true);
            myMap.setOnMyLocationButtonClickListener(this);
            myMap.setOnMyLocationClickListener(this);

            // Zoom to Surrey
            LatLng surrey = new LatLng(SURREY_LAT, SURREY_LONG);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(surrey, 15);
            myMap.animateCamera(update);
        }
    }

    public void showCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

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
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HikeExerciseActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    //DISTANCE///////////////////////////////////////////////
//Resource: https://stackoverflow.com/questions/21536116/calculating-distance-traveled-in-android-google-maps
    @Override
    public void onLocationChanged(@NonNull Location location) {

        //start new thread to track distance moved
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (prevLocation == null) {
                    prevLocation = location;
                }
                updateLocation(location);

                //update distance textview
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (exercising) {
                            disTV.setText(String.format(Locale.getDefault(), "%.2f", distance) + "\n" + "\n" + "M");
                        }
                    }
                });
            }
        }).start();


    }

    //method that gets distance between previous and current location and adds to cumulative distance
    public void updateLocation(Location location) {
        Log.d("test", "updateLocation() called");
        if (location != null && prevLocation != null) {
            distanceToLast = location.distanceTo(prevLocation);
        }
        distance += distanceToLast;
        prevLocation = location;
    }

    public void openWebPageBasedOnLocation() {
        Location currentLocation = new Location("");
//        currentLocation.setLatitude(currentLocation.getLatitude()); //i think this isn't the right way to get the lat and long
//        currentLocation.setLongitude(currentLocation.getLongitude());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        currentLocation = myLocationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        // Hardcoded target location
        Location targetLocation = new Location("");
        targetLocation.setLatitude(SURREY_LAT);
        targetLocation.setLongitude(SURREY_LONG);

        float distance = currentLocation.distanceTo(targetLocation);

        float distThreshold = 5000;

        if (distance <= distThreshold) {
            // Open web page using implicit intent
            String url = "https://www.alltrails.com/canada/british-columbia/surrey"; // Your target web page URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else {
            Toast.makeText(this, "No hiking spots nearby", Toast.LENGTH_SHORT).show();
        }
    }
}

