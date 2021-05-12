package com.example.uberclone.Modules.Car;

import android.graphics.Color;

public class UberBlack extends Car {

    private static final int MAX_NUMBER_OF_PASSENGERS = 4;

    public static final int MAX_NUMBER_OF_DOORS = 4;



    public final static int COLOR_ENTERIOR = Color.BLACK;
    public final static int COLOR_INTERIOR = Color.BLACK;

    public static final double MIN_PRICE_RANGE = 2.2;
    public static final double MAX_PRICE_RANGE = 5.3;

    private String color_enterior;
    private String color_interior;
    private boolean airportpermit;

    public UberBlack(String autoname, int numberOfDoors, int maxPassengers,String color_enterior, String color_interior,boolean airportpermit,double price_per_km){
        super(autoname, numberOfDoors, maxPassengers, price_per_km);
        this.color_enterior = color_enterior;
        this.color_interior = color_interior;
        this.airportpermit = airportpermit;
    }


    public void setColorEnterior(String color_enterior){
        this.color_enterior = color_enterior;
    }
    public void setColorInterior(String color_interior){
        this.color_interior = color_interior;
    }
    public void setAirportpermit(boolean airportpermit){
        this.airportpermit = airportpermit;
    }

    public String getColor_enterior(){
        return this.color_enterior;
    }
    public String  getColor_interior(){
        return this.color_interior;
    }
    public boolean getAirportPermit(){
        return this.airportpermit;
    }
}
