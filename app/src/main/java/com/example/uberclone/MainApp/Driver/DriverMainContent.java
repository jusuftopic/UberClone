package com.example.uberclone.MainApp.Driver;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberclone.Modules.Requests.DriverLocation;
import com.example.uberclone.Modules.Requests.RiderLocation;
import com.example.uberclone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverMainContent extends FragmentActivity implements OnMapReadyCallback{

    private String nameOfDriver;

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private ArrayList<String> riders_requesters;

    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;

    private ArrayList<RiderLocation> ridersCurrentLocations;

    private Location lastKnownLocation;

    private Button acceptRequestButton;
    private Button showInListButton;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (getLastKnownLocation() != null){
                        lastKnownLocation = getLastKnownLocation();
                        updateLocation(lastKnownLocation);
                        addRiderInDatabaseNoAccepted(lastKnownLocation,false);
                    }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main_content);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nameOfDriver = getNameOfDriver();
        Log.i("DriverMainContent name"," "+nameOfDriver);

      /* getRequestedUsers();

        latitudes = getLatitudes();
        longitudes = getLongitudes();

        ridersCurrentLocations = formRidersCurrentLocation();

        acceptRequestButton = (Button) findViewById(R.id.acceptRequest);
        showInListButton = (Button) findViewById(R.id.viewInList);*/

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocation(location);
                lastKnownLocation = location;
            }
            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            if (getLastKnownLocation() != null){
                lastKnownLocation = getLastKnownLocation();
                updateLocation(lastKnownLocation);
                addRiderInDatabaseNoAccepted(lastKnownLocation,false);
                setRidersLocationInMap();
            }
        }

        mMap.setOnMapClickListener(latLng -> Toast.makeText(DriverMainContent.this,"Marker clicked",Toast.LENGTH_LONG).show());
    }

    public void addRiderInDatabaseNoAccepted(Location location, boolean accpeted){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DriverLocation driverLocation = new DriverLocation(location.getLatitude(),location.getLongitude(),false);

                    root.child("Requests").child("Driver's Acceptance").child(nameOfDriver).child("Current location").setValue(driverLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("Driver's location: ","Succesefull added");
                        }
                    });

                }
                else{
                    Log.e("Probelm with path",snapshot.getValue()+" doent't exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DATABASE PROBELEM",error.getMessage());
            }
        });
    }


    public void updateLocation(Location location){
        if (location != null) {
            LatLng current_position = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().title("My location").position(current_position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_position, 15f));
        }
    }

    public Location getLastKnownLocation(){
       if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
           return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       }
       return null;
    }

    public void setRidersLocationInMap(){
        if (riders_requesters.size() == ridersCurrentLocations.size()){
            for (int i = 0; i < riders_requesters.size(); i++){
                showMarkerOnMap(riders_requesters.get(i),ridersCurrentLocations.get(i));
            }
        }
        else{
            Log.i("Lists not equals","Size of rider's names ("+riders_requesters.size()+") not equal number of current locations ("+ridersCurrentLocations+")");
        }
    }

    public void showMarkerOnMap(String username, RiderLocation riderLocation){
        LatLng position = new LatLng(riderLocation.getRider_latitude(),riderLocation.getRider_longitude());
        mMap.addMarker(new MarkerOptions().title(username).position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    public void getRequestedUsers(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider calls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    ArrayList<String> usersFromDatabase = new ArrayList<>();

                    Log.i("Requested ridders",String.valueOf(snapshot.getChildrenCount()));

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        usersFromDatabase.add(String.valueOf(dataSnapshot));
                    }

                    if (!usersFromDatabase.isEmpty()){
                        Log.i("Test requests",usersFromDatabase.get(0));
                    }
                    else{
                        Toast.makeText(DriverMainContent.this,"Empty requests",Toast.LENGTH_LONG).show();
                    }

                    riders_requesters = usersFromDatabase;

                }
                else {
                    Log.e("Snapshot problem: ","Doesn't exists");
                }
            }

            public void getUsersFromDatabas(FireBaseCallback fireBaseCallback){
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference root = firebaseDatabase.getReference();

                root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                        }
                        else{
                            Log.i("Database problem","can't find path");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Driver Main: ",error.getMessage());
            }
        });

    }

    private ArrayList<Double> getLatitudes(){
        ArrayList<Double> latitudes_from_database = new ArrayList<>();

        for (String riderreqester : riders_requesters){
            double latitude_from_database = getLatitudeFromDatabase(riderreqester);
            latitudes_from_database.add(latitude_from_database);
        }
        return latitudes_from_database;
    }

    public double getLatitudeFromDatabase(String username){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        final double[] currentlocation_cords = new double[1];

        root.child("Requests").child("Rider Calls").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    root.child("Requests").child("Rider Calls").child(username).child("Current location").child("rider_latitude").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                currentlocation_cords[0] = Double.parseDouble(String.valueOf(snapshot.getValue()));
                                Log.i("Rider latitude",String.valueOf(currentlocation_cords[0]));
                            }
                            else {
                                Log.e("Driver main","Problem with rider current location-latitude");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Log.e("Latitude problem",username+" didn't recognized");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return currentlocation_cords[0];
    }

    private ArrayList<Double> getLongitudes(){
        ArrayList<Double> longitudes_from_database = new ArrayList<>();

        for (String riderreqester : riders_requesters){
            double longitude_from_database = getLongitudeFromDatabase(riderreqester);
            longitudes_from_database.add(longitude_from_database);
        }
        return longitudes;
    }


    public double getLongitudeFromDatabase(String username){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        final double[] currentlocation_cords = new double[1];

        root.child("Requests").child("Rider Calls").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    root.child("Requests").child("Rider Calls").child(username).child("Current location").child("rider_longitude").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                currentlocation_cords[0] = Double.parseDouble(String.valueOf(snapshot.getValue()));
                            }
                            else {
                                Log.e("Driver main","Problem with rider current location-longitude");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Log.e("Latitude problem",username+" didn't recognized");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return currentlocation_cords[0];
    }

    private ArrayList<RiderLocation> formRidersCurrentLocation(){
        ArrayList<RiderLocation> formedlist = new ArrayList<>();

        if (latitudes.size() == longitudes.size()){
            for (int i = 0; i < longitudes.size(); i++){
                RiderLocation riderLocation = new RiderLocation(latitudes.get(i),longitudes.get(i));
                formedlist.add(riderLocation);
            }
        }
        else{
            Log.e("DriverMain","Not equal number of latitudes ("+latitudes.size()+") and longitudes ("+longitudes.size()+") in database");
        }
       return formedlist;
    }


    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("drivername from cardetails") == null){
            if (this.getIntent().getStringExtra("drivername from login") != null){
                return this.getIntent().getStringExtra("drivername from login");
            }
        }
        return null;
    }

}

