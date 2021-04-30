package com.example.uberclone.Registration.DriverCarDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.uberclone.Extras.Adapters.CarAdapters.CarAdapter;
import com.example.uberclone.Modules.Car.UberBlack;
import com.example.uberclone.Modules.Car.UberExpressPool;
import com.example.uberclone.Modules.Car.UberLux;
import com.example.uberclone.Modules.Car.UberSUV;
import com.example.uberclone.Modules.Car.UberSelect;
import com.example.uberclone.Modules.Car.UberWAV;
import com.example.uberclone.Modules.Car.UberX;
import com.example.uberclone.Modules.Car.UberXL;
import com.example.uberclone.R;
import com.example.uberclone.Registration.DriverCarDetails.CarInformations.UberXDetails;

public class CarTypePicker extends AppCompatActivity {

    private String nameOfDriver;

    private Spinner cartypes;
    private CarAdapter carAdapter;

    private String[] vehicletypes;
    private String[] pricerange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_picker);

        nameOfDriver = getNameOfDriver();
        Log.i("CarType- Name",nameOfDriver);

        vehicletypes = getVehicleTypes();
        pricerange = getPricerange();

        cartypes = (Spinner) findViewById(R.id.car_types);
        carAdapter = new CarAdapter(CarTypePicker.this,vehicletypes,pricerange);
        cartypes.setAdapter(carAdapter);

        cartypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        moveToCarDetails(UberXDetails.class);
                        break;
                    case 1:
                        moveToCarDetails(UberXLDetails.class);
                        break;
                    case 2:
                        moveToCarDetails(UberSUVDetails.class);
                        break;
                    case 3:
                        moveToCarDetails(UberBlackDetails.class);
                        break;
                    case 4:
                        moveToCarDetails(UberSelectDetails.class);
                        break;

                    case 5:
                        moveToCarDetails(UberExpressPoolXDetails.class);
                        break;
                    case 6:
                        moveToCarDetails(UberLuxDetails.class);
                        break;
                    case 7:
                        moveToCarDetails(UberWAVDetails.class);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public String[] getVehicleTypes(){
        String[] vehicles = new String[8];

        vehicles[0] = "UberX";
        vehicles[1] = "UberXL";
        vehicles[2] = "UberSUV";
        vehicles[3] = "UberBlack";
        vehicles[4] = "UberSelect";
        vehicles[5] = "UberExpressPool";
        vehicles[6] = "UberLux";
        vehicles[7] = "UberWAV";

        return vehicles;
    }

    public String[] getPricerange(){

        String[] vehicleprices = new String[8];

        vehicleprices[0] = buildPriceRange(UberX.MIN_PRICE_RANGE,UberX.MAX_PRICE_RANGE);
        vehicleprices[1] = buildPriceRange(UberXL.MIN_PRICE_RANGE,UberXL.MAX_PRICE_RANGE);
        vehicleprices[2] = buildPriceRange(UberSUV.MIN_PRICE_RANGE,UberSUV.MAX_PRICE_RANGE);
        vehicleprices[3] = buildPriceRange(UberBlack.MIN_PRICE_RANGE,UberBlack.MAX_PRICE_RANGE);
        vehicleprices[4] = buildPriceRange(UberSelect.MIN_PRICE_RANGE,UberSelect.MAX_PRICE_RANGE);
        vehicleprices[5] = buildPriceRange(UberExpressPool.MIN_PRICE_RANGE,UberExpressPool.MAX_PRICE_RANGE);
        vehicleprices[6] = buildPriceRange(UberLux.MIN_PRICE_RANGE,UberLux.MAX_PRICE_RANGE);
        vehicleprices[7] = buildPriceRange(UberWAV.MIN_PRICE_RANGE,UberWAV.MAX_PRICE_RANGE);

        return vehicleprices;


    }

    public String buildPriceRange(double min_price, double max_price){
        StringBuilder builder = new StringBuilder();

        return builder.append(min_price).append("-").append(max_price).append("$").toString();

    }

    public void moveToCarDetails(Class activity){
        Intent toCarDetails = new Intent(CarTypePicker.this,activity);
        toCarDetails.putExtra("driver from picker",nameOfDriver);
        startActivity(toCarDetails);
    }

    public String getNameOfDriver(){

        if (this.getIntent().getStringExtra("drivername from phonenumber") != null){
            return this.getIntent().getStringExtra("drivername from phonenumber");
        }

        return null;
    }
}