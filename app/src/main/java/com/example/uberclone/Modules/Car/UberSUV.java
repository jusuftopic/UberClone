package com.example.uberclone.Modules.Car;

import android.graphics.Color;

public class UberSUV extends Car {

    public static final int MAX_NUMBER_OF_PASSENGERS = 4;

    public static final double MIN_PRICE_RANGE = 1.7;
    public static final double MAX_PRICE_RANGE = 3.8;


    public final static int COLOR_ENTERIOR = Color.BLACK;
    public final static int COLOR_INTERIOR = Color.BLACK;

    private Color color_enterior;
    private Color color_interior;

    public UberSUV(String autoname, int numberOfDoors, int maxPassengers,Color color_enterior, Color color_interior,double price_per_km){
        super(autoname, numberOfDoors, maxPassengers, price_per_km);
        this.color_enterior = color_enterior;
        this.color_interior = color_interior;
    }

    public void setColorEnterior(Color color_enterior){
        this.color_enterior = color_enterior;
    }
    public void setColorInterior(Color color_interior){
        this.color_interior = color_interior;
    }

    public Color getColor_enterior(){
        return this.color_enterior;
    }
    public Color getColor_interior(){
        return this.color_interior;
    }
}
