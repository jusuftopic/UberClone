package com.example.uberclone.MainApp.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberclone.MainApp.MainDriver;
import com.example.uberclone.Models.Requests.DriverLocation;
import com.example.uberclone.Models.Requests.RiderLocation;
import com.example.uberclone.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
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

    private RiderLocation rider_currentCordinates;
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

       // rider_endCordinates = getEndCordinates();

        setAvatarImage();
        setUsernameFromIntent();
        setCurrentLocationFromIntent();

        rider_currentCordinates = getCurrentLocationCordinates();
        //transform cordinates to real address
        rider_endCordinates= getEndLocationFromIntent();
        transformCorindatesToAddress(rider_endCordinates);

        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!choosenTime.equals("")){
                    changeDriversAcceptanceStatus(nameOfDriver,rider_currentCordinates,rider_endCordinates);
                   // deleteRiderFromRequests(String.valueOf(usernameField.getText()));
                    mergeDriverAndRider(nameOfDriver,driverLocation,String.valueOf(usernameField.getText()),rider_currentCordinates,rider_endCordinates);

                }
                else{
                    Toast.makeText(RequesterPopUp.this,"Please choose the time at which you will pick up the passenger",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void mergeDriverAndRider(String driverName,DriverLocation driverLocation,String riderName,RiderLocation currentLocation,RiderLocation endLocation){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Accepted requests").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.getChildrenCount() < 2){
                        root.child("Requests").child("Accepted requests").child(driverName).child("Driver's location").setValue(driverLocation);
                        root.child("Requests").child("Accepted requests").child(driverName).child(riderName).child("Current location").setValue(currentLocation);
                        root.child("Requests").child("Accepted requests").child(driverName).child(riderName).child("End location").setValue(endLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("SUCCESEFULL","ADDED ACCEPTED PATH IN POP UP");

                                Intent toMainDriver = new Intent(RequesterPopUp.this, MainDriver.class);
                                toMainDriver.putExtra("driver name from accept",nameOfDriver);
                                startActivity(toMainDriver);
                            }
                        });
                    }
                    else{
                        Log.e("FAILED",nameOfDriver+" has already accpeted request");
                    }
                }
                else{
                    Log.e("Path problem","Failed to find request path");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void changeDriversAcceptanceStatus(String nameOfDriver,RiderLocation rider_currentCordinates,RiderLocation rider_endCordinates){
        if (rider_currentCordinates != null && rider_endCordinates != null){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference root = firebaseDatabase.getReference();

            root.child("Requests").child("Driver's Acceptance").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        double driver_latitude = driverLocation.getDriver_latitude();
                        double driver_longitude = driverLocation.getDriver_longitude();
                        boolean acceptedCall = true;
                        RiderLocation rider_current = rider_currentCordinates;
                        RiderLocation rider_end = rider_endCordinates;

                        DriverLocation location_accpet = new DriverLocation(driver_latitude,driver_longitude,acceptedCall,rider_current,rider_end);

                        root.child("Requests").child("Driver's Acceptance").child(nameOfDriver).child("Current location").setValue(location_accpet).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(RequesterPopUp.this,"Request acceped",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        Log.e("Driver's path problem",nameOfDriver+" doesn't exists in database");
                    }
                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error) {
                    Log.e("Database error",error.getMessage());
                }
            });
        }
        else{
            Log.e("FAIL from intent","Can not change driver's status because current or end rider's location null");
        }
    }

    public void deleteRiderFromRequests(String username){
        if (username != null && !username.equals("")){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference root = firebaseDatabase.getReference();

            root.child("Requests").child("Rider Calls").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                       // root.child("Requests").child("Rider Calls").child(username).setValue(null);
                        Log.i("Deleted rider",username+" deleted from requests list");
                    }
                    else{
                        Log.e("Database problem: ","Can't find path to username: "+username);
                    }
                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error) {

                }
            });
        }
        else{
            Log.e("Rider name","Failed to detele rider from requests-> Username didn't recognized");
        }
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

    public RiderLocation getCurrentLocationCordinates(){
        double latitude = getIntent().getDoubleExtra("currentlocation_latitude",-1);
        double longitude = getIntent().getDoubleExtra("currentlocation_longitude",-1);

        if (latitude != -1 && longitude != -1){
            return new RiderLocation(latitude,longitude);
        }
        else {
            Log.e("Current cordinates","Problem to trasport current rider's cordinates from intent");
            return null;
        }
    }


    public RiderLocation getEndLocationFromIntent(){
        double latitude = this.getIntent().getDoubleExtra("endlocation_latitude",-1);
        double longitude = this.getIntent().getDoubleExtra("endlocation_longitude",-1);

        if (latitude != -1 && longitude != -1){
            return new RiderLocation(latitude,longitude);

        }
        else{
            Log.e("Intent problem","Problem to get cordinates of rider's end location");
            return null;
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