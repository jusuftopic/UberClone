package com.example.uberclone.MainApp.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackEndRiderLocation;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackUsername;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FirebaseCallBackCurrentRiderLocation;
import com.example.uberclone.Modules.Requests.RiderLocation;
import com.example.uberclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowNearestRequesters extends AppCompatActivity {

    private String nameOfDriver;

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

        requesters = new ArrayList<>();
        currentRiderLocs = new ArrayList<RiderLocation>();
        endRiderLocs = new ArrayList<RiderLocation>();

        //temporary
        listContent = new ArrayList<>();
        nearestListView = (ListView) findViewById(R.id.nearestRequestsList);
        listAdapter = new ArrayAdapter<>(ShowNearestRequesters.this, android.R.layout.simple_list_item_1,listContent);
        nearestListView.setAdapter(listAdapter);

        setContentOfList();


    }

    public void setContentOfList(){
        getAllRequesters(new FireBaseCallbackUsername() {
            @Override
            public void onCallbackUsername(ArrayList<String> requestes) {
                getAllCurrentLocations(new FirebaseCallBackCurrentRiderLocation() {
                    @Override
                    public void onCallbackCurrentRiderLocation(ArrayList<RiderLocation> currentlocations) {
                        getAllEndLocations(new FireBaseCallbackEndRiderLocation() {
                            @Override
                            public void onCallbackCurrentRiderLocation(ArrayList<RiderLocation> endlocations) {
                                if (requestes.size() == currentlocations.size() && requestes.size() == endlocations.size()){

                                    for (int i = 0; i < requestes.size(); i++){
                                        listContent.add(requestes.get(i)+" - Current location: ["+currentlocations.get(i).getRider_latitude()+";"+currentlocations.get(i).getRider_longitude()+"]");
                                    }

                                    listAdapter.notifyDataSetChanged();
                                }
                                else {
                                    Log.w("Lists problem","Requsters list ["+requestes.size()+"], current locations list ["+currentlocations.size()+"] and end locations list ["+endlocations.size()+"]  not same size");
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void getAllRequesters(FireBaseCallbackUsername fireBaseCallbackUsername){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.i("Number of users",String.valueOf(snapshot.getChildrenCount()));

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (!dataSnapshot.getKey().equalsIgnoreCase("test")){
                            String requester = dataSnapshot.getKey();

                            requesters.add(requester);
                        }
                    }

                    fireBaseCallbackUsername.onCallbackUsername(requesters);
                }
                else{
                    Log.w("Request list", "Check path to find user in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public void getAllCurrentLocations(FirebaseCallBackCurrentRiderLocation firebaseCallBackCurrentRiderLocation){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (!dataSnapshot.getKey().equalsIgnoreCase("test")){
                            double latitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("Current location").child("rider_latitude").getValue()));
                            double longitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("Current location").child("rider_longitude").getValue()));

                            RiderLocation currentRiderLocation = new RiderLocation(latitude,longitude);

                            Log.i("Check current location",dataSnapshot.getKey()+"'s current location ["+ currentRiderLocation.getRider_latitude()+";"+ currentRiderLocation.getRider_longitude()+"] added in list");

                            currentRiderLocs.add(currentRiderLocation);

                        }
                    }

                    firebaseCallBackCurrentRiderLocation.onCallbackCurrentRiderLocation(currentRiderLocs);
                }
                else{
                    Log.w("Request list", "Check path to find user in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database problem",error.getMessage());
            }
        });

    }

    public void getAllEndLocations(FireBaseCallbackEndRiderLocation firebaseCallBackEndRiderLocation){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (!dataSnapshot.getKey().equalsIgnoreCase("test")){
                            double latitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("End location").child("rider_latitude").getValue()));
                            double longitude = java.lang.Double.parseDouble(String.valueOf(dataSnapshot.child("End location").child("rider_longitude").getValue()));

                            RiderLocation endRiderLocation = new RiderLocation(latitude,longitude);

                            Log.i("Check current location",dataSnapshot.getKey()+"'s current location ["+ endRiderLocation.getRider_latitude()+";"+ endRiderLocation.getRider_longitude()+"] added in list");

                           endRiderLocs.add(endRiderLocation);
                        }
                    }

                    firebaseCallBackEndRiderLocation.onCallbackCurrentRiderLocation(endRiderLocs);
                }
                else{
                    Log.w("Request list", "Check path to find user in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database problem",error.getMessage());
            }
        });

    }


    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("driver name from main") != null){
            return this.getIntent().getStringExtra("driver name from main");
        }
        else{
            Log.e("Intent fail","Failed to transport name of driver from main to list");
            return null;
        }
    }
}