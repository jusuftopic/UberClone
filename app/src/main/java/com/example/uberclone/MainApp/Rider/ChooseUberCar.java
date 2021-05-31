package com.example.uberclone.MainApp.Rider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.uberclone.Extras.Adapters.UberTypeAdapter;
import com.example.uberclone.MainApp.Rider.RiderPayment.RidePayment;
import com.example.uberclone.Modules.Car.UberBlack;
import com.example.uberclone.Modules.Car.UberExpressPool;
import com.example.uberclone.Modules.Car.UberLux;
import com.example.uberclone.Modules.Car.UberSUV;
import com.example.uberclone.Modules.Car.UberSelect;
import com.example.uberclone.Modules.Car.UberWAV;
import com.example.uberclone.Modules.Car.UberX;
import com.example.uberclone.Modules.Car.UberXL;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        Log.i("Username",nameOfRider);

        types = getTypes();
        prices = getPrices();
        numsOfPassengers = getNumsOfPassengers();

        listOfTypes = (ListView) findViewById(R.id.listOfTypes);
        uberTypeAdapter = new UberTypeAdapter(ChooseUberCar.this,types,prices,numsOfPassengers);
        listOfTypes.setAdapter(uberTypeAdapter);

        setSelectListenerOnList();
    }

    public void setSelectListenerOnList(){
        listOfTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addCarInfosToRiderRequest(nameOfRider,types[position]);

                Intent toPayment = new Intent(ChooseUberCar.this,RidePayment.class);
                toPayment.putExtra("name of rider- from car picker",nameOfRider);
                startActivity(toPayment);

            }
        });
    }

    public void addCarInfosToRiderRequest(String ridersname, String cartype){
        if (cartype != null && !cartype.equals("")){
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference root = firebaseDatabase.getReference();

            root.child("Requests").child("Rider Calls").child(ridersname).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        root.child("Requests").child("Rider Calls").child(ridersname).child("Picked car").setValue(cartype).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                             Log.i("CAR ADDED","Succesefull added car to rider's request");
                            }
                        });
                    }
                    else{
                        Log.e("Rider's name path","Problem to find path to rider's username");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Database error",error.getMessage());
                }
            });
        }
    }


    public String[] getTypes(){
        String [] cars = new String[8];

        cars[0] = "UberX";
        cars[1] = "UberXL";
        cars[2] = "UberBlack";
        cars[3] = "UberSelect";
        cars[4] = "UberLux";
        cars[5] = "UberSUV";
        cars[6] = "UberWAV";
        cars[7] = "UberExpressPool";

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