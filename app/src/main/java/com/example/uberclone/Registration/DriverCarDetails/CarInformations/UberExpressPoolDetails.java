package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uberclone.R;

public class UberExpressPoolDetails extends AppCompatActivity {

    private String nameOfDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_express_pool_details);

        nameOfDriver = getNameOfDriver();
    }

    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("driver from picker") != null && !this.getIntent().getStringExtra("driver from picker").equalsIgnoreCase("")){
            return this.getIntent().getStringExtra("driver from picker");
        }
        return null;
    }
}