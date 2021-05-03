package com.example.uberclone.Registration.Phonenumber;

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

import com.example.uberclone.Extras.Adapters.CoutryAdapters.CounrtiesAdapterDriver;
import com.example.uberclone.Modules.Country;
import com.example.uberclone.R;
import com.example.uberclone.Registration.DriverCarDetails.CarTypePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverPhonenumber extends AppCompatActivity implements Phonenumber{

    private String nameOfDriver;

    private Spinner countries;
    private CounrtiesAdapterDriver counrtiesAdapterDriver;

    private TextView warning;

    private TextView numberprefix;
    private EditText phonenumber;

    private Button addnumber;
    private Button addNumberlater;

    private String[] names;
    private int[] images;

    private Country country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_phonenumber2);

        this.getSupportActionBar().hide();

        nameOfDriver = getNameOfUser();

        names = getNames();
        images = getImages();

        countries = (Spinner) findViewById(R.id.countries);
        counrtiesAdapterDriver = new CounrtiesAdapterDriver(DriverPhonenumber.this,names,images);
        countries.setAdapter(counrtiesAdapterDriver);

        warning = (TextView) findViewById(R.id.phonenumberwarningdiver);

        numberprefix = (TextView) findViewById(R.id.drivernumberprefix);
        phonenumber = (EditText) findViewById(R.id.driverphonenumbersecond);

        addnumber = (Button) findViewById(R.id.addNumberButtonDriver);
        addNumberlater = (Button) findViewById(R.id.addNumberLaterDriver);


        numberprefix.setText("+1");
        country = Country.USA;

        countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        numberprefix.setText("+1");
                        country = Country.USA;
                        break;

                    case 1:
                        numberprefix.setText("+44");
                        country = Country.UK;
                        break;

                    case 2:
                        numberprefix.setText("+49");
                        country = Country.GER;
                        break;

                    case 3:
                        numberprefix.setText("+43");
                        country = Country.AUT;
                        break;

                    case 4:
                        numberprefix.setText("+34");
                        country = Country.ESP;
                        break;

                    case 5:
                        numberprefix.setText("+39");
                        country = Country.ITA;
                        break;

                    default:
                        numberprefix.setText("");
                        country = null;
                        break;
                }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void addNumberToDatabase(View view) {
        if (!fieldsEmpty()){
            if (!String.valueOf(numberprefix.getText()).equals("") && country != null){
                if (String.valueOf(numberprefix.getText()).length() >= 2 && String.valueOf(phonenumber.getText()).length() >= 8){

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference root = firebaseDatabase.getReference();

                    root.child("User").child("Driver").child(nameOfDriver).child("Phonenumber").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){

                                String prefixToDatabase = String.valueOf(numberprefix.getText());
                                String phonenumberToDatabase = String.valueOf(phonenumber.getText());

                                String driverphonenumber = mergePhonenumber(prefixToDatabase,phonenumberToDatabase);

                                com.example.uberclone.Modules.Phonenumber driverphonenumberForDatabase = new com.example.uberclone.Modules.Phonenumber(driverphonenumber,country);

                                root.child("User").child("Driver").child(nameOfDriver).child("Phonenumber").setValue(driverphonenumberForDatabase).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent toCarType = new Intent(DriverPhonenumber.this, CarTypePicker.class);
                                        toCarType.putExtra("drivername from phonenumber",nameOfDriver);
                                        startActivity(toCarType);
                                    }
                                });

                            }
                            else{
                                Log.i("Phonenumberproblem: ","Phonenumber child exists");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else{
                    putWarning("Phone musst have 10 digits including prefix");
                }
            }
            else{
                putWarning("Problem with selection");
            }

        }
        else{
            putWarning("Please, add your phonenumber");
        }

    }

    @Override
    public String mergePhonenumber(String prefix, String phonenumber) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(prefix).append("-").append(phonenumber);

        return stringBuilder.toString();
    }

    @Override
    public String getNameOfUser() {
        if (this.getIntent().getStringExtra("drivername for phonenumber") != null){
            return this.getIntent().getStringExtra("drivername for phonenumber");
        }

        return null;
    }

    @Override
    public String[] getNames() {
        String[] namesFromCounties = new String[6];

        namesFromCounties[0] = Country.USA.getCoutryname();
        namesFromCounties[1] = Country.UK.getCoutryname();
        namesFromCounties[2] = Country.GER.getCoutryname();
        namesFromCounties[3] = Country.AUT.getCoutryname();
        namesFromCounties[4] = Country.ESP.getCoutryname();
        namesFromCounties[5] = Country.ITA.getCoutryname();

        return namesFromCounties;
    }

    @Override
    public int[] getImages() {
        int[] imagesForCountires = new int[6];

        imagesForCountires[0] = R.drawable.usa;
        imagesForCountires[1] = R.drawable.uk;
        imagesForCountires[2] = R.drawable.germany;
        imagesForCountires[3] = R.drawable.aut;
        imagesForCountires[4] = R.drawable.esp;
        imagesForCountires[5] = R.drawable.ita;

        return imagesForCountires;
    }

    @Override
    public boolean fieldsEmpty() {

        if (!TextUtils.isEmpty(numberprefix.getText()) && !TextUtils.isEmpty(phonenumber.getText())){
            return false;
        }

        return true;
    }

    @Override
    public void restartFields() {
        if (!TextUtils.isEmpty(phonenumber.getText())){
            phonenumber.setText("");
        }
    }

    @Override
    public void addNumberLater(View view) {

        Intent toCarType = new Intent(DriverPhonenumber.this, CarTypePicker.class);
        toCarType.putExtra("drivername from phonenumber",nameOfDriver);
        startActivity(toCarType);

    }

    public void putWarning(String message){
        warning.setText(message);
        restartFields();
    }
}