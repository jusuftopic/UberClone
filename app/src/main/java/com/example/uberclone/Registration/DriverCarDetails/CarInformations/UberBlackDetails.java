package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.uberclone.Modules.Car.CarMarks.CarMarks;
import com.example.uberclone.Modules.Color.Colors;
import com.example.uberclone.R;

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

    private CheckBox airportPermit;
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

        airportPermit = (CheckBox) findViewById(R.id.airport_permit);
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
                    String uberBlack_markname;
                    String uberBlack_numOfDoors;
                    String uberBlack_numOfPassangers;
                    String uberBlack_price;
                    airport_permit = hasAirportPermit();

                }
                else{
                    setWarning();
                }

            }
            else{
                setWarning();
            }


        }
        else {
            setWarning();
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