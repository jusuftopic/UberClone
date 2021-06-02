package com.example.uberclone.MainApp.Driver;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberclone.MainApp.CallBacks.Rider.CurrentLocationCallBack;
import com.example.uberclone.MainApp.CallBacks.Rider.EndLocationCallBack;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.Driver.FireBaseCallBackDriverCar;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackLatitude;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackLongitude;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackUsername;
import com.example.uberclone.MainApp.RiderDriverMeeting;
import com.example.uberclone.Models.Requests.DriverLocation;
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

import java.util.ArrayList;

public class DriverMainContent extends FragmentActivity implements OnMapReadyCallback {

    private String nameOfDriver;

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private ArrayList<String> riders_requesters;

    private ArrayList<java.lang.Double> latitudes;
    private ArrayList<java.lang.Double> longitudes;

    private ArrayList<RiderLocation> ridersCurrentLocations;

    private RiderLocation acceptedRiderCurrentLocation;
    private RiderLocation acceptedRiderEndLocation;

    private Location lastKnownLocation;

    private Button acceptRequestButton;
    private Button showInListButton;


    private String acceptedRider;
    private LatLng acceptedLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (getLastKnownLocation() != null) {
                    lastKnownLocation = getLastKnownLocation();
                    updateLocation(lastKnownLocation);
                    addDriverInDatabaseNoAccepted(lastKnownLocation);
                  //  setMarkersOnMap();
                    //setClickListenerOnMarker();
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
                .findFragmentById(R.id.spotmap);
        mapFragment.getMapAsync(this);

        nameOfDriver = getNameOfDriver();
        Log.i("Name of driver",nameOfDriver);

        riders_requesters = new ArrayList<>();
        latitudes = new ArrayList<java.lang.Double>();
        longitudes = new ArrayList<java.lang.Double>();

        ridersCurrentLocations = new ArrayList<>();

        acceptRequestButton = (Button) findViewById(R.id.acceptRequest);
        acceptRequestButton.setEnabled(false);

        showInListButton = (Button) findViewById(R.id.viewInList);

        acceptedRider = "";
        acceptedLocation = new LatLng(-1,-1);




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

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            if (getLastKnownLocation() != null) {
                lastKnownLocation = getLastKnownLocation();
                updateLocation(lastKnownLocation);
                addDriverInDatabaseNoAccepted(lastKnownLocation);
                setMarkersOnMap();
              //  setClickListenerOnMarker();
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (!marker.getTitle().equals("My location")){
                            acceptedRider = marker.getTitle();
                            acceptedLocation = marker.getPosition();
                            acceptRequestButton.setEnabled(true);
                        }

                        return true;
                    }
                });
            }
        }
    }

    public void onAcceptRequest(View view){
        if (acceptRequestButton.isEnabled()){
            if (acceptedLocation.latitude != -1 && acceptedLocation.longitude != -1){
                acceptRequest(acceptedLocation);
                mergeDriverAndRider(nameOfDriver,getDriverLocation(getLastKnownLocation()),acceptedRider);

            }
            else{
                Log.e("LatLng problem: ",acceptedLocation.toString() + " didn't recognized in on marker click");
            }
        }
        else{
            Toast.makeText(DriverMainContent.this,"First select drive, you want to accept",Toast.LENGTH_LONG).show();
        }
    }

    public void mergeDriverAndRider(String driverName, DriverLocation driverLocation,String riderName){
        getAcceptedRiderCurrentLocation(new CurrentLocationCallBack() {
            @Override
            public void onCurrentLocationCallBack(RiderLocation currentRiderLocation) {
                getAcceptedRiderEndLocation(new EndLocationCallBack() {
                    @Override
                    public void onEndLocationCallBack(RiderLocation endRiderLocation) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference root = firebaseDatabase.getReference();

                        root.child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    root.child("Requests").child("Accepted requests").child(driverName).child("Driver's location").setValue(driverLocation).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Log.e("Failed","Failed to add driver "+driverName+" to accepted requests");
                                            e.printStackTrace();
                                        }
                                    });

                                    root.child("Requests").child("Accepted requests").child(driverName).child(riderName).child("Current location").setValue(currentRiderLocation).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Failed","Failed to add rider "+riderName+" to accepted requests- current location"+currentRiderLocation.toString());
                                            e.printStackTrace();

                                        }
                                    });
                                    root.child("Requests").child(driverName).child(riderName).child("End location").setValue(endRiderLocation).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull  Exception e) {
                                            Log.e("Failed","Failed to add rider "+riderName+" to accepted requests- current location"+endRiderLocation.toString());
                                            e.printStackTrace();
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("Succesefull","Created new path to accepted requests");
                                        }
                                    });
                                }
                                else{
                                    Log.e("Failed","Failed to find requsts path");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull  DatabaseError error) {
                                Log.e("Database error",error.getMessage());
                            }
                        });
                    }
                });
            }
        });
    }


    public void acceptRequest(LatLng acceptedLoc){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        double latitude = acceptedLoc.latitude;
        double longitude = acceptedLoc.longitude;

        RiderLocation aRiderLocation = new RiderLocation(latitude,longitude);
        DriverLocation driverLocationAccept = new DriverLocation(true, aRiderLocation);

        root.child("Requests").child("Driver's Acceptance").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    root.child("Requests").child("Driver's Acceptance").child(nameOfDriver).child("AcceptedRequest").setValue(driverLocationAccept).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                         Log.i("User Accepted","Driver: "+nameOfDriver+" accpeted rider location");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("accept request failed","Failed to set in driver path accepted request");
                        }
                    });
                }
                else{
                    Log.e("Problem with name", nameOfDriver +" didn't recognized in acceptRequstMethod");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database problem",error.getMessage());
            }
        });
    }

    public void deleteRiderFromRequests(String username){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").child(username).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("User deleted",username+" deletef from requests in database");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Log.e("Delete failed",username+" could not be deleted from database");
            }
        });

    }

    public void showListOfNearestLocations(View view){
        Intent toListOfRequests = new Intent(DriverMainContent.this,ShowNearestRequesters.class);
        toListOfRequests.putExtra("driver name from main",nameOfDriver);
        startActivity(toListOfRequests);
    }

    public void setMarkersOnMap() {
        getRiderRequesters(new FireBaseCallbackUsername() {
            @Override
            public void onCallbackUsername(ArrayList<String> requestes) {
                getRiderLatitudes(new FireBaseCallbackLatitude() {
                    @Override
                    public void onCallbackLatitude(ArrayList<java.lang.Double> latitudes) {
                        getRiderLongitude(new FireBaseCallbackLongitude() {
                            @Override
                            public void onCallBackLongitude(ArrayList<Double> longitudes) {
                                if (requestes.size() == latitudes.size() && latitudes.size() == longitudes.size()) {
                                    Log.i("Size check", "Requesters: " + requestes.size() + "\nLatitudes: " + latitudes.size() + "\nLongitudes: " + longitudes.size());

                                    for (int i = 0; i < requestes.size(); i++) {
                                        Log.i("Marker to add", requestes.get(i) + " Position: " + latitudes.get(i) + ":" + longitudes.get(i));
                                        LatLng position = new LatLng(latitudes.get(i), longitudes.get(i));
                                        mMap.addMarker(new MarkerOptions().position(position).title(requestes.get(i)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                                    }

                                } else {
                                    Log.e("Problem with callback", "List not same size->Requesters: " + requestes.size() + "\nLatitudes: " + latitudes.size() + "\nLongitudes: " + longitudes.size());
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    public void addDriverInDatabaseNoAccepted(Location location) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DriverLocation driverLocation = new DriverLocation(false, location.getLatitude(), location.getLongitude());

                    root.child("Requests").child("Driver's Acceptance").child(nameOfDriver).child("Current location").setValue(driverLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("Driver's location: ", "Succesefull added");
                        }
                    });

                } else {
                    Log.e("Probelm with path", snapshot.getValue() + " doent't exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DATABASE PROBELEM", error.getMessage());
            }
        });
    }

    public DriverLocation getDriverLocation(Location location){
        return new DriverLocation(true,location.getLongitude(),location.getLongitude());
    }

    public void updateLocation(Location location) {
        if (location != null) {
            LatLng current_position = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().title("My location").position(current_position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current_position));
        }
    }

    public Location getLastKnownLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }

    public void getAcceptedRiderCurrentLocation(CurrentLocationCallBack currentLocationCallBack){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").child(acceptedRider).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double latitude = (double) snapshot.child("Current location").child("rider_latitude").getValue();
                    double longitude = (double) snapshot.child("Current location").child("rider_latitude").getValue();

                    acceptedRiderCurrentLocation = new RiderLocation(latitude,longitude);

                    currentLocationCallBack.onCurrentLocationCallBack(acceptedRiderCurrentLocation);

                }
                else{
                    Log.e("Accepted rider","Failed to find accepted rider on marker click");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void getAcceptedRiderEndLocation(EndLocationCallBack endLocationCallBack){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").child(acceptedRider).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    double latitude = (double) snapshot.child("End location").child("rider_latitude").getValue();
                    double longitude = (double) snapshot.child("End location").child("rider_latitude").getValue();

                    acceptedRiderEndLocation = new RiderLocation(latitude,longitude);

                    endLocationCallBack.onEndLocationCallBack(acceptedRiderEndLocation);

                }
                else{
                    Log.e("Accepted rider","Failed to find accepted rider on marker click");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void getRiderRequesters(FireBaseCallbackUsername fireBaseCallback) {
       getDriverCategory(new FireBaseCallBackDriverCar() {
           @Override
           public void onCallBackDriverCarCategory(String driversCarCategory) {
               FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
               DatabaseReference root = firebaseDatabase.getReference();

               root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()) {
                           for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                               String pickedCar = String.valueOf(dataSnapshot.child("Picked car").getValue());

                               if (!dataSnapshot.getKey().equals("test") && pickedCar.equalsIgnoreCase(driversCarCategory)){
                                   riders_requesters.add(String.valueOf(dataSnapshot.getKey()));}
                           }

                           fireBaseCallback.onCallbackUsername(riders_requesters);

                           Log.i("Added drivers",riders_requesters.toString());
                       } else {
                           Log.e("Database problem", "Can not find Rider Calls path");
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                       Log.e("Database problem", error.getMessage());
                   }
               });

           }
       });
    }

    public void getRiderLatitudes(FireBaseCallbackLatitude fireBaseCallbackLatitude) {
       getDriverCategory(new FireBaseCallBackDriverCar() {
           @Override
           public void onCallBackDriverCarCategory(String driversCarCategory) {
               FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
               DatabaseReference root = firebaseDatabase.getReference();

               root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()) {
                           Log.i("Number of users", String.valueOf(snapshot.getChildrenCount()));

                           for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                               String pickedCar = String.valueOf(dataSnapshot.child("Picked car").getValue());

                               if (!dataSnapshot.getKey().equals("test") && pickedCar.equalsIgnoreCase(driversCarCategory)) {
                                   java.lang.Double latitude = java.lang.Double.parseDouble(String.valueOf(snapshot.child(dataSnapshot.getKey()).child("Current location").child("rider_latitude").getValue()));
                                   latitudes.add(latitude);
                               }
                           }

                           Log.i("Latitude list: ", latitudes.toString());
                           fireBaseCallbackLatitude.onCallbackLatitude(latitudes);
                       } else {
                           Log.e("Database problem", "Path Rider Calls not recognized");
                       }
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {
                       Log.e("Database error",error.getMessage());
                   }
               });

           }
       });

    }

    public void getRiderLongitude(FireBaseCallbackLongitude fireBaseCallbackLongitude) {
        getDriverCategory(new FireBaseCallBackDriverCar() {
            @Override
            public void onCallBackDriverCarCategory(String driversCarCategory) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference root = firebaseDatabase.getReference();

                root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Log.i("Number of users", String.valueOf(snapshot.getChildrenCount()));
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                String pickedCar = String.valueOf(dataSnapshot.child("Picked car").getValue());
                                if (!dataSnapshot.getKey().equals("test") && pickedCar.equalsIgnoreCase(driversCarCategory)){
                                    java.lang.Double longitude = java.lang.Double.parseDouble(String.valueOf(snapshot.child(dataSnapshot.getKey()).child("Current location").child("rider_longitude").getValue()));
                                    longitudes.add(longitude);
                                }
                            }

                            fireBaseCallbackLongitude.onCallBackLongitude(longitudes);
                            Log.i("Longitude list: ",longitudes.toString());
                        } else {
                            Log.e("Database problem", "Path Rider Calls not recognized");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Database error", error.getMessage());
                    }
                });
            }
        });

    }

    public void getDriverCategory(FireBaseCallBackDriverCar fireBaseCallBackDriverCar){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Driver").child(nameOfDriver).child("Car").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){

                    if (snapshot.hasChildren()){
                       for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                           //TODO I know, it is only one car category, but refactor if it is something wrong and driver has more then one added category
                           String carcategory = dataSnapshot.getKey();
                           fireBaseCallBackDriverCar.onCallBackDriverCarCategory(carcategory);
                       }
                    }
                    else{
                        Log.e("Driver's car","Failed to get car's category from driver in database");
                    }
                }
                else{
                    Log.e("Driver's name","Problem to get driver's name for car category");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.e("Database problems",error.getMessage());
            }
        });
    }

    public String getNameOfDriver() {
        if (this.getIntent().getStringExtra("drivername from cardetails") == null) {
            if (this.getIntent().getStringExtra("drivername from login") != null) {
                return this.getIntent().getStringExtra("drivername from login");
            }
        }
        else{
            return this.getIntent().getStringExtra("drivername from cardetails");
        }
        return null;
    }

}

