package com.example.uberclone.MainApp.Rider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.uberclone.R;

public class ChooseUberCar extends AppCompatActivity {

    private String nameOfRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_uber_car);

        nameOfRider = getNameOfDriver();
    }


    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("username from ridermain") != null){
            return this.getIntent().getStringExtra("username from ridermain");
        }
        else{
            Log.e("Trasportcar picker ","PROBLEM WITH INTENT FOR USERNAME" );
            return null;
        }
    }
}