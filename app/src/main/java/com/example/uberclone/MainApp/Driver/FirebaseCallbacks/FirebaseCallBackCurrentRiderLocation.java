package com.example.uberclone.MainApp.Driver.FirebaseCallbacks;

import com.example.uberclone.Models.Requests.RiderLocation;

import java.util.ArrayList;

public interface FirebaseCallBackCurrentRiderLocation {

    public void onCallbackCurrentRiderLocation(ArrayList<RiderLocation> currentlocations);
}
