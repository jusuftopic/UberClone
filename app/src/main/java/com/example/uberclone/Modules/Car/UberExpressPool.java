package com.example.uberclone.Modules.Car;

import android.graphics.Color;
import android.location.Location;

import java.util.ArrayList;

public class UberExpressPool extends Car {

    public static final int MAX_NUMBER_OF_PASSENGERS = 4;

    public static final int MAX_NUMBER_OF_DOORS = 4;

    public static final double MIN_PRICE_RANGE = 1.1;
    public static final double MAX_PRICE_RANGE = 2.6;

    private ArrayList<Location> spots;

    public UberExpressPool(String autoname, int numberOfDoors, int maxPassengers, ArrayList<Location> spots,double price_per_km){
        super(autoname,numberOfDoors,maxPassengers, price_per_km);
        this.spots = spots;
    }

    public ArrayList<Location> getSpots() {
        return spots;
    }

    public void setSpots(ArrayList<Location> spots) {
        this.spots = spots;
    }
}
