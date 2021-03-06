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

import com.example.uberclone.MainApp.Driver.DriverMainContentLobby;
import com.example.uberclone.Models.Car.CarMarks.CarMarks;
import com.example.uberclone.Models.Car.UberX;
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

    private TextView warning;

    private Button addCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_x_details);

        this.getSupportActionBar().hide();

        drivername = getDriverName();
        Log.i("Name from car picker", drivername);

        carname = (EditText) findViewById(R.id.carname_uberx);
        number_of_doors = (EditText) findViewById(R.id.numberofdoors_uberx);
        number_of_passangers = (EditText) findViewById(R.id.maxpassangers_uberx);
        price = (EditText) findViewById(R.id.price_uberx);

        warning = (TextView) findViewById(R.id.warning_uberx);

        addCar = (Button) findViewById(R.id.submitCarDetails_uberx);



    }

    public void addCarToDatabase(View view){
        if (!fieldsEmpty()){
            if (isValidCarLength()){
                if (CarMarks.isValideCarMarkt(String.valueOf(carname.getText()))){

                    String uberXcar = String.valueOf(carname.getText());
                    String uberXnumOfDoors = String.valueOf(number_of_doors.getText());
                    String uberXNumberOfPassangers = String.valueOf(number_of_passangers.getText());
                    String uberXprice = String.valueOf(price.getText());

                    UberX uberX = new UberX(uberXcar,Integer.parseInt(uberXnumOfDoors),Integer.parseInt(uberXNumberOfPassangers),Double.parseDouble(uberXprice));

                    if(valideRequirements(uberX)){

                        Log.i("UberXDetails update:","VALID CAR");

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference root = firebaseDatabase.getReference();

                        root.child("User").child("Driver").child(drivername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    root.child("User").child("Driver").child(drivername).child("Car").child("UberX").setValue(uberX).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                         Log.i("UberXDetails car: ","ADDED");
                                            Intent toMainContent = new Intent(UberXDetails.this, DriverMainContentLobby.class);
                                            toMainContent.putExtra("drivername from cardetails",drivername);
                                            startActivity(toMainContent);
                                        }
                                    });
                                }
                                else{
                                    Log.i("UberXDetails user: ","EXISTS");
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
            else{
                setWarning("Car mark musst have minimum 3 latters!");
            }

        }
        else{
            setWarning("Some fields are empty. Try again!");
        }

    }

    public boolean valideRequirements(UberX uberX){
        if (uberX.isValidNumberOfDoors(UberX.MAX_NUMBER_OF_DOORS)){
            if (uberX.isValidNumberOfPassangers(UberX.MAX_NUMBER_OF_PASSENGERS)){
                if (uberX.isValidPrice(UberX.MIN_PRICE_RANGE,UberX.MAX_PRICE_RANGE)){
                    return true;
                }
                else{
                    setWarning("Price musst be between "+UberX.MIN_PRICE_RANGE+" and "+UberX.MAX_PRICE_RANGE);
                    return false;
                }

            }
            else{
                setWarning("UberX musst have maximum +"+UberX.MAX_NUMBER_OF_PASSENGERS +" passangers");
                return false;
            }

        }
        else{
            setWarning("UberX musst have minumum 1 and maximum "+UberX.MAX_NUMBER_OF_DOORS +" doors");
            return false;
        }
    }

    public boolean fieldsEmpty(){
        if (TextUtils.isEmpty(carname.getText()) || TextUtils.isEmpty(number_of_doors.getText()) || TextUtils.isEmpty(number_of_passangers.getText()) || TextUtils.isEmpty(price.getText())){
            return true;
        }
        return false;
    }



    public String getDriverName() {
        if (this.getIntent().getStringExtra("driver from picker") != null) {
            return this.getIntent().getStringExtra("driver from picker");
        }

        return null;
    }

    public boolean isValidCarLength(){

        return String.valueOf(carname.getText()).length() >= 3;
    }


    public void setWarning(String message){
        warning.setText(message);
        restartFields();
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