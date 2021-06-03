package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.uberclone.Extras.Adapters.InteriorAdapter;
import com.example.uberclone.MainApp.Driver.DriverMainContentLobby;
import com.example.uberclone.Models.Car.CarMarks.CarMarks;
import com.example.uberclone.Models.Car.InteriorType.InteriorType;
import com.example.uberclone.Models.Car.UberSelect;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UberSelectDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carname;
    private EditText numOfDoors;
    private EditText numOfPassangers;
    private EditText price;

    private Spinner interiorpicker;
    private InteriorAdapter interiorAdapter;

    private TextView warning;

    private Button addCar;

    private String[] interiors;

    private String pickinterior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_select_details);

        this.getSupportActionBar().hide();

        nameOfDriver = getNameOfDriver();
        Log.i("UberSelect drivername: ", nameOfDriver);

        carname = (EditText) findViewById(R.id.carname_uberSelect);
        numOfDoors = (EditText) findViewById(R.id.numberofdoors_uberSelect);
        numOfPassangers = (EditText) findViewById(R.id.maxpassangers_uberSelect);
        price = (EditText) findViewById(R.id.price_uberSelect);

        warning = (TextView) findViewById(R.id.warning_uberSelect);
        addCar = (Button) findViewById(R.id.submitCarDetails_uberSelect);

        interiors = getInteriors();
        pickinterior = "";

        interiorpicker = (Spinner) findViewById(R.id.interiorpicker_UberSelect);
        interiorAdapter = new InteriorAdapter(UberSelectDetails.this,interiors);
        interiorpicker.setAdapter(interiorAdapter);

        interiorpicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        pickinterior = "LEATHER";
                        break;
                    case 1:
                        pickinterior = "VINLY";
                        break;
                    case 2:
                        pickinterior = "POLYESTER";
                        break;
                    case 3:
                        pickinterior = "NYLON";
                    default:
                        pickinterior = "";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addCarToDatabase(View view){
        if (!fieldsEmpty()){
            if (isValidCarLength()){
                if (CarMarks.isValideCarMarkt(String.valueOf(carname.getText()))){
                    String uberSelect_carname = String.valueOf(carname.getText());
                    String uberSelect_numOfDoors = String.valueOf(numOfDoors.getText());
                    String uberSelect_numOfPassangers = String.valueOf(numOfPassangers.getText());
                    String uberSelect_price = String.valueOf(price.getText());
                    String uberSelect_interior = pickinterior;

                    UberSelect uberSelect = new UberSelect(uberSelect_carname,Integer.parseInt(uberSelect_numOfDoors),Integer.parseInt(uberSelect_numOfPassangers),uberSelect_interior,Double.parseDouble(uberSelect_price));

                    if (valideCar(uberSelect)){
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference root = firebaseDatabase.getReference();

                        root.child("User").child("Driver").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    root.child("User").child("Driver").child(nameOfDriver).child("Car").child("UberSelect").setValue(uberSelect).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("UberSelect reg ", "SUCCESEFULL");
                                            Intent toMainContent = new Intent(UberSelectDetails.this, DriverMainContentLobby.class);
                                            toMainContent.putExtra("drivername from cardetails", nameOfDriver);
                                            startActivity(toMainContent);
                                        }
                                    });
                                }
                                else{
                                    Log.e("UberSelectDetails: ", "ERROR TO FIND A USER WITH username " + nameOfDriver);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
                else {
                    setWarning("Your car does not belong to this category!");
                }
            }
            else {
                setWarning("Car mark musst have minimum 3 latters!");
            }

        }
        else{
            setWarning("Some fields are empty. Try again!");
        }
    }

    public boolean valideCar(UberSelect uberSelect){
        if (uberSelect.isValidNumberOfDoors(UberSelect.MAX_NUMBER_OF_DOORS)){
            if (uberSelect.isValidNumberOfPassangers(UberSelect.MAX_NUMBER_OF_PASSENGERS)){
                if (uberSelect.isValidPrice(UberSelect.MIN_PRICE_RANGE,UberSelect.MAX_PRICE_RANGE)){
                    if (uberSelect.isValidInterior()){
                        return true;
                    }
                    else{
                        setWarning("Interior musst be Leather or Vinly");
                    }

                }
                else {
                    setWarning("Price musst be between "+UberSelect.MIN_PRICE_RANGE+"-"+UberSelect.MAX_PRICE_RANGE+"$");
                }
            }
            else {
                setWarning("Car must have maximum "+UberSelect.MAX_NUMBER_OF_PASSENGERS+" passangers");
            }
        }
        else{
            setWarning("Car must have maximum "+UberSelect.MAX_NUMBER_OF_DOORS+" doors");
        }
        return false;
    }

    public String[] getInteriors(){
        String[] uberSelect_interiors = new String[4];
        int counter = 0;

        for (InteriorType type : InteriorType.values()){
            uberSelect_interiors[counter] = type.getInteriortype();
            counter++;
        }

        return uberSelect_interiors;
    }

    public boolean fieldsEmpty() {
        if (TextUtils.isEmpty(carname.getText()) || TextUtils.isEmpty(numOfPassangers.getText()) || TextUtils.isEmpty(numOfDoors.getText()) || TextUtils.isEmpty(price.getText())) {
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
        if (!TextUtils.isEmpty(numOfDoors.getText())) {
            numOfDoors.setText("");
        }
        if (!TextUtils.isEmpty(numOfPassangers.getText())) {
            numOfPassangers.setText("");
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