package com.example.uberclone.Registration.DriverCarDetails.CarInformations.UberExpressPoolDetails;

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
import com.example.uberclone.Modules.Car.UberExpressPool;
import com.example.uberclone.Modules.Car.UberLux;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UberExpressPoolDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carnameField;
    private EditText maxDoorsField;
    private EditText maxPassangersField;
    private EditText priceField;

    private Button setLocations;

    private TextView warning;

    private Button addCar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_express_pool_details);

        nameOfDriver = getNameOfDriver();

        carnameField = (EditText) findViewById(R.id.carname_uberExpressPool);
        maxDoorsField = (EditText) findViewById(R.id.numberofdoors_uberExpressPool);
        maxPassangersField = (EditText) findViewById(R.id.maxpassangers_uberExpressPool);
        priceField = (EditText) findViewById(R.id.price_uberExpressPool);

        setLocations = (Button) findViewById(R.id.setLocations);

        warning = (TextView) findViewById(R.id.warning_uberExpressPool);

        addCar = (Button) findViewById(R.id.submitCarDetails_uberExpressPool);

        addCar.setEnabled(false);

    }

    public void goAndSetLocations(View view) {
        Intent toSpots = new Intent(UberExpressPoolDetails.this, SpotPicker.class);
        toSpots.putExtra("driver name for spots", nameOfDriver);
        startActivity(toSpots);
    }

    public void addCarInDatabasePool(View view) {
        if (addCar.isEnabled()) {
            if (fieldsEmpty()) {
                if (isValidCarLength()) {
                    if (CarMarks.isValideCarMarkt(String.valueOf(carnameField.getText()))) {
                        String uberExpressPool_makname = String.valueOf(carnameField.getText());
                        String uberExpressPool_maxDoors = String.valueOf(maxDoorsField.getText());
                        String uberExpressPool_maxPassangers = String.valueOf(maxPassangersField.getText());
                        //TODO check if driver picked spots
                        //      boolean pickedLocs = pickedLocations();
                        String uberExpressPool_price = String.valueOf(priceField.getText());

                        //TODO add arraylist of spots
                        UberExpressPool uberExpressPool = new UberExpressPool(uberExpressPool_makname, Integer.parseInt(uberExpressPool_maxDoors), Integer.parseInt(uberExpressPool_maxPassangers), null, Double.parseDouble(uberExpressPool_price));

                        if (isValidUberExpressPool(uberExpressPool)) {
                            addUberLuxInDatabase(uberExpressPool);
                            Intent toMain = new Intent(UberExpressPoolDetails.this, DriverMainContent.class);
                            toMain.putExtra("drivername from cardetails", nameOfDriver);
                            startActivity(toMain);
                        }

                    } else {
                        setWarning("Your car does not belong to this category!");
                    }
                } else {
                    setWarning("Car mark need to have minimum 3 latters!");
                }

            } else {
                setWarning("Some fields are empty. Try again!");
            }
        }
    }

    public boolean isValidUberExpressPool(UberExpressPool uberExpressPool) {
        if (uberExpressPool.isValidNumberOfPassangers(UberExpressPool.MAX_NUMBER_OF_PASSENGERS)) {
            if (uberExpressPool.isValidNumberOfDoors(UberExpressPool.MAX_NUMBER_OF_DOORS)) {
                if (uberExpressPool.isValidPrice(UberExpressPool.MIN_PRICE_RANGE, UberExpressPool.MAX_PRICE_RANGE)) {
                   /* if (pickedFiveLocations()) {
                        return true;
                    }
                    else {

                    }*/
                } else {
                    setWarning("Price musst be between " + UberLux.MIN_PRICE_RANGE + " and " + UberLux.MAX_PRICE_RANGE);
                }
            } else {
                setWarning("UberLux need to have minumum 1 and maximum " + UberLux.MAX_NUMBER_OF_DOORS + " doors");
            }

        } else {
            setWarning("UberLux need to have maximum " + UberLux.MAX_NUMBER_OF_PASSENGERS + " passangers");
        }
        return false;
    }




    public void addUberLuxInDatabase(UberExpressPool uberExpressPool) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Driver").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    root.child("User").child("Driver").child(nameOfDriver).child("Car").child("UberExpressPool").setValue(uberExpressPool).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.i("Car added", "UberExpressPool succesefull added in database");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Car failed", "Failed to add UberExpressPool in database");
                                }
                            });
                } else {
                    Log.e("Driver's path", "Can not find driver's path in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setWarning(String message) {
        warning.setText(message);
        restartFields();
    }

    public boolean fieldsEmpty() {
        if (TextUtils.isEmpty(carnameField.getText()) || TextUtils.isEmpty(maxPassangersField.getText()) || TextUtils.isEmpty(maxDoorsField.getText()) || TextUtils.isEmpty(priceField.getText())) {
            return true;
        }
        return false;
    }


    public boolean isValidCarLength() {

        return String.valueOf(carnameField.getText()).length() >= 3;
    }

    public void restartFields() {
        if (!TextUtils.isEmpty(carnameField.getText())) {
            carnameField.setText("");
        }
        if (!TextUtils.isEmpty(maxDoorsField.getText())) {
            maxDoorsField.setText("");
        }
        if (!TextUtils.isEmpty(maxPassangersField.getText())) {
            maxDoorsField.setText("");
        }
        if (!TextUtils.isEmpty(priceField.getText())) {
            priceField.setText("");
        }
    }


    public String getNameOfDriver() {
        if (this.getIntent().getStringExtra("driver from picker") != null && !this.getIntent().getStringExtra("driver from picker").equalsIgnoreCase("")) {
            return this.getIntent().getStringExtra("driver from picker");
        }
        return null;
    }
}