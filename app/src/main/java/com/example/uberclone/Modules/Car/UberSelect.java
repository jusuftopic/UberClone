package com.example.uberclone.Modules.Car;

import android.graphics.Color;
import android.text.InputType;

import com.example.uberclone.Modules.Car.Car;
import com.example.uberclone.Modules.Car.InteriorType.InteriorType;

public class UberSelect extends Car {

    public static final int MAX_NUMBER_OF_PASSENGERS = 4;

    public static final int MAX_NUMBER_OF_DOORS = 4;


    public static final double MIN_PRICE_RANGE = 2.2;
    public static final double MAX_PRICE_RANGE = 5.3;

    private String interior;

    public UberSelect(String autoname, int numberOfDoors, int maxPassengers, String interior,double price_per_km) {
        super(autoname, numberOfDoors, maxPassengers, price_per_km);
        this.interior = interior;
    }

    public boolean isValidInterior() {
        if (this.interior.equalsIgnoreCase(InteriorType.LEATHER.getInteriortype()) || this.interior.equalsIgnoreCase(InteriorType.VINLY.getInteriortype())) {
            return true;
        }
        return false;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }


}
