package com.example.uberclone.MainApp.CallBacks.Rider;

import com.example.uberclone.Models.Requests.RiderLocation;

public interface CurrentLocationCallBack {
    public void onCurrentLocationCallBack(RiderLocation currentRiderLocation);

    public void onCurrentLocationCallBackWihtUsername(String username, RiderLocation riderLocation);
}

