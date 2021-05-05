package com.example.uberclone.Modules.Car;

public class UberX extends Car {

    public static final int MAX_NUMBER_OF_PASSENGERS = 4;

    public static final int MAX_NUMBER_OF_DOORS = 4;


    public static final double MIN_PRICE_RANGE = 1.5;
    public static final double MAX_PRICE_RANGE = 3.5;

    public UberX(){}

    public UberX(String autoname, int numberOfDoors, int maxPassengers, double price_per_km) {
        super(autoname, numberOfDoors, maxPassengers, price_per_km);
    }
}
