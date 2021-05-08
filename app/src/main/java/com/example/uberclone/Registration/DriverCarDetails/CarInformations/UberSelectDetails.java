package com.example.uberclone.Registration.DriverCarDetails.CarInformations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

        interiorpicker = (Spinner) findViewById(R.id.interiorpicker_UberSelect);
        colorAdapter = new ColorAdapter(UberSelectDetails.this,colors);
    }

    public String[] getColors(){
        String[] cols = new String[11];

        int i = 0;

        for (Colors colors)
    }



    public String getNameOfDriver() {
        if (this.getIntent().getStringExtra("driver from picker") != null) {
            return this.getIntent().getStringExtra("driver from picker");
        }
        return null;
    }
}