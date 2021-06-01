package com.example.uberclone.Models.Car;

public class UberXL extends Car {

    public static final int MAX_NUMBER_OF_PASSENGERS = 6;

    public static final int MAX_NUMBER_OF_DOORS = 6;


    public static final double MIN_PRICE_RANGE = 2;
    public static final double MAX_PRICE_RANGE = 4;

    public UberXL(String autoname, int numberOfDoors, int maxPassengers, double price_per_km) {
        super(autoname, numberOfDoors, maxPassengers, price_per_km);
    }
}
