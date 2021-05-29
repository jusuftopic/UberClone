package com.example.uberclone.MainApp.Rider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.uberclone.R;

public class RidePayment extends AppCompatActivity {

    private String nameOfRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_payment);

        nameOfRider = getNameOfRider();
    }


    public String getNameOfRider() {
        if (this.getIntent().getStringExtra("nameOfRider") != null && this.getIntent().getStringExtra("nameOfRider").equalsIgnoreCase("")) {

            return this.getIntent().getStringExtra("nameOfRider");
        }
        Log.e("Intent failed","Failed to get name of rider");
        return null;
    }
}


