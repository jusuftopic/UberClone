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

import com.example.uberclone.Login.RiderLogin;
import com.example.uberclone.Modules.PaymentMethode;
import com.example.uberclone.Modules.Participants.Rider;
import com.example.uberclone.Modules.Phonenumber;
import com.example.uberclone.R;
import com.example.uberclone.Registration.Phonenumber.RiderPhonenumber;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderRegistration extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private EditText confirmpassword;
    private EditText email;
    private EditText age;

    private TextView warning;

    private Button registration;
    private Button alreadyMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_registration);

        getSupportActionBar().hide();

        this.getSupportActionBar().hide();


        name = (EditText) findViewById(R.id.ridernameforregistration);
        password = (EditText) findViewById(R.id.riderpasswordforregisttration);
        confirmpassword = (EditText) findViewById(R.id.riderconfirmpassword);
        email = (EditText) findViewById(R.id.email);
        age = (EditText) findViewById(R.id.age);

        warning = (TextView) findViewById(R.id.registrationwarning);

        registration = (Button) findViewById(R.id.registration);
        alreadyMember = (Button) findViewById(R.id.alreadyAccount);

        restartFields();
    }



    public void alreadyMemberToLogin(View view){
        Intent toLogin = new Intent(RiderRegistration.this, RiderLogin.class);
        startActivity(toLogin);
    }

    public void riderregistration(View view){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        String user = String.valueOf(name.getText());

        if (!fieldsEmpty()){
            if (passwordValid()){
                if (emailValid()){
                    if (isRightConfirmPassword()) {

                        root.child("User").child("Rider").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(RiderRegistration.this)
                                            .setTitle("User already exists")
                                            .setMessage("User has already account.\nDo you want to login yourself")
                                            .setIcon(android.R.drawable.stat_sys_warning)
                                            .setNegativeButton("No",null)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent toLogin = new Intent(RiderRegistration.this, RiderLogin.class);

                                                    toLogin.putExtra("registredusername",String.valueOf(name.getText()));
                                                    toLogin.putExtra("registredpassword",String.valueOf(password.getText()));

                                                    startActivity(toLogin);
                                                }
                                            });

                                    builder.create().show();

                                } else {
                                    String riderpassword = String.valueOf(password.getText());
                                    String rideremail = String.valueOf(email.getText());
                                    String riderage = String.valueOf(age.getText());
                                    Phonenumber phonenumberOfRider = null;
                                    PaymentMethode paymentMethode = null;

                                    Rider rider = new Rider(user,riderpassword,rideremail,Integer.parseInt(riderage),phonenumberOfRider,paymentMethode);

                                    root.child("User").child("Rider").child(user).setValue(rider).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("REGISTRATION STATUS","User added");

                                            Intent toPhonenumber = new Intent(RiderRegistration.this, RiderPhonenumber.class);
                                            toPhonenumber.putExtra("ridername for phonenumber",user);
                                            startActivity(toPhonenumber);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("DATABASE ERROR", error.getMessage());

                            }
                        });

                    }
                    else{
                        warning.setText("Incorrect confirm password");
                        restartFields();
                    }
                }
                else{
                    warning.setText("Invalid form of email");
                    restartFields();
                }
            }
            else{
                warning.setText("Password musst have upper/lower cases and digits ");
                restartFields();
            }

        }
        else{
            warning.setText("Some fields are empty.");
            restartFields();
        }
    }


    public boolean fieldsEmpty(){
        if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(password.getText()) || TextUtils.isEmpty(confirmpassword.getText()) || TextUtils.isEmpty(email.getText()) && !TextUtils.isEmpty(age.getText())){
            return true;
        }

        return false;
    }

    public boolean passwordValid(){
        String passwordToValidate = String.valueOf(password.getText());

        int numOfUpperCase = 0;
        int numOfLowerCase=0;
        int numOfDigits = 0;

        for (int i = 0; i < passwordToValidate.length(); i++){
            if (Character.isUpperCase(passwordToValidate.charAt(i))){
                numOfUpperCase++;
            }
            if (Character.isLowerCase(passwordToValidate.charAt(i))){
                numOfLowerCase++;
            }
            if (Character.isDigit(passwordToValidate.charAt(i))){
                numOfDigits++;
            }
        }

        if (numOfUpperCase > 0 && numOfLowerCase > 0 && numOfDigits > 0){
            return true;
        }

        return false;
    }

    public boolean emailValid(){
        String emailToValidate = String.valueOf(email.getText());

        if (Character.isLowerCase(emailToValidate.charAt(0)) && emailToValidate.charAt(0) != '@'){
            if (emailToValidate.contains("@")){
                return true;
            }
        }

        return false;
    }

    public boolean isRightConfirmPassword(){

        String riderpassword = String.valueOf(password.getText());
        String riderconfirmpassword = String.valueOf(confirmpassword.getText());

        if (riderconfirmpassword.equals(riderpassword)){
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
        if (!TextUtils.isEmpty(confirmpassword.getText())){
            confirmpassword.setText("");
        }
        if (!TextUtils.isEmpty(email.getText())){
            email.setText("");
        }
        if (!TextUtils.isEmpty(age.getText())){
            age.setText("");
        }
    }

}