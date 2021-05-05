package com.example.uberclone.Modules.Car;

import android.graphics.Color;

public class UberSUV extends Car {

    public static final int MAX_NUMBER_OF_PASSENGERS = 4;

    public static final double MIN_PRICE_RANGE = 1.7;
    public static final double MAX_PRICE_RANGE = 3.8;


    public final static int COLOR_ENTERIOR = Color.BLACK;
    public final static int COLOR_INTERIOR = Color.BLACK;

    private String color_enterior;
    private String color_interior;

    public UberSUV(String autoname, int numberOfDoors, int maxPassengers,String color_enterior, String color_interior,double price_per_km){
        super(autoname, numberOfDoors, maxPassengers, price_per_km);
        this.color_enterior = color_enterior;
        this.color_interior = color_interior;
    }

    public boolean isValideInterior(String color){
        return color.equalsIgnoreCase("black");
    }

    public boolean isValideEnterior(String color){
        return color.equalsIgnoreCase("black");
    }

    public void setColorEnterior(String color_enterior){
        this.color_enterior = color_enterior;
    }
    public void setColorInterior(String color_interior){
        this.color_interior = color_interior;
    }

    public String getColor_enterior(){
        return this.color_enterior;
    }
    public String getColor_interior(){
        return this.color_interior;
    }
}
