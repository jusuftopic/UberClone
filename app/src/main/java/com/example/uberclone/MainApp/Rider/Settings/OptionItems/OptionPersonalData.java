package com.example.uberclone.MainApp.Rider.Settings.OptionItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.uberclone.MainApp.Rider.Settings.SettingsMenu;
import com.example.uberclone.R;

import java.util.ArrayList;

public class OptionPersonalData extends AppCompatActivity {

    private String nameOfRider;

    private ListView listOfPersonalSettings;
    private ArrayList<String> personalSettings;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_personal_data);

        nameOfRider = SettingsMenu.getRiderUsername();

        listOfPersonalSettings = (ListView) findViewById(R.id.listOfPersonalSettings);

        personalSettings = getPersonalSettings();
        listAdapter = new ArrayAdapter<>(OptionPersonalData.this, android.R.layout.simple_list_item_1,personalSettings);

        listOfPersonalSettings.setAdapter(listAdapter);

        handleOnListClick();
    }

    public void handleOnListClick(){
        listOfPersonalSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        goToOption(ChangePassword.class);

                }
            }
        });
    }

    public void goToOption(Class c){
        Intent toOption = new Intent(OptionPersonalData.this,c);
        startActivity(toOption);
    }


    public ArrayList<String> getPersonalSettings(){
        ArrayList<String> settings = new ArrayList<>();

        settings.add("Change password");

        return settings;
    }

}