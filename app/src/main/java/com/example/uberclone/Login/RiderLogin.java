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

import com.example.uberclone.Login.ForgottenPassword.RiderForgottenPassword;
import com.example.uberclone.MainApp.Rider.RiderMainContent;
import com.example.uberclone.R;
import com.example.uberclone.Registration.RiderRegistration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderLogin extends AppCompatActivity {

    private TextView warning;

    private EditText name;
    private EditText password;

    private Button login;
    private Button toRegistration;
    private Button forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_login);

        getSupportActionBar().hide();

        warning = (TextView) findViewById(R.id.registrationwarning);
        password = (EditText) findViewById(R.id.riderpasswordforregisttration);

        name = (EditText) findViewById(R.id.ridernameforregistration);

        login = (Button) findViewById(R.id.registration);
        toRegistration = (Button) findViewById(R.id.notAccountRider);
        forgotpassword = (Button) findViewById(R.id.forgotpassword);

        restartFields();

        getDataFromRegistration();
    }

    public void loginrider(View view){
        if (notEmptyFields()){

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference root = firebaseDatabase.getReference();

            String nameOfUser = String.valueOf(name.getText());
            String passwordOfUser= String.valueOf(password.getText());

            root.child("User").child("Rider").child(nameOfUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String passwordInDatabase = String.valueOf(snapshot.child("password").getValue());

                        if (passwordOfUser.equals(passwordInDatabase)){
                            Intent toMainContent = new Intent(RiderLogin.this, RiderMainContent.class);
                            toMainContent.putExtra("ridername from login",nameOfUser);
                            startActivity(toMainContent);
                        }
                        else{
                            warning.setText("Wrong password! Please try again");
                            restartFields();
                        }
                    }
                    else{
                        warning.setText("Wrong username or user not registred");
                        restartFields();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        else{
            warning.setText("Some of the fields are empty");
            restartFields();
        }
    }

    public void forgottenPasswordProcess(View view){

        if (!TextUtils.isEmpty(String.valueOf(name.getText()))){

            String ridername = String.valueOf(name.getText());

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference root = firebaseDatabase.getReference();

            root.child("User").child("Rider").child(ridername).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Intent toForgottenPassword = new Intent(RiderLogin.this,RiderForgottenPassword.class);
                        toForgottenPassword.putExtra("ridernameToForgottenPassword",ridername);
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

    public void regiterNewUser(View view){
        Intent intent = new Intent(RiderLogin.this, RiderRegistration.class);
        startActivity(intent);
    }

    public void getDataFromRegistration(){
        String nameFromRegistration = this.getIntent().getStringExtra("registredusername");
        String passwordFromRegistration = this.getIntent().getStringExtra("registredpassword");

        if (nameFromRegistration != null && passwordFromRegistration != null){
            name.setText(nameFromRegistration);
            password.setText(passwordFromRegistration);
        }
    }

    public boolean notEmptyFields(){
        if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(password.getText())){
            return true;
        }

        return false;
    }

    public void restartFields(){
        if (!TextUtils.isEmpty(name.getText())){
            name.setText("");
        }
        if (!TextUtils.isEmpty(password.getText())){
            password.setText("");
        }
    }

}