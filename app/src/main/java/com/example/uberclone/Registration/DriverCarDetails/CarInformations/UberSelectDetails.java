package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
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
import com.example.uberclone.Modules.Color.Colors;
import com.example.uberclone.R;

public class UberSelectDetails extends AppCompatActivity {

    private String nameOfDriver;

    private EditText carname;
    private EditText numOfDoors;
    private EditText numOfPassangers;
    private EditText price;

    private Spinner interiorpicker;
    private ColorAdapter colorAdapter;

    private TextView warning;

    private Button addCar;

    private String[] colors;

    private String pickedcolor;

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

        colors = getColors();
        pickedcolor = "";

        interiorpicker = (Spinner) findViewById(R.id.interiorpicker_UberSelect);
        colorAdapter = new ColorAdapter(UberSelectDetails.this,colors);
        interiorpicker.setAdapter(colorAdapter);

        interiorpicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        pickedcolor = "Black";
                        break;
                    case 1:
                        pickedcolor = "White";
                        break;
                    case 2:
                        pickedcolor = "Yellow";
                        break;
                    case 3:
                        pickedcolor = "Green";
                        break;
                    case 4:
                        pickedcolor = "Red";
                        break;
                    case 5:
                        pickedcolor = "Blue";
                        break;
                    case 6:
                        pickedcolor = "Orange";
                        break;
                    case 7:
                        pickedcolor = "Violet";
                        break;
                    case 8:
                        pickedcolor = "Brown";
                        break;
                    case 9:
                        pickedcolor = "Grey";
                        break;

                    case 10:
                        pickedcolor = "Pink";
                        break;

                    default:
                        pickedcolor = "";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String[] getColors(){
        String[] cols = new String[11];

        int i = 0;

        for (Colors color : Colors.values()){
            cols[i] = color.getColorname();
            i++;
        }

        return cols;
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