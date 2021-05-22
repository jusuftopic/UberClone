package com.example.uberclone.MainApp.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.Driver.FireBaseCallBackDriverLocation;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackEndRiderLocation;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackUsername;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FirebaseCallBackCurrentRiderLocation;
import com.example.uberclone.Modules.Requests.DriverLocation;
import com.example.uberclone.Modules.Requests.RiderLocation;
import com.example.uberclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowNearestRequesters extends AppCompatActivity {

    private String nameOfDriver;

    private DriverLocation driverLocation;


    private ArrayList<String> requesters;
    private ArrayList<RiderLocation> currentRiderLocs;
    private ArrayList<RiderLocation> endRiderLocs;

    //temporary
    //TODO make custom Adater to make better list
    private ArrayList<String> listContent;
    private ListView nearestListView;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nearest_requesters);

        nameOfDriver = getNameOfDriver();

        driverLocation = new DriverLocation();

        requesters = new ArrayList<>();
        currentRiderLocs = new ArrayList<RiderLocation>();
        endRiderLocs = new ArrayList<RiderLocation>();

        //temporary
        listContent = new ArrayList<>();
        nearestListView = (ListView) findViewById(R.id.nearestRequestsList);
        listAdapter = new ArrayAdapter<>(ShowNearestRequesters.this, android.R.layout.simple_list_item_1, listContent);
        nearestListView.setAdapter(listAdapter);

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
                            public void onCallbackCurrentRiderLocation(ArrayList<RiderLocation> endlocations) {
                                getDriverLocation(new FireBaseCallBackDriverLocation() {
                                    @Override
                                    public void onCallBackDriverLocation(DriverLocation driverLocation) {
                                        handelList(requestes,currentlocations,endlocations,driverLocation);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    public void handelList(ArrayList<String> requesters,ArrayList<RiderLocation> currentlocations_rider,ArrayList<RiderLocation> endlocations_rider,DriverLocation driverLocation){
        if (requesters.size() == currentlocations_rider.size() && requesters.size() == endlocations_rider.size()){
            if (requesters.size() > 1){
                Location location_driver = switchDriverLocationToLocation(driverLocation);
                Location location_rider = switchRiderLocationToLocation(currentlocations_rider.get(0));

                float nearestRequest = location_driver.distanceTo(location_rider);
                int counter = 0;
                int indexOfNearste = 0;

                while (counter <= requesters.size()-1){
                    for (int i = 1; i < requesters.size(); i++){
                        if (indexOfNearste != i){
                            float newDistance = location_driver.distanceTo(switchRiderLocationToLocation(currentlocations_rider.get(i)));

                            if (newDistance < nearestRequest){
                                nearestRequest = newDistance;
                                indexOfNearste = i;
                            }
                        }
                    }

                    counter++;
                    listContent.add(requesters.get(indexOfNearste)+" ["+currentlocations_rider.get(indexOfNearste).getRider_latitude()+":"+currentlocations_rider.get(indexOfNearste).getRider_longitude()+"]");
                }
                listAdapter.notifyDataSetChanged();
            }
            else{
                this.listContent.add(requesters.get(0)+"- ["+currentlocations_rider.get(0).getRider_latitude()+":"+currentlocations_rider.get(0).getRider_longitude()+"]");
            }
        }
        else{
            Log.w("Problem withs lists","Requests list ("+requesters.size()+"), current locations list ("+currentlocations_rider.size()+"), end locations list ("+endlocations_rider.size()+") not same size");
        }

    }

    public Location switchDriverLocationToLocation(DriverLocation driverLocation){
      Location location = new Location(LocationManager.GPS_PROVIDER);
      location.setLatitude(driverLocation.getDriver_latitude());
      location.setLongitude(driverLocation.getDriver_longitude());

      return location;
    }

    public Location switchRiderLocationToLocation(RiderLocation riderLocation){
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(riderLocation.getRider_latitude());
        location.setLongitude(riderLocation.getRider_longitude());

        return location;
    }

    public void getAllRequesters(FireBaseCallbackUsername fireBaseCallbackUsername) {
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

                            requesters.add(requester);
                        }
                    }

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

    public void getAllCurrentLocations(FirebaseCallBackCurrentRiderLocation firebaseCallBackCurrentRiderLocation) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (!dataSnapshot.getKey().equalsIgnoreCase("test")) {
                            double latitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("Current location").child("rider_latitude").getValue()));
                            double longitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("Current location").child("rider_longitude").getValue()));

                            RiderLocation currentRiderLocation = new RiderLocation(latitude, longitude);

                            Log.i("Check current location", dataSnapshot.getKey() + "'s current location [" + currentRiderLocation.getRider_latitude() + ";" + currentRiderLocation.getRider_longitude() + "] added in list");

                            currentRiderLocs.add(currentRiderLocation);

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

    public void getAllEndLocations(FireBaseCallbackEndRiderLocation firebaseCallBackEndRiderLocation) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (!dataSnapshot.getKey().equalsIgnoreCase("test")) {
                            double latitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("End location").child("rider_latitude").getValue()));
                            double longitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("End location").child("rider_longitude").getValue()));

                            RiderLocation endRiderLocation = new RiderLocation(latitude, longitude);

                            Log.i("Check current location", dataSnapshot.getKey() + "'s current location [" + endRiderLocation.getRider_latitude() + ";" + endRiderLocation.getRider_longitude() + "] added in list");

                            endRiderLocs.add(endRiderLocation);
                        }
                    }

                    firebaseCallBackEndRiderLocation.onCallbackCurrentRiderLocation(endRiderLocs);
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

        root.child("Requsts").child("Driver's Acceptance").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
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
}