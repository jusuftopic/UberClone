package com.example.uberclone.MainApp.Rider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.uberclone.R;

public class SettingsMenu extends AppCompatActivity {

    private String nameOfRider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        this.getSupportActionBar().hide();

        nameOfRider = getNameOfRider();
    }


    public String getNameOfRider(){
        if (this.getIntent().getStringExtra("RIDER_LOBBY-name of rider") != null && !this.getIntent().getStringExtra("RIDER_LOBBY-name of rider").equalsIgnoreCase("")){
            return this.getIntent().getStringExtra("RIDER_LOBBY-name of rider");
        }

        Log.e("ERROR OCCURED","Failed to trasport a name of driver");
        return null;
    }
}