package com.example.uberclone.MainApp.Driver.FirebaseCallbacks;

import com.example.uberclone.Modules.Requests.RiderLocation;

import java.util.ArrayList;

public interface FirebaseCallBackCurrentRiderLocation {

    public void onCallbackCurrentRiderLocation(ArrayList<RiderLocation> currentlocation);
}
