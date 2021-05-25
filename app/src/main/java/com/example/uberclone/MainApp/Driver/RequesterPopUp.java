package com.example.uberclone.MainApp.Driver;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.uberclone.Modules.Requests.DriverLocation;
import com.example.uberclone.Modules.Requests.RiderLocation;
import com.example.uberclone.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RequesterPopUp extends AppCompatActivity {

    private ImageView avatarView;
    private TextView usernameField;
    private TextView currentLocationField;
    private TextView endLocationField;

    private Spinner timechooser;
    ArrayAdapter<Integer> timesAdapter;
    private Integer[] times;

    private Integer choosenTime;

    private Button acceptRequest;

    private String nameOfDriver;
    private DriverLocation driverLocation;

    private RiderLocation rider_endCordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requesterpopup);

        this.getSupportActionBar().hide();

        setMetrics();

        avatarView = (ImageView) findViewById(R.id.avatarImage);
        usernameField = (TextView) findViewById(R.id.username);
        currentLocationField = (TextView) findViewById(R.id.currentlocation);
        endLocationField = (TextView) findViewById(R.id.endlocation);


        setUpTimes();
        choosenTime = 0;

        timechooser = (Spinner) findViewById(R.id.timechooser);
        timesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,times);
        timechooser.setAdapter(timesAdapter);
        setListenerOnSpinner(timechooser);


        acceptRequest = (Button) findViewById(R.id.accept);

        nameOfDriver = getNameOfDriver();
        driverLocation = getDriverLocation();

        rider_endCordinates = getEndCordinates();

        setAvatarImage();
        setUsernameFromIntent();
        setCurrentLocationFromIntent();

        //transform cordinates to real address
        rider_endCordinates= getEndLocationFromIntent();
        transformCorindatesToAddress(rider_endCordinates);



    }

    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("diver name") == null){
            Log.e("Driver's name problem","Problem to get name of driver from intent-> null pointer");
            return "";
        }
        return this.getIntent().getStringExtra("diver name");
    }

    public DriverLocation getDriverLocation(){
        DriverLocation driverLoc = new DriverLocation();

        double latitude = this.getIntent().getDoubleExtra("driver location_latitude",-1);
        double longitude = this.getIntent().getDoubleExtra("driver location_longitude",-1);

        if (latitude == -1 || longitude == -1){
            Log.e("Location from Intent","Problem to get location from intent -> latitude: "+latitude+"; longitude: "+longitude);
        }
        else{
            driverLoc.setDriver_latitude(latitude);
            driverLoc.setDriver_longitude(longitude);
        }
        return driverLoc;
    }

    public RiderLocation getEndCordinates(){
        RiderLocation location_from_intent = new RiderLocation();

        double latitude = this.getIntent().getDoubleExtra("endlocation_latitude",-1);
        double longitude = this.getIntent().getDoubleExtra("endlocation_longitude",-1);

        if (latitude == -1 || longitude == -1){
            Log.e("Rider's endLoc problem","Problem to get latitude and longitude from rider's end locations");
            return null;
        }

        location_from_intent.setRider_latitude(latitude);
        location_from_intent.setRider_longitude(longitude);

        return location_from_intent;
    }

    public void setAvatarImage(){
       int avatarRessource = this.getIntent().getIntExtra("avatar image",-1);

       if (avatarRessource == 1){
           Log.e("Avatar image problem","Problem to get ressource for avatar image from intetn");
           return;
       }

       this.avatarView.setImageResource(avatarRessource);
    }

    public void setUsernameFromIntent(){
        if (this.getIntent().getStringExtra("username_rider") == null){
            Log.e("Rider's name problem","Problem to get name of rider from intent");
            return;
        }

        String ridersUsername = this.getIntent().getStringExtra("username_rider");
        this.usernameField.setText(ridersUsername);
    }

    public void setCurrentLocationFromIntent(){
        if (this.getIntent().getStringExtra("current address") == null){
            Log.e("Endlocation problem","Problem to get End location from intetn-> show on null object");
            return;
        }
        String endaddress = this.getIntent().getStringExtra("current address");

        this.currentLocationField.setText(endaddress);
    }


    public RiderLocation getEndLocationFromIntent(){
        double latitude = this.getIntent().getDoubleExtra("endlocation_latitude",-1);
        double longitude = this.getIntent().getDoubleExtra("endlocation_longitude",-1);

        if (latitude != -1 && longitude != -1){
            return new RiderLocation(latitude,longitude);

        }
        else{
            rider_endCordinates = null;
            Log.e("Intent problem","Problem to get cordinates of rider's end location");
            return rider_endCordinates;
        }
    }

    public void transformCorindatesToAddress(RiderLocation rider_endlocation){
        LatLng endposition = new LatLng(rider_endlocation.getRider_latitude(),rider_endlocation.getRider_longitude());

        Geocoder geocoder = new Geocoder(RequesterPopUp.this, Locale.getDefault());
        List<Address> addresses = null;

        String streetname = "";

        try {
            addresses = geocoder.getFromLocation(endposition.latitude,endposition.longitude,1);

            if (addresses.size() > 0 && addresses != null){
                if (addresses.get(0).getThoroughfare() != null){
                    streetname += addresses.get(0).getThoroughfare();
                }
                else{
                    Log.e("Problem with streetname","Can not get streetname from geocoder-> NullPointerException or empty");
                }
            }
            else{
                Log.e("List of addresses","Can not get addresses from geocoder");
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

        endLocationField.setText(streetname);
    }

    public void setListenerOnSpinner(Spinner timesspinner){
        timesspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        choosenTime = times[0];
                        break;
                    case 1:
                        choosenTime = times[1];
                        break;
                    case 2:
                        choosenTime = times[2];
                        break;
                    case 3:
                        choosenTime = times[3];
                        break;
                    default:
                        Log.e("Spinner problem","Can not choose time for picking");
                        choosenTime = 0;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setUpTimes(){
        this.times = new Integer[4];

        times[0] = 15;
        times[1] = 30;
        times[2] = 45;
        times[3] = 60;
    }


    public void setMetrics(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*0.9),(int) (height*0.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }
}