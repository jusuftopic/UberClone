package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberclone.Modules.Car.CarMarks.SUVMarks;
import com.example.uberclone.Modules.Car.UberSUV;
import com.example.uberclone.R;

import yuku.ambilwarna.AmbilWarnaDialog;

public class UberSUVDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carname;
    private EditText num_of_doors;
    private EditText num_of_passangers;
    private EditText price;

    private Button choose_interior_color;
    private Button choose_enterior_color;

    private String interior_color;
    private String enterior_color;

    private int defaultColor;

    private TextView warning;

    private Button addcar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_s_u_v_details);

        nameOfDriver = getNameOfDriver();
        Log.i("UberSUVDetails user",nameOfDriver);

        carname = (EditText) findViewById(R.id.carname_uberSUV);
        num_of_doors = (EditText) findViewById(R.id.numberofdoors_uberSUV);
        num_of_passangers = (EditText) findViewById(R.id.maxpassangers_uberSUV);
        price = (EditText) findViewById(R.id.price_uberSUV);

        warning = (TextView) findViewById(R.id.warning_uberSUV);

        choose_interior_color = (Button) findViewById(R.id.interiorbutton);
        choose_enterior_color = (Button) findViewById(R.id.enteriorbutton);

        interior_color = "";
        enterior_color = "";

        defaultColor = ContextCompat.getColor(UberSUVDetails.this,R.color.colorPrimary);

        addcar = (Button) findViewById(R.id.submitCarDetails_uberSUV);


    }

    public void addCarToDatabase(View view){
        if (!fieldsEmpty()){
            if (isValidCarLength()){
                if (SUVMarks.isValideSUVMark(String.valueOf(carname.getText()))){

                    String uberSUVcarmark = String.valueOf(carname.getText());
                    String uberSUVnumOfDoors = String.valueOf(num_of_doors.getText());
                    String uberSUVnumOfPassangers = String.valueOf(num_of_passangers.getText());
                    String uberSUVprice = String.valueOf(price.getText());

                    /*UberSUV uberSUV = new UberSUV(uberSUVcarmark,uberSUVnumOfDoors,uberSUVnumOfPassangers,uberSUVprice)

                    if ()*/

                }
                else{
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

    public void pickInterior(View view){

    }

    public void pickEnterior(View view){

    }

    public void openColorPicker(){
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(UberSUVDetails.this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                warning.setText(String.valueOf(color));
            }
        });
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

    public String getNameOfDriver(){
        String drivername_from_intent = this.getIntent().getStringExtra("driver from picker");

        if (drivername_from_intent != null){
            return drivername_from_intent;
        }

        return null;
    }

    public void setWarning(String message){
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