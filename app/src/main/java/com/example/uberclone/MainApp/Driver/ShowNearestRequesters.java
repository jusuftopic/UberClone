package com.example.uberclone.MainApp.Driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.uberclone.R;

public class ShowNearestRequesters extends AppCompatActivity {

    private String nameOfDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nearest_requesters);

        nameOfDriver = getNameOfDriver();
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