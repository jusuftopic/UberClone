package com.example.uberclone.MainApp.Rider.Settings.OptionItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uberclone.MainApp.Rider.Settings.SettingsMenu;
import com.example.uberclone.R;

public class OptionPersonalData extends AppCompatActivity {

    private String nameOfRider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_personal_data);

        nameOfRider = SettingsMenu.getRiderUsername();
    }

}