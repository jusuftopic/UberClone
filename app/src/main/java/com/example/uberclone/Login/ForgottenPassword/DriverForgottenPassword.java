package com.example.uberclone.Login.ForgottenPassword;

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
import android.widget.Toast;

import com.example.uberclone.Login.DriverLogin;
import com.example.uberclone.Models.Participants.Driver;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverForgottenPassword extends AppCompatActivity {

    String nameOfUser;

    private TextView welcometext;
    private TextView warning;

    private EditText newpassword;
    private EditText confirmationpassword;

    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_forgotten_password);

        this.getSupportActionBar().hide();

        welcometext = (TextView) findViewById(R.id.driverwelcometext);

        warning = (TextView) findViewById(R.id.driverforgottenpasswordwarning);

        newpassword = (EditText) findViewById(R.id.drivernewpassword);
        confirmationpassword = (EditText) findViewById(R.id.confirmnewdriverpassword);

        submit = (Button) findViewById(R.id.submitrider);

        nameOfUser = getNameOfUser();

        restartFields();

        setWelcomeText();
    }

    public String getNameOfUser(){

        if (this.getIntent().getStringExtra("drivernameToForgottenPassword") != null){
            return this.getIntent().getStringExtra("drivernameToForgottenPassword");
        }

        return null;
    }

    public void setWelcomeText() {
        welcometext.setText("Hallo, "+nameOfUser);
    }

    public void submitNewPassword(View view) {

        if (!fieldsEmpty()) {
            if (isValidPassword()) {
                if (isValideConfirmPassword()) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference root = firebaseDatabase.getReference();

                    String passwordOfDriver = String.valueOf(newpassword.getText());

                    root.child("User").child("Driver").child(nameOfUser).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                if (String.valueOf(snapshot.getValue()).equals(passwordOfDriver)){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverForgottenPassword.this)
                                            .setTitle("Already your password!")
                                            .setMessage("This is your old password.\nDo you want to change it?")
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent toLogin = new Intent(DriverForgottenPassword.this, Driver.class);
                                                    startActivity(toLogin);
                                                }
                                            })
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                  restartFields();
                                                }
                                            });

                                    builder.create().show();
                                }
                                else{
                                    root.child("User").child("Driver").child(nameOfUser).child("password").setValue(passwordOfDriver).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(DriverForgottenPassword.this,"Password succesefull changed",Toast.LENGTH_LONG).show();

                                            Intent toLogin = new Intent(DriverForgottenPassword.this, DriverLogin.class);
                                            startActivity(toLogin);
                                        }
                                    });
                                }
                            }
                            else{
                                Log.i("Database error","Failed to find atribute password");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Database error",error.getMessage());

                        }
                    });

                } else {
                    warning.setText("Wrong confirm password");
                    restartFields();
                }
            } else {
                warning.setText("Password musst have upper/lower cases and digits");
                restartFields();
            }
        } else {
            warning.setText("Some fields are empty");
            restartFields();
        }
    }

    public boolean fieldsEmpty() {
        if (!TextUtils.isEmpty(newpassword.getText()) && !TextUtils.isEmpty(confirmationpassword.getText())) {
            return false;
        }
        return true;
    }

    public boolean isValidPassword() {

        String passwordToValidate = String.valueOf(newpassword.getText());

        int numUpperCase = 0;
        int numLowerCase = 0;
        int numDigits = 0;

        for (int i = 0; i < passwordToValidate.length(); i++) {
            if (Character.isUpperCase(passwordToValidate.charAt(i))) {
                numUpperCase++;
            }
            if (Character.isLowerCase(passwordToValidate.charAt(i))) {
                numLowerCase++;
            }
            if (Character.isDigit(passwordToValidate.charAt(i))) {
                numDigits++;
            }
        }

        if (numUpperCase > 0 && numLowerCase > 0 && numDigits > 0) {
            return true;
        }
        return false;
    }

    public boolean isValideConfirmPassword() {

        String passwordToValidate = String.valueOf(newpassword.getText());
        String confirmpasswordToValidate = String.valueOf(confirmationpassword.getText());

        if (passwordToValidate.equals(confirmpasswordToValidate)) {
            return true;
        }

        return false;
    }

    public void restartFields() {
        if (!TextUtils.isEmpty(newpassword.getText())) {
            newpassword.setText("");
        }
        if (!TextUtils.isEmpty(confirmationpassword.getText())) {
            confirmationpassword.setText("");
        }
    }

}