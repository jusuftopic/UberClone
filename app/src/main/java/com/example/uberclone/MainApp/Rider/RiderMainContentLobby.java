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
import android.widget.ImageButton;

import com.example.uberclone.MainApp.MainRider;
import com.example.uberclone.MainApp.Rider.RiderPayment.RidePayment;
import com.example.uberclone.MainApp.Rider.Settings.SettingsMenu;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderMainContentLobby extends FragmentActivity implements OnMapReadyCallback {

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

    private ImageButton settingsButton;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location lastKnownLocation = getLastKnownLocation();
                    if (lastKnownLocation != null) {
                        setCurrentLocation(lastKnownLocation);
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
                .findFragmentById(R.id.spotmap);
        mapFragment.getMapAsync(this);

        currentlocation = new RiderLocation();
        endlocation = new RiderLocation();


        callUber = (Button) findViewById(R.id.sendRequest);
        callUber.setEnabled(false);


        nameOfRider = getNameOfRider();
        checkIfTheRequestExists(nameOfRider);
        isCalled = false;

        numberOfMarkers = 1;

        settingsButton = (ImageButton) findViewById(R.id.settingButton);

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

                    callUber.setEnabled(true);
                }
            }
        });
    }

    public void callUberOnDrive(View view) {
        if (String.valueOf(callUber.getText()).equalsIgnoreCase("Call uber")) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Please wait...");

            addLastRequestedLocation(currentlocation,endlocation);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            changeButtonInfos(true, "Cancle call");

            Intent toPickCar = new Intent(RiderMainContentLobby.this, RideInformations.class);
            toPickCar.putExtra("username from ridermain", nameOfRider);
            startActivity(toPickCar);

        } else {
            setDialog();
        }
    }

    public void addLastRequestedLocation(RiderLocation currentlocation, RiderLocation endlocation){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Rider").child(nameOfRider).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){


                    root.child("User").child("Rider").child(nameOfRider).child("Location").child("Current Location").setValue(currentlocation)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                 Log.i("Succesefull","Added rider current location["+currentlocation.toString()+"] to database");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    Log.e("Failed","Failed to add user current location ["+currentlocation.toString()+"] to database");
                                    e.printStackTrace();
                                }
                            });
                    root.child("User").child("Rider").child(nameOfRider).child("Location").child("End Location").setValue(endlocation)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i("Succesefull","Added rider's end location["+endlocation.toString()+"] to database");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    Log.e("Failed","Failed to add user end location ["+currentlocation.toString()+"] to database");
                                    e.printStackTrace();
                                }
                            });

                }
                else{
                    Log.e("Path failed","Failed to find "+nameOfRider+" in database");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void changeButtonInfos(boolean called, String message) {
        isCalled = called;
        callUber.setText(message);
    }

    public void goToSettings(View view){
        Intent toSettings = new Intent(RiderMainContentLobby.this, SettingsMenu.class);
        toSettings.putExtra("RIDER_LOBBY-name of rider",nameOfRider);
        startActivity(toSettings);
    }

    public void setDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RiderMainContentLobby.this)
                .setTitle("Cancle Uber?")
                .setMessage("You already requested Uber.\nDo yo want to cancle and call again?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRequestFromDatabase(nameOfRider);
                        deleteRequestFromActiveCalls(nameOfRider);
                        changeButtonInfos(false, "Call Uber");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeButtonInfos(true, "Cancle Uber");
                        Location lastKnownLocation = getLastKnownLocation();
                        if (lastKnownLocation != null) {
                          //  addCurrentLocationInDatabase(lastKnownLocation);
                        }
                        addMarkerOfUsersEndLocation(nameOfRider);
                        callUber.setEnabled(false);

                        Intent toMainRider = new Intent(RiderMainContentLobby.this, MainRider.class);
                        toMainRider.putExtra("existed requester",nameOfRider);
                        startActivity(toMainRider);
                    }
                });

        dialogBuilder.create().show();
    }

    public void addMarkerOfUsersEndLocation(String ridersUsername){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Rider").child(nameOfRider).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double endLocationLatitude = (double) snapshot.child("Location").child("End Location").child("rider_latitude").getValue();
                    double endLocationLongitude = (double) snapshot.child("Location").child("End Location").child("rider_longitude").getValue();

                    setMarkerOnAlreadyRequestedRiders(ridersUsername,endLocationLatitude,endLocationLongitude);
                }
                else{
                    Log.e("Path failed","Failed to find "+nameOfRider+" in database");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });


    }

    public void setMarkerOnAlreadyRequestedRiders(String username,double latitude, double longitude){
        LatLng endPosition = new LatLng(latitude,longitude);

        mMap.addMarker(new MarkerOptions().position(endPosition).title(username).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(endPosition));
    }


    public void deleteRequestFromDatabase(String rider_username) {
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
       DatabaseReference root = firebaseDatabase.getReference();

       root.child("User").child("Rider").child(rider_username).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull  DataSnapshot snapshot) {
               if (snapshot.exists()){
                   root.child("User").child("Rider").child(rider_username).child("Location").setValue(null)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Log.i("Location deleted","Succesefull deleted location for rider");
                               }
                           })
                           .addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Log.e("Loc deletion failed",e.getMessage());

                               }
                           });
               }
               else{
                   Log.e("Path fail","Failed to find rider: "+rider_username+" in database");
               }
           }

           @Override
           public void onCancelled(@NonNull  DatabaseError error) {
               Log.e("Database error",error.getMessage());

           }
       });
    }

    public void deleteRequestFromActiveCalls(String ridername){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.getChildrenCount() > 0){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if (dataSnapshot.getKey().equals(ridername)){
                                root.child("Requests").child("Rider Calls").child(String.valueOf(dataSnapshot.getKey())).setValue(null)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.i("Deletion succesefully","Succefull deletion of "+dataSnapshot.getKey()+" from database");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Deletion failed","Failed to delete "+dataSnapshot.getKey()+" from database");
                                            }
                                        });
                            }
                        }
                    }

                }
                else{
                    Log.e("Path fail","Failed to get Rider Calls path");
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }


    public void checkIfTheRequestExists(String username) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        Log.i("Users check", username);

        root.child("User").child("Rider").child(nameOfRider).child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                  //  setDialog();
                    checkIfRequestActive(username);
                } else {
                    Log.i("Location null", username + " didn't call driver yet");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error", error.getMessage());
            }
        });

    }

    public void checkIfRequestActive(String username){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    setDialog();
                }
                else{
                    Log.i("Request inactive",nameOfRider+" doens't have active requests");
                    deleteRequestFromDatabase(nameOfRider);
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database fail",error.getMessage());
            }
        });
    }

    /*
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

                        createMarker(latitude, longitude);
                    } else {
                        Log.e("Rider's end location", "Failed to get latitude and longitude from rider's end location to set marker");
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
    }*/

    public void createMarker(double latitude, double longitude) {
        LatLng endPosition = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().title("Location to drive").position(endPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(endPosition, 5f));
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