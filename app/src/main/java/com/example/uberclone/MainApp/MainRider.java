package com.example.uberclone.MainApp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.uberclone.MainApp.CallBacks.Rider.EndLocationCallBack;
import com.example.uberclone.MainApp.CallBacks.Rider.MarkerCallBack;
import com.example.uberclone.MainApp.DirectionHelper.FetchURL;
import com.example.uberclone.MainApp.DirectionHelper.TaskLoadedCallBack;
import com.example.uberclone.Models.Requests.RiderLocation;
import com.example.uberclone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainRider extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallBack {

    private GoogleMap mMap;

    private String nameOfRider;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private RiderLocation currentRiderLocation;
    private RiderLocation endRiderLocation;

    private boolean isRequestAccepted;

    private Marker currentRiderMarker;
    private Marker driverMarker;

    private Polyline polyline;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location lastKnownLocation = getLastKnownLocation();
                    updateLocation(lastKnownLocation, nameOfRider, "START");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rider);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        nameOfRider = getNameOfRider();

        isRequestAccepted = false;
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
                updateLocation(location, nameOfRider, "START");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        handlePermission();

        setMarkerOnRiderEndLocation();

        checkIfRequestAccepted();

        drawRoute();

    }

    public void handlePermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Location lastKnownLocation = getLastKnownLocation();
            updateLocation(lastKnownLocation, nameOfRider, "START");
        }
    }

    public Location getLastKnownLocation() {

        Location lastKnwnLoc = null;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastKnwnLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (lastKnwnLoc == null) {
            Log.e("Last known loc", "PROBLEM TO GET LAST KNOWN LOCATION VIA GPS");
            return null;
        }

        return lastKnwnLoc;
    }

    public void checkIfRequestAccepted(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Accepted requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.i("Accpted requests",String.valueOf(snapshot.getChildrenCount()));

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (dataSnapshot.child(nameOfRider) != null){
                            isRequestAccepted = true;
                            setMarkerOfDriversLocation(String.valueOf(dataSnapshot.getKey()));

                        }
                    }
                }
                else{
                    Log.e("Path failed","Can not find path to Accpted requesters");
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database problem",error.getMessage());
            }
        });
    }

    public void setMarkerOfDriversLocation(String driversName){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Accepted requests").child(driversName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double latitude = (double) snapshot.child("Driver's location").child("driver_latitude").getValue();
                    double longitude = (double) snapshot.child("Driver's location").child("driver_longitude").getValue();

                    putCordinatesInMap(driversName,latitude, longitude);
                }
                else{
                    Log.e("Path failed","Failed to get driver from database in accepted requests");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void putCordinatesInMap(String driversName, double latitude, double longitude){
        if ((Double)latitude == null || (Double)longitude == null){
            Log.e("Cordinates failed","Failed to ger driver's location from database");
            return;
        }

        LatLng driversposition = new LatLng(latitude,longitude);

        driverMarker= mMap.addMarker(new MarkerOptions().position(driversposition).title("Driver").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        MarkerCallBack markerCallBack = new MarkerCallBack() {
            @Override
            public void onAddedMarker(Marker marker) {
                marker = driverMarker;
            }
        };
        Log.i("Marker added","Accepted call from "+driversName+" and marker added on map");
    }

    public void updateLocation(Location location, String username, String message) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        currentRiderMarker =  mMap.addMarker(new MarkerOptions().position(position).title(username + "\n" + message).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    public void setMarkerOnRiderEndLocation() {
        getEndRiderLocation(new EndLocationCallBack() {
            @Override
            public void onEndLocationCallBack(RiderLocation endRiderLocation) {
                LatLng endPosition = new LatLng(endRiderLocation.getRider_latitude(), endRiderLocation.getRider_longitude());
                driverMarker = mMap.addMarker(new MarkerOptions().position(endPosition).title(nameOfRider + "\n" + "END").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                Log.i("Marker added","Marked added on locations "+endPosition.toString());

            }
        });
    }

    public void getEndRiderLocation(EndLocationCallBack endLocationCallBack) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").child(nameOfRider).child("End location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double latitude = (double) snapshot.child("rider_latitude").getValue();
                    double longitude = (double) snapshot.child("rider_longitude").getValue();

                    endRiderLocation = new RiderLocation(latitude, longitude);

                    endLocationCallBack.onEndLocationCallBack(endRiderLocation);
                } else {
                    Log.e("Path problem", "Can not find path to user end location");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error", error.getMessage());
            }
        });

    }

    public String getNameOfRider() {
        if (this.getIntent().getStringExtra("existed requester") != null && !this.getIntent().getStringExtra("existed requester").equalsIgnoreCase("")) {
            return this.getIntent().getStringExtra("existed requester");
        } else {
            if (this.getIntent().getStringExtra("name of rider from payment") != null && !this.getIntent().getStringExtra("name of rider from payment").equalsIgnoreCase("")) {
                return this.getIntent().getStringExtra("name of rider from payment");
            }

            Log.e("Intent problem", "Problem to get name of rider from intent");
            return null;
        }
    }

    public void drawRoute(){
        String url = getUrl(currentRiderMarker.getPosition(),driverMarker.getPosition(),"driving");
        new FetchURL(MainRider.this).execute(url,"driving");

    }

    private String getUrl(LatLng origin,LatLng dest,String directionMode){
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        String str_destination = "destionation="+dest.latitude+","+dest.longitude;

        String direct_mode = "mode= "+directionMode;

        String parameters = str_origin+"&"+str_destination+"&"+direct_mode;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key="+getString(R.string.google_maps_key);

        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (polyline != null){
            polyline.remove();
        }
        mMap.addPolyline((PolylineOptions) values[0]);
    }
}