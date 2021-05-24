package com.example.uberclone.MainApp.Driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uberclone.Modules.Requests.DriverLocation;
import com.example.uberclone.R;

public class RequesterPopUp extends AppCompatActivity {

    private ImageView avatarView;
    private TextView usernameField;
    private TextView currentLocationField;
    private TextView endLocationField;

    private Button acceptRequest;

    private String nameOfDriver;
    private DriverLocation driverLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requesterpopup);

        this.getSupportActionBar().hide();

        setMetrics();

        avatarView = (ImageView) findViewById(R.id.avatarImage);
        usernameField = (TextView) findViewById(R.id.username);
        currentLocationField = (TextView) findViewById(R.id.currentlocation);
        endLocationField = (TextView) findViewById(R.id.endlocation);

        acceptRequest = (Button) findViewById(R.id.accept);

        nameOfDriver = getNameOfDriver();
        driverLocation = getDriverLocation();

        setAvatarImage();
        setUsernameFromIntent();
        setCurrentLocationFromIntent();
      //  setEndLocationFromIntent();


    }

    public String getNameOfDriver(){
        if (this.getIntent().getStringExtra("diver name") == null){
            Log.e("Driver's name problem","Problem to get name of driver from intent-> null pointer");
            return "";
        }
        return this.getIntent().getStringExtra("diver name");
    }

    public DriverLocation getDriverLocation(){
        DriverLocation driverLoc = new DriverLocation();

        double latitude = this.getIntent().getDoubleExtra("driver location_latitude",-1);
        double longitude = this.getIntent().getDoubleExtra("driver location_longitude",-1);

        if (latitude == -1 || longitude == -1){
            Log.e("Location from Intent","Problem to get location from intent -> latitude: "+latitude+"; longitude: "+longitude);
        }
        else{
            driverLoc.setDriver_latitude(latitude);
            driverLoc.setDriver_longitude(longitude);
        }
        return driverLoc;
    }

    public void setAvatarImage(){
       int avatarRessource = this.getIntent().getIntExtra("avatar image",-1);

       if (avatarRessource == 1){
           Log.e("Avatar image problem","Problem to get ressource for avatar image from intetn");
           return;
       }

       this.avatarView.setImageResource(avatarRessource);
    }

    public void setUsernameFromIntent(){
        if (this.getIntent().getStringExtra("username_rider") == null){
            Log.e("Rider's name problem","Problem to get name of rider from intent");
            return;
        }

        String ridersUsername = this.getIntent().getStringExtra("username_rider");
        this.usernameField.setText(ridersUsername);
    }

    public void setCurrentLocationFromIntent(){
        if (this.getIntent().getStringExtra("current address") == null){
            Log.e("Endlocation problem","Problem to get End location from intetn-> show on null object");
            return;
        }
        String endaddress = this.getIntent().getStringExtra("current address");

        this.currentLocationField.setText(endaddress);
    }

    //TODO get latitude and longitude from intent and make location
    public void setEndLocationFromIntent(){
        if (this.getIntent().getStringExtra("end address") == null){
            Log.e("Endlocation problem","Problem to get End location from intetn-> show on null object");
            return;
        }
        String endaddress = this.getIntent().getStringExtra("end address");

        this.endLocationField.setText(endaddress);
    }


    public void setMetrics(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width*0.8),(int) (height*0.3));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }
}