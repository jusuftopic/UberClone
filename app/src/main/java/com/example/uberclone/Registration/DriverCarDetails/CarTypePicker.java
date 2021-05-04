package com.example.uberclone.Registration.DriverCarDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.example.uberclone.Registration.DriverCarDetails.CarInformations.UberSUVDetails;
import com.example.uberclone.Registration.DriverCarDetails.CarInformations.UberXDetails;
import com.example.uberclone.Registration.DriverCarDetails.CarInformations.UberXLDetails;

public class CarTypePicker extends AppCompatActivity {

    private String nameOfDriver;

    private Spinner cartypes;
    private CarAdapter carAdapter;

    private String[] vehicletypes;
    private String[] pricerange;

    private Button toCategory;

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_type_picker);

        this.getSupportActionBar().hide();

        nameOfDriver = getNameOfDriver();

        vehicletypes = getVehicleTypes();
        pricerange = getPricerange();

        toCategory = (Button) findViewById(R.id.tocategory);

        category = "UberX";

        cartypes = (Spinner) findViewById(R.id.car_types);
        carAdapter = new CarAdapter(CarTypePicker.this,vehicletypes,pricerange);
        cartypes.setAdapter(carAdapter);

        cartypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        category = "UberX";
                        break;

                    case 1:
                        category = "UberXL";
                        break;

                    case 2:
                        category = "UberSUV";
                        break;

                    case 3:
                        category = "UberBlack";
                        break;

                    case 4:
                        category = "UberSelect";
                        break;

                    case 5:
                        category = "UberLux";
                        break;

                    case 6:
                        category = "UberWAV";
                        break;

                    default:
                        category = "";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void goToCategory(View view){
        if (!category.equalsIgnoreCase("")){
          switch (category){
              case "UberX":
                  Intent toUberX = new Intent(CarTypePicker.this,UberXDetails.class);
                  toUberX.putExtra("driver from picker",nameOfDriver);
                  startActivity(toUberX);
                  break;

              case "UberXL":
                  Intent toUberXL = new Intent(CarTypePicker.this, UberXLDetails.class);
                  toUberXL.putExtra("driver from picker",nameOfDriver);
                  startActivity(toUberXL);
                  break;

              case "UberSUV":
                  Intent toUberSUV = new Intent(CarTypePicker.this, UberSUVDetails.class);
                  toUberSUV.putExtra("driver from picker",nameOfDriver);
                  startActivity(toUberSUV);
                  break;
          }
        }
        else{
            Toast.makeText(CarTypePicker.this,"Problem to pick category",Toast.LENGTH_LONG).show();
        }
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

    public String getNameOfDriver(){

        if (this.getIntent().getStringExtra("drivername from phonenumber") != null){
            return this.getIntent().getStringExtra("drivername from phonenumber");
        }

        return null;
    }
}