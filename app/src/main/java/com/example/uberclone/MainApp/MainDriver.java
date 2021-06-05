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
import android.widget.Toast;

import com.example.uberclone.MainApp.CallBacks.Rider.CurrentLocationCallBack;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainDriver extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String nameOfDriver;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Marker driverMarker;
    private Marker riderMarker;

    private RiderLocation currentRiderLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Location lastKnownLocation = getLastKnownLocation();
                    updateLocation(lastKnownLocation);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nameOfDriver = getNameOfDriver();
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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocation(location);
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

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            Location lastKnownLocation = getLastKnownLocation();
            updateLocation(lastKnownLocation);
            getRiderCurrentLocation(new CurrentLocationCallBack() {
                @Override
                public void onCurrentLocationCallBack(RiderLocation currentRiderLocation) {

                }

                @Override
                public void onCurrentLocationCallBackWihtUsername(String username, RiderLocation riderLocation) {
                    setUpMarker(username,riderLocation);
                }
            });


        }
    }

    public void getRiderCurrentLocation(CurrentLocationCallBack currentLocationCallBack){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Accpeted requests").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.i("Number of children",String.valueOf(snapshot.getChildrenCount()));

                    if (snapshot.getChildrenCount() == 2){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if (!dataSnapshot.getKey().equalsIgnoreCase("Driver's location")){
                                double rider_latitude = (double) dataSnapshot.child("Current location").child("rider_latitude").getValue();
                                double rider_longitude = (double) dataSnapshot.child("Current location").child("rider_latitude").getValue();

                                currentRiderLocation = new RiderLocation(rider_latitude,rider_longitude);

                                currentLocationCallBack.onCurrentLocationCallBackWihtUsername(dataSnapshot.getKey(),currentRiderLocation);

                            }
                        }
                    }
                    else{
                        Log.e("Accept failed","Failed to accept user on right way");
                    }
                }
                else{
                    Log.e("FAILED","Failed to find path to driver in accepted requests");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void setUpMarker(String username, RiderLocation currentRiderLocation){
        LatLng currentRiderPosition = new LatLng(currentRiderLocation.getRider_latitude(),currentRiderLocation.getRider_longitude());
        riderMarker = mMap.addMarker(new MarkerOptions().position(currentRiderPosition).title(username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


    }

    public void updateLocation(Location location){
        LatLng driverPosition = new LatLng(location.getLatitude(),location.getLongitude());

        driverMarker = mMap.addMarker(new MarkerOptions().position(driverPosition).title("My location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverPosition,5f));


    }

    public Location getLastKnownLocation(){
        Location lastKnownLoc = null;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            lastKnownLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (lastKnownLoc == null){
            Toast.makeText(MainDriver.this,"Can not get your location",Toast.LENGTH_LONG).show();
        }

        return lastKnownLoc;
    }

    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("driver name from accept") == null || this.getIntent().getStringExtra("driver name from accept").equalsIgnoreCase("")){
            Log.e("Intent failed","Failed to pass name of driver in intent");
            return null;
        }

       return this.getIntent().getStringExtra("driver name from accept");
    }
}