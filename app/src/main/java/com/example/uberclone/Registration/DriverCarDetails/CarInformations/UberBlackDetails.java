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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.uberclone.Extras.Adapters.ColorAdapter;
import com.example.uberclone.MainApp.Driver.DriverMainContentLobby;
import com.example.uberclone.Models.Car.CarMarks.CarMarks;
import com.example.uberclone.Models.Car.UberBlack;
import com.example.uberclone.Models.Color.Colors;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UberBlackDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carname;
    private EditText max_num_of_passangers;
    private EditText max_num_of_doors;
    private EditText price;

    private String interior_color_to_pick;
    private String enterior_color_to_pick;

    private Spinner interiorpicker;
    private Spinner enteriorpicker;
    private String colors[];
    private ColorAdapter colorAdapter;

    private CheckBox airportPermit_checkbox;
    private boolean airport_permit;

    private TextView warning;

    private Button addCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_black);

        nameOfDriver = getNameOfDriver();

        carname = (EditText) findViewById(R.id.carname_uberBlack);
        max_num_of_doors = (EditText) findViewById(R.id.numberofdoors_uberBlack);
        max_num_of_passangers = (EditText) findViewById(R.id.maxpassangers_uberBlack);
        price = (EditText) findViewById(R.id.price_uberBlack);

        interior_color_to_pick = "";
        enterior_color_to_pick = "";


        interiorpicker = (Spinner) findViewById(R.id.interiorpickerUberBlack);
        enteriorpicker = (Spinner) findViewById(R.id.enteriorpickerUberBlack);
        colors = getColors();
        colorAdapter = new ColorAdapter(UberBlackDetails.this,colors);

        airportPermit_checkbox = (CheckBox) findViewById(R.id.airport_permit);
        airport_permit = false;

        warning = (TextView) findViewById(R.id.warning_uberBlack);

        addCar = (Button) findViewById(R.id.submitCarDetails_uberBlack);

        interiorpicker.setAdapter(colorAdapter);
        enteriorpicker.setAdapter(colorAdapter);

        interiorpicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        interior_color_to_pick = "Black";
                        break;
                    case 1:
                        interior_color_to_pick = "White";
                        break;
                    case 2:
                        interior_color_to_pick = "Yellow";
                        break;
                    case 3:
                        interior_color_to_pick = "Green";
                        break;
                    case 4:
                        interior_color_to_pick = "Red";
                        break;
                    case 5:
                        interior_color_to_pick = "Blue";
                        break;
                    case 6:
                        interior_color_to_pick = "Orange";
                        break;
                    case 7:
                        interior_color_to_pick = "Violet";
                        break;
                    case 8:
                        interior_color_to_pick = "Brown";
                        break;
                    case 9:
                        interior_color_to_pick = "Grey";
                        break;

                    case 10:
                        interior_color_to_pick = "Pink";
                        break;

                    default:
                        interior_color_to_pick = "";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enteriorpicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        enterior_color_to_pick = "Black";
                        break;
                    case 1:
                        enterior_color_to_pick = "White";
                        break;
                    case 2:
                        enterior_color_to_pick = "Yellow";
                        break;
                    case 3:
                        enterior_color_to_pick = "Green";
                        break;
                    case 4:
                        enterior_color_to_pick = "Red";
                        break;
                    case 5:
                        enterior_color_to_pick = "Blue";
                        break;
                    case 6:
                        enterior_color_to_pick = "Orange";
                        break;
                    case 7:
                        enterior_color_to_pick = "Violet";
                        break;
                    case 8:
                        enterior_color_to_pick = "Brown";
                        break;
                    case 9:
                        enterior_color_to_pick = "Grey";
                        break;

                    case 10:
                        enterior_color_to_pick = "Pink";
                        break;

                    default:
                        enterior_color_to_pick = "";
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
                    String uberBlack_markname = String.valueOf(carname.getText());
                    String uberBlack_numOfDoors = String.valueOf(max_num_of_doors.getText());
                    String uberBlack_numOfPassangers = String.valueOf(max_num_of_passangers.getText());
                    String uberBlack_price = String.valueOf(price.getText());
                    String uberBlack_enterior = enterior_color_to_pick;
                    String uberBlack_interior = interior_color_to_pick;
                    boolean uberBlack_AirportPermit = airport_permit;

                    UberBlack uberBlack = new UberBlack(uberBlack_markname,Integer.parseInt(uberBlack_numOfDoors),Integer.parseInt(uberBlack_numOfPassangers),uberBlack_enterior,uberBlack_interior,uberBlack_AirportPermit,Double.parseDouble(uberBlack_price));

                    if (valideCarInfos(uberBlack)){
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference root = firebaseDatabase.getReference();

                        root.child("User").child("Driver").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    root.child("User").child("Driver").child(nameOfDriver).child("Car").child("UberBlack").setValue(uberBlack).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                         Log.i("UberBlackDetails: ","SUCCESEFULL IN DATABASE");
                                            Intent toMainApp = new Intent(UberBlackDetails.this, DriverMainContentLobby.class);
                                            toMainApp.putExtra("drivername from cardetails",nameOfDriver);
                                            startActivity(toMainApp);
                                        }
                                    });

                                }
                                else {
                                    Log.e("UberBlackDetails: ","PROBLEM WITH USER'S RECOGNATION");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Database error", error.getMessage());
                            }
                        });
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

    public boolean valideCarInfos(UberBlack uberBlack){
        if (uberBlack.isValidNumberOfDoors(UberBlack.MAX_NUMBER_OF_DOORS)){
            if (uberBlack.isValidNumberOfPassangers(UberBlack.MAX_NUMBER_OF_PASSENGERS)){
                if (uberBlack.isValidPrice(UberBlack.MIN_PRICE_RANGE,UberBlack.MAX_PRICE_RANGE)){
                    if (uberBlack.isValideEnterior() && uberBlack.isValideInterior()){
                        if (uberBlack.getAirportPermit() == true){

                        }
                        else{
                            setWarning("UberBlack musst have airport permit");
                        }
                    }
                    else{
                        setWarning("Interior/Enterior musst be black");
                    }
                }
                else{
                    setWarning("Price musst be between " + UberBlack.MIN_PRICE_RANGE + " and " + UberBlack.MAX_PRICE_RANGE);
                }
            }
            else {
                setWarning("UberBlack musst have maximum "+ UberBlack.MAX_NUMBER_OF_PASSENGERS +" passangers");
            }
        }
        else{
            setWarning("SUV musst have minumum 1 and maximum "+ UberBlack.MAX_NUMBER_OF_DOORS+ " doors");
        }
        return false;
    }

    public void hasAirportPermit(){
        if (airportPermit_checkbox.isChecked()){
            airport_permit = true;
            return;
        }
    }

    public String[] getColors(){
        String[] cols = new String[11];
        int index = 0;

        for (Colors color : Colors.values()){
            cols[index] = color.getColorname();
            index++;
        }
        return cols;
    }

    public void setWarning(String message){
        warning.setText(message);
        restartFields();
    }
    public boolean fieldsEmpty() {
        if (TextUtils.isEmpty(carname.getText()) || TextUtils.isEmpty(max_num_of_passangers.getText()) || TextUtils.isEmpty(max_num_of_doors.getText()) || TextUtils.isEmpty(price.getText())) {
            return true;
        }
        return false;
    }


    public boolean isValidCarLength() {

        return String.valueOf(carname.getText()).length() >= 3;
    }

    public void restartFields() {
        if (!TextUtils.isEmpty(carname.getText())) {
            carname.setText("");
        }
        if (!TextUtils.isEmpty(max_num_of_doors.getText())) {
            max_num_of_doors.setText("");
        }
        if (!TextUtils.isEmpty(max_num_of_passangers.getText())) {
            max_num_of_passangers.setText("");
        }
        if (!TextUtils.isEmpty(price.getText())) {
            price.setText("");
        }
    }

    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("driver from picker") != null){
            return this.getIntent().getStringExtra("driver from picker");
        }
        else {
            Log.e("UberBlackDetails: ","Trasport of name failed");
            return null;
        }
    }
}