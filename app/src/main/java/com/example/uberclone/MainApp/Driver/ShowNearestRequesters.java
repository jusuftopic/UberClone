package com.example.uberclone.MainApp.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.uberclone.Extras.Adapters.RequestsAdapter;
import com.example.uberclone.MainApp.CallBacks.Driver.FireBaseCallBackDriverCar;
import com.example.uberclone.MainApp.CallBacks.Driver.FireBaseCallBackDriverLocation;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackEndRiderLocation;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackUsername;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FirebaseCallBackCurrentRiderLocation;
import com.example.uberclone.Models.Requests.DriverLocation;
import com.example.uberclone.Models.Requests.RiderLocation;
import com.example.uberclone.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ShowNearestRequesters extends AppCompatActivity {

    private String nameOfDriver;

    private DriverLocation driverLocation;


    private ArrayList<String> requesters;

    private ArrayList<RiderLocation> currentRiderLocs;
    private ArrayList<Location> sortedRiderLocs;
    private ArrayList<RiderLocation> endRiderLocs;


    private ListView nearestListView;


    private String[] riders;
    private String[] addresses_currentLocation;
    private int[] imgs;
    private RequestsAdapter requestsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nearest_requesters);

        this.getSupportActionBar().hide();

        nameOfDriver = getNameOfDriver();

        driverLocation = new DriverLocation();

        requesters = new ArrayList<>();

        currentRiderLocs = new ArrayList<RiderLocation>();
        sortedRiderLocs = new ArrayList<>();

        endRiderLocs = new ArrayList<RiderLocation>();

        riders = new String[0];
        addresses_currentLocation = new String[0];
        imgs = new int[0];


        nearestListView = (ListView) findViewById(R.id.nearestRequestsList);

        setContentOfList();
    }

    public void setContentOfList() {
        getAllRequesters(new FireBaseCallbackUsername() {
            @Override
            public void onCallbackUsername(ArrayList<String> requestes) {
                getAllCurrentLocations(new FirebaseCallBackCurrentRiderLocation() {
                    @Override
                    public void onCallbackCurrentRiderLocation(ArrayList<RiderLocation> currentlocations) {
                        getAllEndLocations(new FireBaseCallbackEndRiderLocation() {
                            @Override
                            public void onCallbackEndRiderLocation(ArrayList<RiderLocation> endlocations) {
                                getDriverLocation(new FireBaseCallBackDriverLocation() {
                                    @Override
                                    public void onCallBackDriverLocation(DriverLocation driverLocation) {
                                        if (requestes.size() == currentlocations.size() && requestes.size() == endlocations.size() && requestes.size() > 0) {
                                            riders = getRiders(requestes);
                                            addresses_currentLocation = getAddressesFromList(currentlocations);
                                            imgs = getImgs(requestes.size());
                                            requestsAdapter = new RequestsAdapter(ShowNearestRequesters.this, riders, addresses_currentLocation, imgs);
                                            nearestListView.setAdapter(requestsAdapter);
                                            addClickListenerOnList(nearestListView, riders, addresses_currentLocation, currentlocations, endlocations, driverLocation);

                                        } else {
                                            Log.w("Problem withs lists", "Requests list (" + requestes.size() + "), current locations list (" + currentlocations.size() + "), end locations list (" + endlocations.size() + ") not same size");
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void addClickListenerOnList(ListView listOfRequests, String[] users, String[] currentAdresses, ArrayList<RiderLocation> currentRiderLocs, ArrayList<RiderLocation> endRiderLocs, DriverLocation driverLocation) {
        listOfRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toPopUp = new Intent(ShowNearestRequesters.this, RequesterPopUp.class);
                toPopUp.putExtra("username_rider", users[position]);
                toPopUp.putExtra("current address", currentAdresses[position]);
                toPopUp.putExtra("currentlocation_latitude", currentRiderLocs.get(position).getRider_latitude());
                toPopUp.putExtra("currentlocation_longitude", currentRiderLocs.get(position).getRider_longitude());
                toPopUp.putExtra("endlocation_latitude", endRiderLocs.get(position).getRider_latitude());
                toPopUp.putExtra("endlocation_longitude", endRiderLocs.get(position).getRider_latitude());
                toPopUp.putExtra("avatar image", imgs[position]);
                toPopUp.putExtra("diver name", nameOfDriver);
                toPopUp.putExtra("driver location_latitude", driverLocation.getDriver_latitude());
                toPopUp.putExtra("driver location_longitude", driverLocation.getDriver_longitude());

                startActivity(toPopUp);
            }
        });
    }

    public String[] getRiders(ArrayList<String> requesters) {
        String[] riderArray = new String[requesters.size()];
        int index = 0;

        for (String requester : requesters) {
            riderArray[index] = requester;
            index++;
        }

        Log.i("Riders: ", Arrays.toString(riderArray));
        return riderArray;
    }


    public String[] getAddressesFromList(ArrayList<RiderLocation> currentRiderLocations) {
        String[] addresses = new String[currentRiderLocations.size()];
        int index = 0;

        for (RiderLocation riderLocation : currentRiderLocations) {
            addresses[index] = changeCordinationsToAdress(riderLocation);
            index++;
        }

        Log.i("Addresses: ", Arrays.toString(addresses));
        return addresses;
    }

    public String changeCordinationsToAdress(RiderLocation riderLocation) {
        LatLng currentLocation = new LatLng(riderLocation.getRider_latitude(), riderLocation.getRider_longitude());

        Geocoder geocoder = new Geocoder(ShowNearestRequesters.this, Locale.getDefault());
        List<Address> addresses = null;

        String streetsname = "";

        try {
            addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);

            if (addresses.size() > 0 && addresses != null) {
                if (!addresses.get(0).getThoroughfare().equals("") || addresses.get(0).getThoroughfare() != null) {
                    streetsname += addresses.get(0).getThoroughfare();
                } else {
                    Log.e("Problem with streetname", "Can not get streetname from geocoder-> NullPointerException or empty");
                }
            } else {
                Log.e("Geocoder problem", "Problem to get addresses from geocoder- List null or empty");
                streetsname = currentLocation.latitude + ";" + currentLocation.longitude;
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Log.i("Address of rider: ", streetsname);
        return streetsname;
    }

    public int[] getImgs(int size) {
        int[] avatars = new int[size];

        for (int i = 0; i < avatars.length; i++) {
            avatars[i] = R.drawable.avatar;
        }
        return avatars;
    }


    public Location switchDriverLocationToLocation(DriverLocation driverLocation) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(driverLocation.getDriver_latitude());
        location.setLongitude(driverLocation.getDriver_longitude());

        return location;
    }

    public Location switchRiderLocationToLocation(RiderLocation riderLocation) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(riderLocation.getRider_latitude());
        location.setLongitude(riderLocation.getRider_longitude());

        return location;
    }

    public void getAllRequesters(FireBaseCallbackUsername fireBaseCallbackUsername) {
        getDriversCarCategory(new FireBaseCallBackDriverCar() {
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
                                if (!dataSnapshot.getKey().equalsIgnoreCase("test")) {
                                    String requester = dataSnapshot.getKey();
                                    String pickedCar = String.valueOf(dataSnapshot.child("Picked car").getValue());

                                    if (pickedCar.equalsIgnoreCase(driversCarCategory)) {
                                        requesters.add(requester);
                                    }
                                }
                            }

                            Log.i("Check drivers", requesters.toString() + " with " + driversCarCategory + " picked category");
                            fireBaseCallbackUsername.onCallbackUsername(requesters);
                        } else {
                            Log.w("Request list", "Check path to find user in database");
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

    public void getAllCurrentLocations(FirebaseCallBackCurrentRiderLocation firebaseCallBackCurrentRiderLocation) {
        getDriversCarCategory(new FireBaseCallBackDriverCar() {
            @Override
            public void onCallBackDriverCarCategory(String driversCarCategory) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference root = firebaseDatabase.getReference();

                root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (!dataSnapshot.getKey().equalsIgnoreCase("test")) {

                                    String pickedCar = String.valueOf(dataSnapshot.child("Picked car").getValue());

                                    if (pickedCar.equalsIgnoreCase(driversCarCategory)) {
                                        double latitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("Current location").child("rider_latitude").getValue()));
                                        double longitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("Current location").child("rider_longitude").getValue()));

                                        RiderLocation currentRiderLocation = new RiderLocation(latitude, longitude);

                                        Log.i("Check current location", dataSnapshot.getKey() + "'s current location [" + currentRiderLocation.getRider_latitude() + ";" + currentRiderLocation.getRider_longitude() + "] added in list");

                                        currentRiderLocs.add(currentRiderLocation);
                                    }
                                }
                            }
                            firebaseCallBackCurrentRiderLocation.onCallbackCurrentRiderLocation(currentRiderLocs);
                        } else {
                            Log.w("Request list", "Check path to find user in database");
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

    public void getAllEndLocations(FireBaseCallbackEndRiderLocation firebaseCallBackEndRiderLocation) {
       getDriversCarCategory(new FireBaseCallBackDriverCar() {
           @Override
           public void onCallBackDriverCarCategory(String driversCarCategory) {
               FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
               DatabaseReference root = firebaseDatabase.getReference();

               root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()) {

                           for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                               if (!dataSnapshot.getKey().equalsIgnoreCase("test")) {

                                   String pickedCar = String.valueOf(dataSnapshot.child("Picked car").getValue());

                                   if (pickedCar.equalsIgnoreCase(driversCarCategory)){
                                       double latitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("End location").child("rider_latitude").getValue()));
                                       double longitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("End location").child("rider_longitude").getValue()));

                                       RiderLocation endRiderLocation = new RiderLocation(latitude, longitude);

                                       Log.i("Check current location", dataSnapshot.getKey() + "'s current location [" + endRiderLocation.getRider_latitude() + ";" + endRiderLocation.getRider_longitude() + "] added in list");

                                       endRiderLocs.add(endRiderLocation);
                                   }
                               }
                           }

                           firebaseCallBackEndRiderLocation.onCallbackEndRiderLocation(endRiderLocs);
                       } else {
                           Log.w("Request list", "Check path to find user in database");
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


    public String getNameOfDriver() {
        if (this.getIntent().getStringExtra("driver name from main") != null) {
            return this.getIntent().getStringExtra("driver name from main");
        } else {
            Log.e("Intent fail", "Failed to transport name of driver from main to list");
            return null;
        }
    }

    public void getDriverLocation(FireBaseCallBackDriverLocation fireBaseCallBackDriverLocation) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Driver's Acceptance").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double latitude = Double.parseDouble(String.valueOf(snapshot.child("Current location").child("driver_latitude").getValue()));
                    double longitude = Double.parseDouble(String.valueOf(snapshot.child("Current location").child("driver_longitude").getValue()));

                    Log.i("Driver location", "[" + String.valueOf(latitude + ";" + String.valueOf(longitude) + "]"));

                    driverLocation = new DriverLocation(false, latitude, longitude);

                    fireBaseCallBackDriverLocation.onCallBackDriverLocation(driverLocation);
                } else {
                    Log.e("Path problem", "Can not find path to " + nameOfDriver);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Database problem", error.getMessage());
            }
        });

    }

    public void getDriversCarCategory(FireBaseCallBackDriverCar fireBaseCallBackDriverCar) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Driver").child(nameOfDriver).child("Car").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String carcategory = dataSnapshot.getKey();
                            fireBaseCallBackDriverCar.onCallBackDriverCarCategory(carcategory);
                        }
                    } else {
                        Log.e("Driver's car", "Failed to get car's category from driver in database");
                    }

                } else {
                    Log.e("Car's category", "Problem to find driver's path in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database problem", error.getMessage());
            }
        });
    }
}