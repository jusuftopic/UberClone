package com.example.uberclone.Login;

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

import com.example.uberclone.Login.ForgottenPassword.DriverForgottenPassword;
import com.example.uberclone.MainApp.Driver.DriverMainContent;
import com.example.uberclone.R;
import com.example.uberclone.Registration.DriverRegistration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverLogin extends AppCompatActivity {

    private EditText drivername;
    private EditText driverpassword;

    private TextView warning;

    private Button driverlogin;
    private Button drivernotaccount;
    private Button driverforgottpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        getSupportActionBar().hide();

        drivername = (EditText) findViewById(R.id.drivernameforlogin);
        driverpassword = (EditText) findViewById(R.id.driverpasswordforlogin);

        warning = (TextView) findViewById(R.id.driverloginwarning);

        driverlogin = (Button) findViewById(R.id.driverlogin);
        drivernotaccount = (Button) findViewById(R.id.notAccountDriver);
        driverforgottpassword = (Button) findViewById(R.id.forgotpasswordDriver);

        restartFields();

        getDataFromRegistration();

    }

    public void loginDriver(View view){
        if (!fieldsEmpty()){

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference root = firebaseDatabase.getReference();

            String nameOfDriver = String.valueOf(drivername.getText());
            String passwordOfUser = String.valueOf(driverpassword.getText());

            root.child("User").child("Driver").child(nameOfDriver).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        String passwordInDatabase = String.valueOf(snapshot.child("password").getValue());

                        if (passwordInDatabase.equals(passwordOfUser)){
                            Intent toMainContent = new Intent(DriverLogin.this, DriverMainContent.class);
                            toMainContent.putExtra("drivername from login",nameOfDriver);
                            startActivity(toMainContent);
                        }
                        else{
                            warning.setText("Wrong password!");
                            restartFields();
                        }

                    }
                    else{
                        warning.setText("Wrong username. Please try again");
                        restartFields();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Database error",error.getMessage());
                }
            });
        }

    }

    public boolean fieldsEmpty(){

        if (!TextUtils.isEmpty(drivername.getText()) && !TextUtils.isEmpty(driverpassword.getText())){
            return false;
        }
        return true;

    }

    public void setForgottenPassword(View view){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        if (!TextUtils.isEmpty(drivername.getText())){
            root.child("User").child("Driver").child(String.valueOf(drivername.getText())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Intent toForgottenPassword = new Intent(DriverLogin.this,DriverForgottenPassword.class);
                        toForgottenPassword.putExtra("drivernameToForgottenPassword",String.valueOf(drivername.getText()));
                        startActivity(toForgottenPassword);
                    }
                    else{
                        warning.setText("Please type your correct username");
                        restartFields();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                 Log.e("Database error",error.getMessage());
                }
            });
        }
        else{
            warning.setText("Please type your username to continue the process");
        }
    }

    public void goToRegistration(View view){
        Intent toRegistration = new Intent(DriverLogin.this, DriverRegistration.class);
        startActivity(toRegistration);
    }

    public void getDataFromRegistration(){
        String nameFromRegistration = this.getIntent().getStringExtra("drivername");
        String passwordFromRegistration = this.getIntent().getStringExtra("driverpassword");

        if (nameFromRegistration != null && passwordFromRegistration != null){
            drivername.setText(nameFromRegistration);
            driverpassword.setText(passwordFromRegistration);
        }
    }

    public void restartFields(){
        if (!TextUtils.isEmpty(drivername.getText())){
            drivername.setText("");
        }
        if (!TextUtils.isEmpty(driverpassword.getText())){
            driverpassword.setText("");
        }
    }
}