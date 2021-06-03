package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberclone.MainApp.Driver.DriverMainContentLobby;
import com.example.uberclone.Models.Car.CarMarks.CarMarks;
import com.example.uberclone.Models.Car.UberWAV;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UberWAVDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carnameField;
    private EditText maxDoorsField;
    private EditText maxPassangersField;
    private EditText priceField;

    private CheckBox passCertificationCheck;
    private CheckBox wheelchairEqipmentCheck;

    private TextView warning;

    private Button addCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_wavdetails);

        nameOfDriver = getNameOfDriver();

        carnameField = (EditText) findViewById(R.id.carname_uberWAV);
        maxDoorsField = (EditText) findViewById(R.id.numberofdoors_uberWAV);
        maxPassangersField = (EditText) findViewById(R.id.maxpassangers_uberWAV);
        priceField = (EditText) findViewById(R.id.price_uberWAV);

        passCertificationCheck = (CheckBox) findViewById(R.id.passCertification_checkBox);
        wheelchairEqipmentCheck = (CheckBox) findViewById(R.id.wheelchair_checkBox);

        warning = (TextView) findViewById(R.id.warning_uberWAV);

        addCar = (Button) findViewById(R.id.submitCarDetails_uberWAV);
    }

    public void addCarInDatabase(View view){
        if (fieldsEmpty()){
            if (isValidCarLength()){
                if (CarMarks.isValideCarMarkt(String.valueOf(carnameField.getText()))){
                    if (isWheelchairEquipmentChecked()){
                        if (isPassCertificationChecked()){
                            String uberWAV_makname = String.valueOf(carnameField.getText());
                            String uberWAV_maxDoors = String.valueOf(maxDoorsField.getText());
                            String uberWAV_maxPassangers = String.valueOf(maxPassangersField.getText());
                            boolean uberWAV_wheelchairEqipment = isWheelchairEquipmentChecked();
                            boolean uberWAV_passcertification = isPassCertificationChecked();
                            String uberWAV_price = String.valueOf(priceField.getText());

                            UberWAV uberWAV = new UberWAV(uberWAV_makname,Integer.parseInt(uberWAV_maxDoors),Integer.parseInt(uberWAV_maxPassangers),uberWAV_wheelchairEqipment,uberWAV_passcertification,Double.parseDouble(uberWAV_price));

                            if (isValidUberWAV(uberWAV)){
                                addUberLuxInDatabase(uberWAV);
                                Intent toMain = new Intent(UberWAVDetails.this, DriverMainContentLobby.class);
                                toMain.putExtra("drivername from cardetails",nameOfDriver);
                                startActivity(toMain);
                            }
                        }
                        else {
                            setWarning("UberWAV need to pass certification");
                        }
                    }
                    else{
                        setWarning("UberWAV need to have wheelchair equipment");
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

    public boolean isValidUberWAV(UberWAV uberWAV){
        if (uberWAV.isValidNumberOfPassangers(UberWAV.MAX_NUMBER_OF_PASSENGERS)){
            if (uberWAV.isValidNumberOfDoors(UberWAV.MAX_NUMBER_OF_DOORS)){
                if (uberWAV.isValidPrice(UberWAV.MIN_PRICE_RANGE,UberWAV.MAX_PRICE_RANGE)){
                    if (uberWAV.isValideUberWAV()){
                        return true;
                    }
                    else{
                        setWarning("UberWAV need to have commercial registration and insurance");
                    }
                }
                else{
                    setWarning("Price musst be between " + UberWAV.MIN_PRICE_RANGE + " and " + UberWAV.MAX_PRICE_RANGE);
                }
            }
            else{
                setWarning("UberWAV need to have minumum 1 and maximum "+ UberWAV.MAX_NUMBER_OF_DOORS+ " doors");
            }
        }
        else{
            setWarning("UberWAV need to have maximum "+ UberWAV.MAX_NUMBER_OF_PASSENGERS +" passangers");
        }
        return false;
    }

    public void addUberLuxInDatabase(UberWAV uberWAV){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Driver").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    root.child("User").child("Driver").child(nameOfDriver).child("Car").child("UberWAV").setValue(uberWAV).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                         Log.i("Car added","UberWAV succesefull added in database");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Log.e("Car failed","Failed to add UberWAV in database");
                        }
                    });
                }
                else{
                    Log.e("Driver's path","Can not find driver's path in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean isPassCertificationChecked(){
        return this.passCertificationCheck.isChecked();
    }

    public boolean isWheelchairEquipmentChecked(){
        return wheelchairEqipmentCheck.isChecked();
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