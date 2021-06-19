package com.example.uberclone.MainApp.Rider.Settings.OptionItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberclone.MainApp.Rider.Settings.SettingsMenu;
import com.example.uberclone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {

    private static final String PASSWORD_FORM_MESSAGE = "Password need to contain upper/lower casses, numbers and characters";

    private final String nameOfRider = SettingsMenu.getRiderUsername();

    private EditText currentPassword;
    private EditText confirmedCurrentPassword;

    private TextView warning;

    private Button changePasswordButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = (EditText) findViewById(R.id.currentPassword);
        confirmedCurrentPassword = (EditText) findViewById(R.id.confirmCurrentPassword);

        warning = (TextView) findViewById(R.id.warningChangePassword);

        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);

    }

    public void changePassword(View view){
        String currentPasswordText = String.valueOf(currentPassword.getText());
        String confirmedCurrentPasswordText = String.valueOf(confirmedCurrentPassword.getText());

        if (areSamePasswords(currentPasswordText,confirmedCurrentPasswordText)){
            if (isApprovedPasswordStyle(currentPasswordText))
            checkDatabaseAndChange(currentPasswordText);
        }
    }

    public boolean isApprovedPasswordStyle(String approvedPassword){
        int counterUpperCase = 0;
        int counterLowerCase = 0;
        int counterDigits = 0;
        int counterCharacters = 0;

        for (int i = 0; i < approvedPassword.length(); i++){
            char currentCharacter = approvedPassword.charAt(i);
            if (Character.isUpperCase(currentCharacter)){
                counterUpperCase++;
            }
            if (Character.isLowerCase(currentCharacter)){
                counterLowerCase++;
            }
            if (Character.isDigit(currentCharacter)){
                counterDigits++;
            }
            if ((int) currentCharacter >= 33 && (int) currentCharacter<= 46 ||
                    (int) currentCharacter >= 58 && (int) currentCharacter<=64
            || (int) currentCharacter >= 91 && (int)currentCharacter<=96
            || (int) currentCharacter >= 123 && (int) currentCharacter <=126){
                counterCharacters++;
            }

        }

        if (counterUpperCase > 0 && counterLowerCase > 0 && counterDigits > 0 && counterCharacters > 0){
            return true;
        }

        makeWarning(PASSWORD_FORM_MESSAGE);
        return false;
    }

    public void checkDatabaseAndChange(String approvedPassword){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference root = firebaseDatabase.getReference();

        root.child("User").child("Rider").child(nameOfRider).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (String.valueOf(snapshot.child("password").getValue()).equals(approvedPassword)){
                        makeWarning("Please, pick a new password");
                    }
                    else {
                        root.child("User").child("Rider").child(nameOfRider).child("password").setValue(approvedPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.i("Succesefull","Succesefull changed password");
                                        makeWarning("Succesefull changed password");
                                    }
                                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Failed",e.getMessage());
                                makeWarning("Problems  changing a password");
                            }
                        });
                    }
                }
                else{
                    Log.e("Fail","Failed to find user in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database error",error.getMessage());
            }
        });
    }

    public boolean areSamePasswords(String currentPassword, String confirmCurrentPassword){
        if (currentPassword.equals(confirmCurrentPassword)){
            return true;
        }
        else{
            makeWarning("Wrong confirmation password");
            restartFields();
            return false;
        }
    }

    public void makeWarning(String message){
        warning.setText(message);
    }

    public void restartFields(){
        this.currentPassword.setText("");
        this.confirmedCurrentPassword.setText("");
    }
}