package com.example.uberclone.MainApp.Rider;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class RiderMainContent extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String nameOfRider;

    private boolean isCalled;


    private LocationManager locationManager;
    private LocationListener locationListener;

    private Button callUber;

    private Marker marker_currentlocation;
    private Marker marker_endlocation;

    private RiderLocation currentlocation;
    private RiderLocation endlocation;

    int numberOfMarkers;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location lastKnownLocation = getLastKnownLocation();
                    if (lastKnownLocation != null) {
                        setCurrentLocation(lastKnownLocation);
                        addCurrentLocationInDatabase(lastKnownLocation);
                    } else {
                        Log.e("Last location ", "Not recognized");
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main_content);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        nameOfRider = getNameOfRider();

        isCalled = false;

        numberOfMarkers = 1;

        callUber = (Button) findViewById(R.id.sendRequest);
        callUber.setEnabled(false);

        currentlocation = new RiderLocation();
        endlocation = new RiderLocation();

        handleInfosAfterCarPick();

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
                setCurrentLocation(location);
                currentlocation.setRider_latitude(location.getLatitude());
                currentlocation.setRider_longitude(location.getLongitude());
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                setCurrentLocation(lastKnownLocation);
                addCurrentLocationInDatabase(lastKnownLocation);
            } else {
                Log.e("Last location ", "Not recognized");
            }
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (numberOfMarkers <= 2) {
                    mMap.clear();
                    endlocation.setRider_latitude(latLng.latitude);
                    endlocation.setRider_longitude(latLng.longitude);


                    marker_endlocation = mMap.addMarker(new MarkerOptions().title("Location to drive").position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    callUber.setEnabled(true);
                }
            }
        });
    }

    public void callUberOnDrive(View view) {
        if (String.valueOf(callUber.getText()).equalsIgnoreCase("Call uber")) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");

            addEndLocationToDatabase(endlocation);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            changeButtonInfos(true, "Cancle call");

            Intent toPickCar = new Intent(RiderMainContent.this, ChooseUberCar.class);
            toPickCar.putExtra("username from ridermain", nameOfRider);
            startActivity(toPickCar);

        } else {
            setDialog();
        }
    }

    public void changeButtonInfos(boolean called, String message) {
        isCalled = called;
        callUber.setText(message);
    }

    public void setDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RiderMainContent.this)
                .setTitle("Cancle Uber?")
                .setMessage("You already requested Uber.\nDo yo want to cancle and call again?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRequestFromDatabase(nameOfRider);
                        changeButtonInfos(false, "Call Uber");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeButtonInfos(true, "Cancle Uber");
                    }
                });

        dialogBuilder.create().show();
    }

    public void addEndLocationToDatabase(RiderLocation location) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    root.child("Requests").child("Rider Calls").child(nameOfRider).child("End location").setValue(location).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("End location", "SUCCESEFULL ADDED");
                        }
                    });
                } else {
                    Log.e("ERROR-END LOCATION", nameOfRider + " not recognized");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR-END LOCATION", error.getMessage());
            }
        });
    }

    public void addCurrentLocationInDatabase(Location location) {
        currentlocation.setRider_latitude(location.getLatitude());
        currentlocation.setRider_longitude(location.getLongitude());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    root.child("Requests").child("Rider Calls").child(nameOfRider).child("Current location").setValue(currentlocation).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("CURRENT LOCATION ", "ADDED");
                        }
                    });

                } else {
                    Log.e("ERROR-CURRENT LOCATION ", nameOfRider + " doesn't exists");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR-CURRENT LOCATION ", error.getMessage());
            }
        });
    }

    public void deleteRequestFromDatabase(String rider_username) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(rider_username)) {
                    root.child("Requests").child("Rider Calls").child(rider_username).setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void handleInfosAfterCarPick() {
        boolean pickFinished = this.getIntent().getBooleanExtra("Back from picker", false) && this.getIntent().getBooleanExtra("picked car", false);
        String username_pick = this.getIntent().getStringExtra("name of user");

        if (pickFinished && username_pick != null) {
            nameOfRider = username_pick;
            callUber.setEnabled(true);
            changeButtonInfos(true, "Cancle call");
            setMarkerOnEndLocation(nameOfRider);
        } else {
            //TODO first ask rider if he/she wants to cancle request or not
            deleteRequestFromDatabase(nameOfRider);
        }
    }

    public void setMarkerOnEndLocation(String username) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").child(nameOfRider).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("End location").child("rider_latitude").getValue() != null && snapshot.child("End location").child("rider_longitude").getValue() != null) {
                        double latitude = Double.parseDouble(String.valueOf(snapshot.child("End location").child("rider_latitude").getValue()));
                        double longitude = Double.parseDouble(String.valueOf(snapshot.child("End location").child("rider_longitude").getValue()));

                        createMarker(latitude,longitude);
                    }
                    else{
                        Log.e("Rider's end location","Failed to get latitude and longitude from rider's end location to set marker");
                    }


                } else {
                    Log.e("Rider's name path", "Failed to find rider's path in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error", error.getMessage());
            }
        });
    }

    public void createMarker(double latitude, double longitude){
        LatLng endPosition = new LatLng(latitude,longitude);

        mMap.addMarker(new MarkerOptions().title("Location to drive").position(endPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endPosition,5f));
    }


    public Location getLastKnownLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return lastKnownLocation;
        }
        return null;
    }

    public void setCurrentLocation(Location location) {
        if (location != null) {
            LatLng currentposition = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().title("Your current location").position(currentposition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentposition));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentposition, 5f));

        }
    }

    public String getNameOfRider() {
        return this.getIntent().getStringExtra("ridername from card activity") != null ? this.getIntent().getStringExtra("ridername from card activity") : this.getIntent().getStringExtra("ridername from login");
    }

}