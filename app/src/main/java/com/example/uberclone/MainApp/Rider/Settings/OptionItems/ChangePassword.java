package com.example.uberclone.MainApp.Rider.Settings.OptionItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberclone.MainApp.Rider.Settings.SettingsMenu;
import com.example.uberclone.R;

public class ChangePassword extends AppCompatActivity {

    private final String nameOfRider = SettingsMenu.getRiderUsername();

    private EditText currentPassword;
    private EditText confirmedCurrentPassword;

    private TextView warning;

    private Button changePasswordButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        this.getActionBar().hide();

        currentPassword = (EditText) findViewById(R.id.currentPassword);
        confirmedCurrentPassword = (EditText) findViewById(R.id.confirmCurrentPassword);

        warning = (TextView) findViewById(R.id.warningChangePassword);

        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);

    }

    public void changePassword(View view){
        String currentPasswordText = String.valueOf(currentPassword.getText());
        String confirmedCurrentPasswordText = String.valueOf(confirmedCurrentPassword.getText());

        if (areSamePasswords(currentPasswordText,confirmedCurrentPasswordText)){

        }
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