package com.example.uberclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.uberclone.Login.DriverLogin;
import com.example.uberclone.Login.RiderLogin;

public class MainActivity extends AppCompatActivity {

    private TextView driver;
    private TextView rider;

    private Switch aSwitch;

    private Button toLogOrReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


        driver = (TextView) findViewById(R.id.driver);
        rider = (TextView) findViewById(R.id.rider);

        aSwitch = (Switch) findViewById(R.id.aSwitch);

        toLogOrReg = (Button) findViewById(R.id.registration);
    }

    public void goToLoginOrRegistration(View view){
        if (aSwitch.isChecked()){
            Intent intent = new Intent(MainActivity.this, RiderLogin.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(MainActivity.this, DriverLogin.class);
            startActivity(intent);
        }

    }



}