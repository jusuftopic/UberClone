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

import com.example.uberclone.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DriverMainContent extends FragmentActivity implements OnMapReadyCallback {

    private String nameOfDriver;

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    Location current_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateLocation(current_location);
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location current_location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateLocation(current_location);
        }
    }


    public void updateLocation(Location location){
        LatLng current_position = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().title("My location").position(current_position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_position,10f));
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

