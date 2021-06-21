package com.example.uberclone.MainApp.Rider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.uberclone.R;

public class RideInformations extends AppCompatActivity {

    private String nameOfRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_informations);

        nameOfRider = getNameOfRider();

    }


    public String getNameOfRider(){
        String nameFromIntent = this.getIntent().getStringExtra("username from ridermain");

        if (nameFromIntent == null || nameFromIntent.equals("")){
            Log.e("Rider's name","Failed to get name of rider ("+nameFromIntent+") from intent");
            return null;
        }

        return nameFromIntent;
    }
}