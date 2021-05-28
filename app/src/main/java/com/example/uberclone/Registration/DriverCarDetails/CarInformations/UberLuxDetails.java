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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberclone.MainApp.Driver.DriverMainContent;
import com.example.uberclone.Modules.Car.CarMarks.CarMarks;
import com.example.uberclone.Modules.Car.UberBlack;
import com.example.uberclone.Modules.Car.UberLux;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UberLuxDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carnameField;
    private EditText maxDoorsField;
    private EditText maxPassangersField;
    private EditText priceField;

    private CheckBox commercialRegistrationCheck;
    private CheckBox insuranceCheck;

    private TextView warning;

    private Button addCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_lux_details);

        nameOfDriver = getNameOfDriver();

        carnameField = (EditText) findViewById(R.id.carname_uberLux);
        maxDoorsField = (EditText) findViewById(R.id.numberofdoors_uberLux);
        maxPassangersField = (EditText) findViewById(R.id.maxpassangers_uberLux);
        priceField = (EditText) findViewById(R.id.price_uberLux);

        commercialRegistrationCheck = (CheckBox) findViewById(R.id.commercial_reg_checkBox);
        insuranceCheck = (CheckBox) findViewById(R.id.insurance_checkBox);

        warning = (TextView) findViewById(R.id.warning_uberLux);

        addCar = (Button) findViewById(R.id.submitCarDetails_uberLux);
    }


    public void addCarInDatabase(View view){
        if (fieldsEmpty()){
            if (isValidCarLength()){
                if (CarMarks.isValideCarMarkt(String.valueOf(carnameField.getText()))){
                    if (hasCommercialRegistration()){
                        if (hasInsurance()){
                          String uberLux_makname = String.valueOf(carnameField.getText());
                          String uberLux_maxDoors = String.valueOf(maxDoorsField.getText());
                          String uberLux_maxPassangers = String.valueOf(maxPassangersField.getText());
                          boolean uberLux_commercialRegistration = hasCommercialRegistration();
                          boolean uberLux_insurance = hasInsurance();
                          String uberLux_price = String.valueOf(priceField.getText());

                            UberLux uberLux = new UberLux(uberLux_makname,Integer.parseInt(uberLux_maxDoors),Integer.parseInt(uberLux_maxPassangers),uberLux_commercialRegistration,uberLux_insurance,Double.parseDouble(uberLux_price));

                            if (isValidUberLux(uberLux)){
                                addUberLuxInDatabase(uberLux);
                                Intent toMain = new Intent(UberLuxDetails.this, DriverMainContent.class);
                                toMain.putExtra("drivername from cardetails",nameOfDriver);
                                startActivity(toMain);
                            }
                        }
                        else {
                            setWarning("UberLux need to have insurance");
                        }
                    }
                    else{
                        setWarning("UberLux need to have commercial registration");
                    }
                }
                else{
                    setWarning("Your car does not belong to this category!");
                }
            }
            else{
                setWarning("Car mark need to have minimum 3 latters!");
            }

        }
        else{
            setWarning("Some fields are empty. Try again!");
        }

    }

    public boolean isValidUberLux(UberLux uberLux){
        if (uberLux.isValidNumberOfPassangers(UberLux.MAX_NUMBER_OF_PASSENGERS)){
            if (uberLux.isValidNumberOfDoors(UberLux.MAX_NUMBER_OF_DOORS)){
                if (uberLux.isValidPrice(UberLux.MIN_PRICE_RANGE,UberLux.MAX_PRICE_RANGE)){
                    if (uberLux.isValidUberLux()){
                        return true;
                    }
                    else{
                        setWarning("UberLux need to have commercial registration and insurance");
                    }
                }
                else{
                    setWarning("Price musst be between " + UberLux.MIN_PRICE_RANGE + " and " + UberLux.MAX_PRICE_RANGE);
                }
            }
            else{
                setWarning("UberLux need to have minumum 1 and maximum "+ UberLux.MAX_NUMBER_OF_DOORS+ " doors");
            }
        }
        else{
            setWarning("UberLux need to have maximum "+ UberLux.MAX_NUMBER_OF_PASSENGERS +" passangers");
        }
        return false;
    }

    public void addUberLuxInDatabase(UberLux uberLux){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Driver").child(nameOfDriver).child("Car").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.getChildrenCount() > 0){
                        setDialog(uberLux);
                    }
                    else{
                        root.child("User").child("Driver").child(nameOfDriver).child("Car").child("Car").child("UberLux").setValue(uberLux).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("Car added","Succesefull added UberLux in database");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
                else{
                    Log.e("Driver's path","Can not find driver's path in database");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    public void setDialog(UberLux uberLux){
        AlertDialog.Builder builder = new AlertDialog.Builder(UberLuxDetails.this)
                .setIcon(android.R.drawable.stat_notify_error)
                .setTitle("Already registred car")
                .setMessage("You already registred one car.\nDo you want to replace him with new car?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        swapCarInDatabase(uberLux);
                    }
                })
                .setNegativeButton("No",null);
    }

    public void swapCarInDatabase(UberLux uberLux){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Driver").child(nameOfDriver).child("Car").child("UberLux").setValue(uberLux)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("Swaped car","Succesefull swaped car in database");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Log.e("Swap failed","Failed to swap cars in database");
                        e.printStackTrace();
                    }
                });
    }

    public boolean hasCommercialRegistration(){
        return this.commercialRegistrationCheck.isChecked();
    }

    public boolean hasInsurance(){
        return this.insuranceCheck.isChecked();
    }

    public void setWarning(String message){
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


    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("driver from picker") != null && !this.getIntent().getStringExtra("driver from picker").equalsIgnoreCase("")){
            return this.getIntent().getStringExtra("driver from picker");
        }

        Log.e("Name of driver", "Problem to transport name of driver to UberLux class");
        return null;
    }
}