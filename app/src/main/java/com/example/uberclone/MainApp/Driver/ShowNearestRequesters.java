package com.example.uberclone.MainApp.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackLatitude;
import com.example.uberclone.MainApp.Driver.FirebaseCallbacks.FireBaseCallbackUsername;
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
    private ArrayList<Double> latitudes;
    private ArrayList<Double> longitudes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nearest_requesters);

        nameOfDriver = getNameOfDriver();

        requesters = new ArrayList<>();
        latitudes = new ArrayList<Double>();
        longitudes = new ArrayList<Double>();
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

    public void getLatitudesOfRequesters(FireBaseCallbackLatitude fireBaseCallbackLatitude){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Rider Calls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (!dataSnapshot.getKey().equalsIgnoreCase("test")){
                            double latitude = Double.parseDouble(String.valueOf(dataSnapshot.child("Current location").child("rider_latitude").getValue()));
                            Log.i("Check-latitude",dataSnapshot.getKey()+"'s latitude: "+latitude+" is going to be added in database");

                            latitudes.add(latitude);
                        }
                    }
                    fireBaseCallbackLatitude.onCallbackLatitude(latitudes);
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