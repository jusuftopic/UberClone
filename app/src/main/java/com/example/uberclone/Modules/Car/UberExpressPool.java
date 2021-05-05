package com.example.uberclone.Modules.Car;

import android.graphics.Color;
import android.location.Location;

public class UberExpressPool extends Car {

    public static final int MAX_NUMBER_OF_PASSENGERS = 4;

    public static final int MAX_NUMBER_OF_DOORS = 4;



    public static final double MIN_PRICE_RANGE = 1.1;
    public static final double MAX_PRICE_RANGE = 2.6;

    private Location locationToWait;

    public UberExpressPool(String autoname, int numberOfDoors, int maxPassengers, Location locationToWait,double price_per_km){
        super(autoname,numberOfDoors,maxPassengers, price_per_km);
        this.locationToWait = locationToWait;
    }

    public void setLocationToWait(Location locationToWait){
        this.locationToWait = locationToWait;
    }
    public Location getLocationToWait(){
        return this.locationToWait;
    }
}
