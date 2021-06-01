package com.example.uberclone.Models.Car;

public class UberLux extends Car{

    public static final int MAX_NUMBER_OF_PASSENGERS = 4;

    public static final int MAX_NUMBER_OF_DOORS = 4;


    public static final double MIN_PRICE_RANGE = 4.5;
    public static final double MAX_PRICE_RANGE = 6.6;

    private boolean commercial_registration;
    private boolean insurance;

    public UberLux(String autoname, int numberOfDoors, int maxPassengers,boolean commercial_registration, boolean insurance,double price_per_km){
        super(autoname,numberOfDoors,maxPassengers, price_per_km);
        this.commercial_registration = commercial_registration;
        this.insurance = insurance;
    }

    public boolean isValidUberLux(){
        if (this.commercial_registration && this.insurance){
            return true;
        }
        return false;
    }


    public void setCommercial_registration(boolean commercial_registration) {
        this.commercial_registration = commercial_registration;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }
}
