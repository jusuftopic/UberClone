package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberclone.MainApp.Driver.DriverMainContent;
import com.example.uberclone.Modules.Car.CarMarks.CarMarks;
import com.example.uberclone.Modules.Car.UberX;
import com.example.uberclone.Modules.Car.UberXL;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UberXLDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carname;
    private EditText num_of_doors;
    private EditText num_of_passangers;
    private EditText price;

    private TextView warning;

    private Button addcar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_x_l_details);

        nameOfDriver = getNameOfDriver();

        carname = (EditText) findViewById(R.id.carname_uberxl);
        num_of_doors = (EditText) findViewById(R.id.numberofdoors_uberxl);
        num_of_passangers = (EditText) findViewById(R.id.maxpassangers_uberxl);
        price = (EditText) findViewById(R.id.price_uberxl);

        warning = (TextView) findViewById(R.id.warning_uberxl);

        addcar = (Button) findViewById(R.id.submitCarDetails_uberxl);
    }

    public void addCarToDatabase(View view) {

        if (!fieldsEmpty()){
            if (isValidCarLength()){
                if (CarMarks.isValideCarMarkt(String.valueOf(carname.getText()))){

                    String uberXLcarmark = String.valueOf(carname.getText());
                    String uberXLnumOfDoors= String.valueOf(num_of_doors.getText());
                    String uberXLnumOfPassangers = String.valueOf(num_of_passangers.getText());
                    String uberXLprice = String.valueOf(price.getText());


                    UberXL uberXL = new UberXL(uberXLcarmark,Integer.parseInt(uberXLnumOfDoors),Integer.parseInt(uberXLnumOfPassangers),Double.parseDouble(uberXLprice));

                    if (uberXL.isValidNumberOfDoors(UberXL.MAX_NUMBER_OF_DOORS) && uberXL.isValidNumberOfPassangers(UberXL.MAX_NUMBER_OF_PASSENGERS) && uberXL.isValidPrice(UberXL.MIN_PRICE_RANGE,UberXL.MAX_PRICE_RANGE)){

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference root = firebaseDatabase.getReference();

                        root.child("User").child("Driver").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){

                                    root.child("User").child("Driver").child(nameOfDriver).child("Car").child("UberXL").setValue(uberXL).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                         Log.i("UberXL car: ","ADDED");
                                            Intent toMainContent = new Intent(UberXLDetails.this, DriverMainContent.class);
                                            toMainContent.putExtra("drivername from cardetails",nameOfDriver);
                                            startActivity(toMainContent);
                                        }
                                    });
                                }
                                else{
                                    Log.e("UberXL username: ","doesnt exists");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else{
                        setWarning("Number of doors: 4; Number of passangers: 4");
                    }

                }
                else{
                    setWarning("Your car does not belong to this category!");
                }
            }
            else{
                setWarning("Car mark musst have minimum 3 latters!");
            }

        }
        else {
            setWarning("Some fields are empty. Try again!");
        }
    }


    public boolean fieldsEmpty() {
        if (TextUtils.isEmpty(carname.getText()) || TextUtils.isEmpty(num_of_passangers.getText()) || TextUtils.isEmpty(num_of_doors.getText()) || TextUtils.isEmpty(price.getText())) {
            return true;
        }
        return false;
    }


    public boolean isValidCarLength() {

        return String.valueOf(carname.getText()).length() >= 3;
    }

    public void setWarning(String message) {
        warning.setText(message);
        restartFields();
    }

    public void restartFields() {
        if (!TextUtils.isEmpty(carname.getText())) {
            carname.setText("");
        }
        if (!TextUtils.isEmpty(num_of_doors.getText())) {
            num_of_doors.setText("");
        }
        if (!TextUtils.isEmpty(num_of_passangers.getText())) {
            num_of_passangers.setText("");
        }
        if (!TextUtils.isEmpty(price.getText())) {
            price.setText("");
        }
    }


    public String getNameOfDriver() {
        if (this.getIntent().getStringExtra("driver from picker") != null) {
            return this.getIntent().getStringExtra("driver from picker");
        }
        return null;
    }
}