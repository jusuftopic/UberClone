package com.example.uberclone.MainApp.Rider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uberclone.Extras.Adapters.SettingsAdapter;
import com.example.uberclone.R;

public class SettingsMenu extends AppCompatActivity {

    private static final String MESSAGE_TAG = "SETTINGS_MENU- name of rider";

    private String nameOfRider;

    private TextView usernameTextView;

    private int[] logos;
    private String[] settingsOptions;

    private ListView listOfSettings;
    private SettingsAdapter settingsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        this.getSupportActionBar().hide();

        nameOfRider = getNameOfRider();

        usernameTextView = (TextView) findViewById(R.id.usernameSettings);
        usernameTextView.setText(nameOfRider);

        logos = getLogos();
        settingsOptions = getSettingOptions();

        listOfSettings = (ListView) findViewById(R.id.listSettings);
        settingsAdapter = new SettingsAdapter(SettingsMenu.this,logos,settingsOptions);
        listOfSettings.setAdapter(settingsAdapter);

        handleListClickListener();
    }

    public void handleListClickListener(){
        this.listOfSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent toPayment = new Intent(SettingsMenu.this,OptionPayment.class);
                        toPayment.putExtra(MESSAGE_TAG,nameOfRider);
                        startActivity(toPayment);
                }
            }
        });
    }

    public int[] getLogos(){
        int[] resLogos = new int[1];

        resLogos[0] = R.drawable.ic_card;

        return resLogos;
    }

    public String[] getSettingOptions(){
        String[] menuOptions = new String[1];

        menuOptions[0] = "Payment";

        return menuOptions;
    }

    public String getNameOfRider(){
        if (this.getIntent().getStringExtra("RIDER_LOBBY-name of rider") != null && !this.getIntent().getStringExtra("RIDER_LOBBY-name of rider").equalsIgnoreCase("")){
            return this.getIntent().getStringExtra("RIDER_LOBBY-name of rider");
        }

        Log.e("ERROR OCCURED","Failed to trasport a name of driver");
        return null;
    }

    public static String getMessageTag(){
        return MESSAGE_TAG;
    }
}