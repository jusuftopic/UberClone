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

import com.example.uberclone.Modules.Car.CarMarks.CarMarks;
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

        this.getSupportActionBar().hide();

        drivername = getDriverName();
        Log.i("Name from car picker", drivername);

        carname = (EditText) findViewById(R.id.carname);
        number_of_doors = (EditText) findViewById(R.id.numberofdoors);
        number_of_passangers = (EditText) findViewById(R.id.maxpassangers);
        price = (EditText) findViewById(R.id.price);

        Toast.makeText(UberXDetails.this,String.valueOf(number_of_doors.getText()),Toast.LENGTH_LONG).show();

        addCar = (Button) findViewById(R.id.submitCarDetails);
    }

    public void addCarToDatabase(View view){
        if (!fieldsEmpty()){
            if (isValidCarLength()){
                if (CarMarks.isValideCarMarkt(String.valueOf(carname.getText()))){

                }

            }
            else{
                Toast.makeText(UberXDetails.this,"Name of car musst have minimal 3 latters",Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast.makeText(UberXDetails.this,"Some fields are empty. Try again!",Toast.LENGTH_LONG).show();
            restartFields();
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

   /* public boolean isValideCarMark(String mark){

        for (CarMarks carMarks : CarMarks.values()){
            if (carMarks.getCarmark_name().equalsIgnoreCase(mark)){
                return true;
            }
        }



        return false;
    }*/

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