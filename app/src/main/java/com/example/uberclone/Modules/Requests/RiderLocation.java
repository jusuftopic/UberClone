package com.example.uberclone.Modules.Requests;

public class RiderLocation {

    private double rider_latitude;
    private double rider_longitude;

    public RiderLocation(){}

    public RiderLocation(double latitude, double longitude){
        this.rider_latitude = latitude;
        this.rider_longitude = longitude;
    }

    public double getRider_latitude() {
        return rider_latitude;
    }

    public void setRider_latitude(double rider_latitude) {
        this.rider_latitude = rider_latitude;
    }

    public double getRider_longitude() {
        return rider_longitude;
    }

    public void setRider_longitude(double rider_longitude) {
        this.rider_longitude = rider_longitude;
    }
}
