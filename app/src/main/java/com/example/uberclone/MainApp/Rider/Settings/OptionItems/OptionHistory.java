package com.example.uberclone.MainApp.Rider.Settings.OptionItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.uberclone.Extras.Adapters.HistoryAdapter;
import com.example.uberclone.MainApp.Rider.Settings.SettingsMenu;
import com.example.uberclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OptionHistory extends AppCompatActivity {

    private String nameOfRider;
    private ListView listOfHistoryRides;

    private ArrayList<String> addresses;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_history);

        nameOfRider = SettingsMenu.getRiderUsername();

        listOfHistoryRides = (ListView) findViewById(R.id.historyOfRidesList);

        setUpHistoryRides();
    }

    public void setUpHistoryRides(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("Requests").child("Finished requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        for (DataSnapshot childSnapshot : dataSnapshot.child("Riders").getChildren()){
                            if (childSnapshot.getKey().equalsIgnoreCase(nameOfRider)){
                                double latitude = (double) childSnapshot.child("Location").child("latitude").getValue();
                                double longitude = (double) childSnapshot.child("Location").child("longitude").getValue();

                                addresses.add(transformToAddress(latitude,longitude));
                            }

                        }

                        historyAdapter = new HistoryAdapter(OptionHistory.this,addresses);
                        listOfHistoryRides.setAdapter(historyAdapter);
                    }
                }
                else{
                    Log.e("FAILED","Failed to get finished requests path");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database fail",error.getMessage());
            }
        });
    }

    public String transformToAddress(double latitude, double longitude){
        Geocoder geocoder = new Geocoder(OptionHistory.this, Locale.getDefault());
        List<Address> addresses;
        String address = "";

        try{
            addresses = geocoder.getFromLocation(latitude,longitude,1);

            if (addresses.size() > 0 && address != null){
                if (addresses.get(0).getThoroughfare() != null){
                    address = addresses.get(0).getThoroughfare();
                }
            }
            else{
                Log.e("Address fail","Failed to get address");
                address = "Unknown";
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

        return address;
    }
}