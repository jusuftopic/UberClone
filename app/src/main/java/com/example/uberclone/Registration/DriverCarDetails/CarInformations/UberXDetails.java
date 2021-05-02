package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uberclone.Modules.Car.UberX;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UberXDetails extends AppCompatActivity {

    private String drivername;

    private EditText carname;
    private EditText number_of_doors;
    private EditText number_of_passangers;
    private EditText price;

    private Button addCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_x_details);

        drivername = getDriverName();
        Log.i("Name from car picker", drivername);

        carname = (EditText) findViewById(R.id.carname);
        number_of_doors = (EditText) findViewById(R.id.numberofdoors);
        number_of_passangers = (EditText) findViewById(R.id.maxpassangers);
        price = (EditText) findViewById(R.id.price);

        addCar = (Button) findViewById(R.id.submitCarDetails);
    }

    public void addCarToDatabase(View view){

        String name_car = String.valueOf(carname.getText());
        int doors = Integer.parseInt(String.valueOf(number_of_doors.getText()));
        int passangers = Integer.parseInt(String.valueOf(number_of_passangers.getText()));
        double car_price = Double.parseDouble(String.valueOf(price.getText()));

        if (isValideCar(name_car,doors,passangers,car_price)){

            UberX uberX = new UberX(name_car,doors,passangers,car_price);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference root = firebaseDatabase.getReference();

            root.child("User").child("Driver").child(drivername).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        root.child("User").child("Driver").child(drivername).child("Car").setValue(uberX).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                             Toast.makeText(UberXDetails.this,"Succesefull added car",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        Log.e("SNAPSHOT STATUS(UBERX)","Problem with user in database");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
        /*    AlertDialog.Builder builder = new AlertDialog.Builder(UberXDetails.this)
                    .setTitle("Invalid informations")
                    .setMessage("Your car may not fit the UberX category.\nRead requirements for this category?")
                    .setNegativeButton("No",null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent toCategoryDetails = new Intent(UberXDetails.this,UberXCategoryInfos.class);
                            startActivity(toCategoryDetails);
                        }
                    });

            builder.create().show();*/

            Toast.makeText(UberXDetails.this,"Not fit the UberX category",Toast.LENGTH_LONG).show();
            restartFields();
        }


    }

    public boolean fieldsEmpty(){
        if (TextUtils.isEmpty(carname.getText()) || TextUtils.isEmpty(number_of_doors.getText()) || TextUtils.isEmpty(number_of_passangers.getText()) || TextUtils.isEmpty(price.getText())){
            return true;
        }
        return false;
    }

    public boolean isValideCar(String name_car,int doors,int passangers, double price){
        if (!fieldsEmpty()){
            UberX uberX = new UberX(name_car,doors,passangers,price);

            if (uberX.isValidNumberOfDoors() && uberX.isValidNumberOfPassangers(uberX.getMaxPassengers()) && uberX.isValidPrice(UberX.MIN_PRICE_RANGE,UberX.MAX_PRICE_RANGE)){
                return true;
            }
        }
        else{
            Toast.makeText(UberXDetails.this,"Fill all required fields",Toast.LENGTH_LONG).show();
            restartFields();
        }



        return false;
    }

    public String getDriverName() {
        if (this.getIntent().getStringExtra("driver from picker") != null) {
            return this.getIntent().getStringExtra("driver from picker");
        }

        return null;
    }

    public void restartFields(){
        if (!TextUtils.isEmpty(carname.getText())){
            carname.setText("");
        }
        if (!TextUtils.isEmpty(number_of_doors.getText())){
            number_of_doors.setText("");
        }
        if (!TextUtils.isEmpty(number_of_passangers.getText())){
            number_of_passangers.setText("");
        }
        if (!TextUtils.isEmpty(price.getText())){
            price.setText("");
        }
    }
}