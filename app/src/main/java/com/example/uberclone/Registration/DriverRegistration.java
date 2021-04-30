package com.example.uberclone.Registration;

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
import android.widget.TextView;

import com.example.uberclone.Login.DriverLogin;
import com.example.uberclone.Modules.Participants.Driver;
import com.example.uberclone.Modules.Phonenumber;
import com.example.uberclone.R;
import com.example.uberclone.Registration.Phonenumber.DriverPhonenumber;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverRegistration extends AppCompatActivity {

    private EditText drivername;
    private EditText driverpassword;
    private EditText driverconfirmpassword;
    private EditText driverage;
    private EditText driveremail;

    private TextView warnings;

    private Button driverregistration;
    private Button driveralreadAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        getSupportActionBar().hide();

        drivername = (EditText) findViewById(R.id.drivernameforregistration);
        driverpassword = (EditText) findViewById(R.id.driverpasswordforregisttration);
        driverconfirmpassword = (EditText) findViewById(R.id.driverconfirmpassword);
        driverage = (EditText) findViewById(R.id.driverage);
        driveremail = (EditText) findViewById(R.id.driveremail);

        warnings = (TextView) findViewById(R.id.driverwarning);

        driverregistration = (Button) findViewById(R.id.registrationDriver);
        driveralreadAcc = (Button) findViewById(R.id.alreadyAccountDriver);

        restartFields();

    }

    public void registerDriver(View view){

        if (!fieldsEmpty()) {
            if (isValidEmail()) {
                if (isValidePassword()) {
                    if (isEqualsConfirmationPassword()) {

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference root = firebaseDatabase.getReference();

                        String nameOfDriver = String.valueOf(drivername.getText());
                        String passwordOfDriver = String.valueOf(driverpassword.getText());

                        root.child("User").child("Driver").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {

                                    String ageOfDriver = String.valueOf(driverage.getText());
                                    String emailOfDriver = String.valueOf(driveremail.getText());
                                    Phonenumber phonenumberOfUser = null;

                                    Driver driver = new Driver(nameOfDriver,passwordOfDriver,emailOfDriver,Integer.parseInt(ageOfDriver),phonenumberOfUser);

                                    root.child("User").child("Driver").child(nameOfDriver).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("Driver registration:","SUCCESEFULL");

                                            Intent toPhonenumber = new Intent(DriverRegistration.this,DriverPhonenumber.class);
                                            toPhonenumber.putExtra("drivername for phonenumber",nameOfDriver);
                                            startActivity(toPhonenumber);

                                        }
                                    });


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverRegistration.this)
                                            .setTitle("User already registred")
                                            .setMessage("You already have account.\nDo you want to login yourself?")
                                            .setNegativeButton("No", null)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent toLogin = new Intent(DriverRegistration.this, DriverLogin.class);

                                                    toLogin.putExtra("drivername", nameOfDriver);
                                                    toLogin.putExtra("driverpassword", passwordOfDriver);

                                                    startActivity(toLogin);
                                                }
                                            });

                                    builder.create().show();
                                }
                            }

                            ;

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("DATABASE ERROR", error.getMessage());
                            }
                        });
                    } else {
                        warnings.setText("Wrong conformation password");
                        restartFields();
                    }
                }
                else{
                        warnings.setText("Password musst have upper/lower cases and digits");
                        restartFields();
                    }
                }else {
                    warnings.setText("Email is not valid");
                    restartFields();
                }
            } else {
                warnings.setText("Some fields are empty");
                restartFields();
            }
        }

    public boolean fieldsEmpty(){

        if (TextUtils.isEmpty(drivername.getText()) || TextUtils.isEmpty(driverpassword.getText()) || TextUtils.isEmpty(driverconfirmpassword.getText()) || TextUtils.isEmpty(driverage.getText())){
            return true;
        }

        return false;
    }

    public boolean isValidEmail(){
        String emailToValidate = String.valueOf(driveremail.getText());

        if (Character.isLowerCase(emailToValidate.charAt(0)) && emailToValidate.charAt(0) != '@'){
            if (emailToValidate.contains("@")){
                return true;
            }
        }
        return false;
    }

    public boolean isValidePassword(){

        String passwordToValidate = String.valueOf(driverpassword.getText());

        int numUpperCase = 0;
        int numLowwerCase = 0;
        int numDigits = 0;

        for (int i = 0; i < passwordToValidate.length(); i++){

            if (Character.isUpperCase(passwordToValidate.charAt(i))){
                numUpperCase++;
            }
            if (Character.isLowerCase(passwordToValidate.charAt(i))){
                numLowwerCase++;
            }
            if (Character.isDigit(passwordToValidate.charAt(i))){
                numDigits++;
            }}

            if (numUpperCase == 0 || numLowwerCase == 0 || numDigits == 0){
                return false;
            }

            return true;
    }

    public boolean isEqualsConfirmationPassword(){

        String passwordTocheck = String.valueOf(driverpassword.getText());
        String confirmationpasswordToCheck = String.valueOf(driverconfirmpassword.getText());

        if (passwordTocheck.equals(confirmationpasswordToCheck)){
            return true;
        }

        return false;
    }

    public void restartFields(){
        if (TextUtils.isEmpty(drivername.getText())){
            drivername.setText("");
        }
        if (TextUtils.isEmpty(driverpassword.getText())){
            driverpassword.setText("");
        }
        if (TextUtils.isEmpty(driverconfirmpassword.getText())){
            driverconfirmpassword.setText("");
        }
        if (TextUtils.isEmpty(driverage.getText())){
            driverage.setText("");
        }
    }

    public void driverHasAlreadyAccount(View view){
        Intent intent = new Intent(DriverRegistration.this,DriverLogin.class);
        startActivity(intent);
    }
}