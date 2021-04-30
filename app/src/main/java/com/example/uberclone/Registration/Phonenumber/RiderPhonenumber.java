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
import android.widget.Toast;

import com.example.uberclone.Extras.Adapters.CoutryAdapters.CountriesAdapterRider;
import com.example.uberclone.Modules.Country;
import com.example.uberclone.Modules.Phonenumber;
import com.example.uberclone.R;
import com.example.uberclone.Registration.CardActivity.CardEntery;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderPhonenumber extends AppCompatActivity implements com.example.uberclone.Registration.Phonenumber.Phonenumber {

    private String nameOfRider;

    private Spinner countries;

    private String[] names;
    private int[] images;

    private CountriesAdapterRider countriesAdapterRider;

    private TextView warning;

    private TextView numberprefix;
    private EditText numbersecondcolumn;

    private Button addNumber;
    private Button addLater;

    private Country country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_phonenumber2);

        this.getSupportActionBar().hide();

        nameOfRider = getNameOfUser();

        countries = (Spinner) findViewById(R.id.countriesDriver);

        names = getNames();
        images = getImages();

        countriesAdapterRider = new CountriesAdapterRider(RiderPhonenumber.this, names, images);
        countries.setAdapter(countriesAdapterRider);

        warning = (TextView) findViewById(R.id.phonenumberwarningrider);

        numberprefix = (TextView) findViewById(R.id.ridernumberprefix);
        numberprefix.setText("+1");

        numbersecondcolumn = (EditText) findViewById(R.id.riderphonenumbersecond);

        addNumber = (Button) findViewById(R.id.addNumberButtonRider);
        addLater = (Button) findViewById(R.id.addNumberLaterRider);

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

    public void addNumberToDatabase(View view){

        String prefixFromField = String.valueOf(numberprefix.getText());
        String phonenumberFromField = String.valueOf(numbersecondcolumn.getText());


        if (!prefixFromField.equals("") && !phonenumberFromField.equals("") && country != null){
            if (prefixFromField.length() >= 2 && phonenumberFromField.length() >= 8){
            Toast.makeText(RiderPhonenumber.this,prefixFromField+" "+phonenumberFromField+" "+country, Toast.LENGTH_LONG).show();

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference root = firebaseDatabase.getReference();

            root.child("User").child("Rider").child(nameOfRider).child("Phonenumber").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (!snapshot.exists()){

                     String phonenumberToMerge = mergePhonenumber(prefixFromField,phonenumberFromField);
                     Phonenumber riderphonenumber = new Phonenumber(phonenumberToMerge,country);

                     root.child("User").child("Rider").child(nameOfRider).child("Phonenumber").setValue(riderphonenumber).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                          Log.i("Phonenumberstatus: ","ADDED SUCCESEFULL");

                          Intent toCardEntery = new Intent(RiderPhonenumber.this, CardEntery.class);
                          toCardEntery.putExtra("ridername for card",nameOfRider);
                          startActivity(toCardEntery);
                         }
                     });
                 }
                 else{
                     Log.i("Phonenumber exist",String.valueOf(snapshot.getValue()));
                 }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Database error",error.getMessage());
                }
            });
            }
            else {
                warning.setText("Phone musst have 10 digits including prefix");
                restartFields();
            }
        }
        else{
            warning.setText("Problem with seletion");
            restartFields();
        }
    }

    public String mergePhonenumber(String prefix, String phonenumber){
        StringBuilder builder = new StringBuilder();

        builder.append(prefix).append("-").append(phonenumber);

        return builder.toString();
    }


    public String getNameOfUser() {
        String nameFromRegistration = this.getIntent().getStringExtra("ridername for phonenumber");
        Log.i("Transfer rider name: ", nameFromRegistration);

        if (nameFromRegistration != null) {
            return nameFromRegistration;
        }

        return null;
    }

    public String[] getNames() {
        String[] countiynames = new String[6];

        countiynames[0] = Country.USA.getCoutryname();
        countiynames[1] = Country.UK.getCoutryname();
        countiynames[2] = Country.GER.getCoutryname();
        countiynames[3] = Country.AUT.getCoutryname();
        countiynames[4] = Country.ESP.getCoutryname();
        countiynames[5] = Country.ITA.getCoutryname();

        return countiynames;
    }

    public int[] getImages() {
        int[] countryimages = new int[6];

        countryimages[0] = R.drawable.usa;
        countryimages[1] = R.drawable.uk;
        countryimages[2] = R.drawable.germany;
        countryimages[3] = R.drawable.aut;
        countryimages[4] = R.drawable.esp;
        countryimages[5] = R.drawable.ita;

        return countryimages;
    }


    public boolean fieldsEmpty() {
        if (!TextUtils.isEmpty(numberprefix.getText())  && !TextUtils.isEmpty(numbersecondcolumn.getText())) {
            return false;
        }
        return true;
    }

    public void restartFields() {
        if (!TextUtils.isEmpty(numbersecondcolumn.getText())) {
            numbersecondcolumn.setText("");
        }
    }

    public void addNumberLater(View view){
        Intent toCardEntity = new Intent(RiderPhonenumber.this,CardEntery.class);
        toCardEntity.putExtra("ridername for card",nameOfRider);
        startActivity(toCardEntity);
    }

}