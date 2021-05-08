package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import com.example.uberclone.Extras.Adapters.ColorAdapter.ColorAdapter;
import com.example.uberclone.MainApp.Driver.DriverMainContent;
import com.example.uberclone.Modules.Car.CarMarks.SUVMarks;
import com.example.uberclone.Modules.Car.UberSUV;
import com.example.uberclone.Modules.Color.SUVColors;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import yuku.ambilwarna.AmbilWarnaDialog;

public class UberSUVDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carname;
    private EditText num_of_doors;
    private EditText num_of_passangers;
    private EditText price;

    private String[] colors;

    private Spinner interiorpicker;
    private Spinner enteriorpicker;

    private String interior_color;
    private String enterior_color;

    private ColorAdapter colorAdapter;

    private TextView warning;

    private Button addcar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_s_u_v_details);

        this.getSupportActionBar().hide();

        nameOfDriver = getNameOfDriver();
        Log.i("UberSUVDetails user", nameOfDriver);

        carname = (EditText) findViewById(R.id.carname_uberSUV);
        num_of_doors = (EditText) findViewById(R.id.numberofdoors_uberSUV);
        num_of_passangers = (EditText) findViewById(R.id.maxpassangers_uberSUV);
        price = (EditText) findViewById(R.id.price_uberSUV);

        warning = (TextView) findViewById(R.id.warning_uberSUV);

        interior_color = "";
        enterior_color = "";

        colors = getColors();

        interiorpicker = (Spinner) findViewById(R.id.interiorpicker);
        enteriorpicker = (Spinner) findViewById(R.id.enteriorpicker);

        colorAdapter = new ColorAdapter(UberSUVDetails.this, colors);

        interiorpicker.setAdapter(colorAdapter);
        enteriorpicker.setAdapter(colorAdapter);

        interiorpicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        interior_color = "Black";
                        break;
                    case 1:
                        interior_color = "White";
                        break;
                    case 2:
                        interior_color = "Yellow";
                        break;
                    case 3:
                        interior_color = "Green";
                        break;
                    case 4:
                        interior_color = "Red";
                        break;
                    case 5:
                        interior_color = "Blue";
                        break;
                    case 6:
                        interior_color = "Orange";
                        break;
                    case 7:
                        interior_color = "Violet";
                        break;
                    case 8:
                        interior_color = "Brown";
                        break;
                    case 9:
                        interior_color = "Grey";
                        break;

                    case 10:
                        interior_color = "Pink";
                        break;

                    default:
                        interior_color = "";
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
                        enterior_color = "Black";
                        break;
                    case 1:
                        enterior_color = "White";
                        break;
                    case 2:
                        enterior_color = "Yellow";
                        break;
                    case 3:
                        enterior_color = "Green";
                        break;
                    case 4:
                        enterior_color = "Red";
                        break;
                    case 5:
                        enterior_color = "Blue";
                        break;
                    case 6:
                        enterior_color = "Orange";
                        break;
                    case 7:
                        enterior_color = "Violet";
                        break;
                    case 8:
                        enterior_color = "Brown";
                        break;
                    case 9:
                        enterior_color = "Grey";
                        break;

                    case 10:
                        enterior_color = "Pink";
                        break;

                    default:
                        enterior_color = "";
                        break;
                }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        addcar = (Button) findViewById(R.id.submitCarDetails_uberSUV);


    }

    public void addCarToDatabase(View view) {
        if (!fieldsEmpty()) {
            if (isValidCarLength()) {
                if (SUVMarks.isValideSUVMark(String.valueOf(carname.getText()))) {

                    String uberSUVcarmark = String.valueOf(carname.getText());
                    String uberSUVnumOfDoors = String.valueOf(num_of_doors.getText());
                    String uberSUVnumOfPassangers = String.valueOf(num_of_passangers.getText());
                    String uberSUVcolor_interior = enterior_color;
                    String uberSUVcolor_enterior = interior_color;
                    String uberSUVprice = String.valueOf(price.getText());

                    UberSUV uberSUV = new UberSUV(uberSUVcarmark,Integer.parseInt(uberSUVnumOfDoors),Integer.parseInt(uberSUVnumOfPassangers),uberSUVcolor_enterior,uberSUVcolor_interior,Double.parseDouble(uberSUVprice));

                    if (valideInfos(uberSUV)){

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference root = firebaseDatabase.getReference();

                        root.child("User").child("Driver").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    root.child("User").child("Driver").child(nameOfDriver).setValue(uberSUV).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                         Log.i("UberSUV registration ","SUCCESEFULL");
                                            Intent toMainContent = new Intent(UberSUVDetails.this, DriverMainContent.class);
                                            toMainContent.putExtra("drivername from cardetails",nameOfDriver);
                                            startActivity(toMainContent);
                                        }
                                    });
                                }
                                else{
                                    Log.e("UberSUVDetails user: ","ERROR TO FIND A USER WIHT username "+nameOfDriver);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }



                } else {
                    setWarning("Your car does not belong to this category!");
                }

            } else {
                setWarning("Car mark musst have minimum 3 latters!");
            }

        } else {
            setWarning("Some fields are empty. Try again!");

        }
    }

    public boolean valideInfos(UberSUV uberSUV){

        if (uberSUV.isValidNumberOfDoors(UberSUV.MAX_NUMBER_OF_DOORS)){
            if (uberSUV.isValidNumberOfPassangers(4)){
                if (uberSUV.isValidPrice(UberSUV.MIN_PRICE_RANGE,UberSUV.MAX_PRICE_RANGE)){
                    if (uberSUV.isValideEnterior(enterior_color)){
                        if (uberSUV.isValideInterior(interior_color)){
                            return true;
                        }
                        else{
                            setWarning("Interior of SUV musst be black");
                            return false;
                        }
                    }
                    else{
                        setWarning("Enterior of SUV musst be black");
                        return false;
                    }

                }
                setWarning("Price musst be between "+UberSUV.MIN_PRICE_RANGE +" and "+UberSUV.MAX_PRICE_RANGE);
                return false;

            }
            else{
                setWarning("SUV musst have maximum 4 passangers");
                return false;
            }

        }
        else {
            setWarning("SUV musst have minumum 1 and maximum 4 doors");
            return false;
        }
    }



    public String[] getColors() {
        String[] colorFormEnum = new String[11];
        int i = 0;

        for (SUVColors colors : SUVColors.values()) {
            colorFormEnum[i] = colors.getColorname();
            i++;
        }

        return colorFormEnum;
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

    public String getNameOfDriver() {
        String drivername_from_intent = this.getIntent().getStringExtra("driver from picker");

        if (drivername_from_intent != null) {
            return drivername_from_intent;
        }

        return null;
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
}