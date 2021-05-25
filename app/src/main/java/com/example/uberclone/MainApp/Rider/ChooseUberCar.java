package com.example.uberclone.MainApp.Rider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.uberclone.Extras.Adapters.UberTypeAdapter;
import com.example.uberclone.Modules.Car.UberBlack;
import com.example.uberclone.Modules.Car.UberExpressPool;
import com.example.uberclone.Modules.Car.UberLux;
import com.example.uberclone.Modules.Car.UberSUV;
import com.example.uberclone.Modules.Car.UberSelect;
import com.example.uberclone.Modules.Car.UberWAV;
import com.example.uberclone.Modules.Car.UberX;
import com.example.uberclone.Modules.Car.UberXL;
import com.example.uberclone.R;

public class ChooseUberCar extends AppCompatActivity {

    private String nameOfRider;

    private ListView listOfTypes;
    private UberTypeAdapter uberTypeAdapter;

    private String[] types;
    private String[] prices;
    private int[] numsOfPassengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_uber_car);

        nameOfRider = getNameOfDriver();

        types = getTypes();
        prices = getPrices();
        numsOfPassengers = getNumsOfPassengers();
    }

    public String[] getTypes(){
        String [] cars = new String[8];

        cars[0] = "UberX";
        cars[1] = "UberXL";
        cars[2] = "Uber Black";
        cars[3] = "Uber Select";
        cars[4] = "Uber Lux";
        cars[5] = "Uber SUV";
        cars[6] = "Uber WAV";
        cars[7] = "Uber Express Pool";

        return cars;
    }

    public String[] getPrices(){
        String[] carprices = new String[8];

        carprices[0] = UberX.MIN_PRICE_RANGE+"-"+UberX.MAX_PRICE_RANGE;
        carprices[1] = UberXL.MIN_PRICE_RANGE+"-"+UberXL.MAX_PRICE_RANGE;
        carprices[2] = UberBlack.MIN_PRICE_RANGE+"-"+UberBlack.MAX_PRICE_RANGE;
        carprices[3] = UberSelect.MIN_PRICE_RANGE+"-"+UberSelect.MAX_PRICE_RANGE;
        carprices[4] =  UberLux.MIN_PRICE_RANGE+"-"+UberLux.MAX_PRICE_RANGE;
        carprices[5] =  UberSUV.MIN_PRICE_RANGE+"-"+UberSUV.MAX_PRICE_RANGE;
        carprices[6] =  UberWAV.MIN_PRICE_RANGE+"-"+UberWAV.MAX_PRICE_RANGE;
        carprices[7] =  UberExpressPool.MIN_PRICE_RANGE+"-"+UberExpressPool.MAX_PRICE_RANGE;

        return carprices;
    }

    public int[] getNumsOfPassengers(){
        int[] passangersNum = new int[8];

        passangersNum[0] = UberX.MAX_NUMBER_OF_PASSENGERS;
        passangersNum[1] = UberXL.MAX_NUMBER_OF_PASSENGERS;
        passangersNum[2] = UberBlack.MAX_NUMBER_OF_PASSENGERS;
        passangersNum[3] = UberSelect.MAX_NUMBER_OF_PASSENGERS;
        passangersNum[4] = UberLux.MAX_NUMBER_OF_PASSENGERS;
        passangersNum[5] = UberSUV.MAX_NUMBER_OF_PASSENGERS;
        passangersNum[6] = UberWAV.MAX_NUMBER_OF_PASSENGERS;
        passangersNum[7] = UberExpressPool.MAX_NUMBER_OF_PASSENGERS;

        return passangersNum;
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