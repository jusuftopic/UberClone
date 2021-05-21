package com.example.uberclone.Modules.Requests;

public class DriverLocation {

    private double driver_latitude;
    private double driver_longitude;
    private boolean acceptedCall;
    private RiderLocation rider_currentlocation;
    private RiderLocation rider_endlocation;

    public DriverLocation(){}

    public DriverLocation(double latitude,double longitude){
        this.driver_latitude = latitude;
        this.driver_longitude = longitude;
    }

    public DriverLocation(boolean acceptedCall, RiderLocation rider_currentlocation){
        this.acceptedCall = acceptedCall;
        this.rider_currentlocation = rider_currentlocation;

    }
    public DriverLocation(double latitude, double longitude, boolean acceptedCall, RiderLocation rider_currentlocation, RiderLocation rider_endlocation){
        this.driver_latitude = latitude;
        this.driver_longitude = longitude;
        this.acceptedCall = acceptedCall;
        this.rider_currentlocation = rider_currentlocation;
        this.rider_endlocation = rider_endlocation;
    }

    public double getDriver_latitude() {
        return driver_latitude;
    }

    public void setDriver_latitude(double driver_latitude) {
        this.driver_latitude = driver_latitude;
    }

    public double getDriver_longitude() {
        return driver_longitude;
    }

    public void setDriver_longitude(double driver_longitude) {
        this.driver_longitude = driver_longitude;
    }

    public boolean isAcceptedCall() {
        return acceptedCall;
    }

    public void setAcceptedCall(boolean acceptedCall) {
        this.acceptedCall = acceptedCall;
    }

    public RiderLocation getRider_currentlocation() {
        return rider_currentlocation;
    }

    public void setRider_currentlocation(RiderLocation rider_currentlocation) {
        this.rider_currentlocation = rider_currentlocation;
    }

    public RiderLocation getRider_endlocation() {
        return rider_endlocation;
    }

    public void setRider_endlocation(RiderLocation rider_endlocation) {
        this.rider_endlocation = rider_endlocation;
    }
}
