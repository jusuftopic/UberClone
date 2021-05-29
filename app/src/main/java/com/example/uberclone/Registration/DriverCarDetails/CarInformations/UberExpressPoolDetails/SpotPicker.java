package com.example.uberclone.Registration.DriverCarDetails.CarInformations.UberExpressPoolDetails;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.uberclone.R;
import com.example.uberclone.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SpotPicker extends FragmentActivity implements OnMapReadyCallback, Serializable {

    private GoogleMap mMap;
    private ActivityMainBinding binding;

    private String nameOfDriver;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private ArrayList<Location> spots;
    private int markerCounter;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull  int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 1){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                setLastKnownLocation();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.spotmap);
        mapFragment.getMapAsync(this);

        nameOfDriver = getNameOfDriver();

        spots = new ArrayList<>();
        markerCounter = 0;
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

        locationManager =(LocationManager) this.getSystemService(LOCATION_SERVICE);
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

        checkLocationPerission();

        addSpotPoints();
    }

    public void checkLocationPerission(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            setLastKnownLocation();
        }
    }

    public void addSpotPoints(){
        if (markerCounter <= 5){
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    String address = getAddressFromLatLang(latLng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    markerCounter = markerCounter + 1;
                    addLatLangToList(latLng);
                }
            });
        }
    }

    public void sendAddresses(View view){
        if (this.spots.size() == 5){
            Intent toCarInfosActivity = new Intent(SpotPicker.this,UberExpressPoolDetails.class);
            toCarInfosActivity.putExtra("spot picked",true);
            toCarInfosActivity.putExtra("DRIVER'S NAME PICKER",nameOfDriver);

            Bundle args = new Bundle();
            args.putSerializable("SPOTLIST",(Serializable) spots);
            toCarInfosActivity.putExtra("BUNDLE",args);

            startActivity(toCarInfosActivity);


        }
        else{
            Toast.makeText(SpotPicker.this,"Please choose 5 spots, where you want to pick passengers",Toast.LENGTH_LONG).show();
        }
    }

    public String getAddressFromLatLang(LatLng latLng){
        String address = "";
        Geocoder geocoder = new Geocoder(SpotPicker.this, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            if (addresses.size() > 0 && addresses != null){
                if (addresses.get(0).getThoroughfare() != null){
                    address += addresses.get(0).getThoroughfare();
                }
            }
        }
        catch (IOException ioException){
            ioException.printStackTrace();
        }

        if (address.equalsIgnoreCase("")){
            Log.e("Address failed","Failed to get address from geocoder");
        }
        return address;
    }

    public void addLatLangToList(LatLng latLng){
        Location newSpotPointLocation = new Location(LocationManager.GPS_PROVIDER);
        newSpotPointLocation.setLatitude(latLng.latitude);
        newSpotPointLocation.setLongitude(latLng.longitude);

        this.spots.add(newSpotPointLocation);
    }

    public void updateLocation(Location location){
        LatLng position = new LatLng(location.getLatitude(),location.getLongitude());

        mMap.addMarker(new MarkerOptions().position(position).title("My position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,5f));
    }

    public void setLastKnownLocation(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null){
                updateLocation(lastKnownLocation);
            }
            else{
                Log.e("Last known location","Problem to get last known location");
            }
        }
    }

    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("driver name for spots") != null && !this.getIntent().getStringExtra("driver name for spots").equalsIgnoreCase("")){
            return this.getIntent().getStringExtra("driver name for spots");
        }

        Log.e("Name of rider","Problem to get name of rider from intetn");
        return null;
    }

}