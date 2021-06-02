package com.example.uberclone.MainApp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.uberclone.MainApp.CallBacks.Rider.CurrentLocationCallBack;
import com.example.uberclone.MainApp.CallBacks.Rider.EndLocationCallBack;
import com.example.uberclone.Models.Requests.RiderLocation;
import com.example.uberclone.R;
import com.example.uberclone.databinding.ActivityRiderDriverMeetingBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RiderDriverMeeting extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityRiderDriverMeetingBinding binding;

    private String nameOfRider;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private RiderLocation currentLocation;
    private RiderLocation endLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getCurrentRiderLocation(new CurrentLocationCallBack() {
                        @Override
                        public void onCurrentLocationCallBack(RiderLocation riderLocation) {
                            updateLocation(changeRiderLocationToLocation(riderLocation),nameOfRider,"START");
                        }
                    });

                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_driver_meeting);


      //  binding = ActivityRiderDriverMeetingBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nameOfRider = getNameOfRider();

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
                updateLocation(location,nameOfRider,"START");

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
            getCurrentRiderLocation(new CurrentLocationCallBack() {
                @Override
                public void onCurrentLocationCallBack(RiderLocation currentRiderLocation) {
                    updateLocation(changeRiderLocationToLocation(currentRiderLocation),nameOfRider,"START");
                }
            });
        }

       getEndRiderLocation(new EndLocationCallBack() {
            @Override
            public void onEndLocationCallBack(RiderLocation endRiderLocation) {
                updateLocation(changeRiderLocationToLocation(endRiderLocation),nameOfRider,"END");
            }
        });

    }

    public void updateLocation(Location location,String username,String message){
        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().title(message+"\n"+username+"\n"+getAddressOfLocation(location)).position(currentPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,20f));
    }

    public String getAddressOfLocation(Location location){
        Geocoder geocoder = new Geocoder(RiderDriverMeeting.this, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();

        String address = "";

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (addresses.size() > 0 && addresses != null){
                if (addresses.get(0).getThoroughfare() != null){
                    address += addresses.get(0).getThoroughfare();
                }
            }
            else{
                Log.e("Addresses problem","Can't catch address for this location");
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

        return address;
    }

    public void getCurrentRiderLocation(CurrentLocationCallBack currentLocationCallBack){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").child(nameOfRider).child("Current location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double latitude = (double) snapshot.child("rider_latitude").getValue();
                    double longitude = (double) snapshot.child("rider_longitude").getValue();

                    currentLocation = new RiderLocation(latitude,longitude);

                    currentLocationCallBack.onCurrentLocationCallBack(currentLocation);
                }
                else{
                    Log.e("Current loc error","Can not find path to user's currentlocation");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void getEndRiderLocation(EndLocationCallBack endLocationCallBack){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").child(nameOfRider).child("End location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double latitude = (double) snapshot.child("rider_latitude").getValue();
                    double longitude = (double) snapshot.child("rider_longitude").getValue();

                    endLocation = new RiderLocation(latitude,longitude);

                   endLocationCallBack.onEndLocationCallBack(endLocation);
                }
                else{
                    Log.e("End loc error","Can not find path to user's end location");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public Location changeRiderLocationToLocation(RiderLocation riderLocation){
        Location transformedLocation = new Location(LocationManager.GPS_PROVIDER);
        transformedLocation.setLatitude(riderLocation.getRider_latitude());
        transformedLocation.setLongitude(riderLocation.getRider_longitude());

        return transformedLocation;
    }
    public String getNameOfRider(){
        if (this.getIntent().getStringExtra("name of rider from payment") != null && !this.getIntent().getStringExtra("name of rider from payment").equalsIgnoreCase("")){
            return this.getIntent().getStringExtra("name of rider from payment");
        }

        Log.e("Intent problem: ","Problem to get name of rider in main rider/driver activity");
        return null;
    }
}