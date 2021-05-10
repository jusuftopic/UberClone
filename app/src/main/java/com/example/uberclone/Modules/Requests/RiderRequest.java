package com.example.uberclone.Modules.Requests;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderRequest {

    private String rider_name;
    private Location rider_location;

    RiderRequest(){}

    RiderRequest(String rider_name,Location rider_location){
        this.rider_name = rider_name;
        this.rider_location = rider_location;
    }


    public void setRider_name(String rider_name){
        this.rider_name = rider_name;
    }
    public void setRider_location(Location rider_location){
        this.rider_location = rider_location;
    }

    public String getRider_name(){
        return this.rider_name;
    }
    public Location getRider_location(){
        return this.rider_location;
    }
}
