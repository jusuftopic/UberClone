package com.example.uberclone.Models.Car;

public class UberWAV extends Car {

    public static final int MAX_NUMBER_OF_PASSENGERS = 6;

    public static final int MAX_NUMBER_OF_DOORS = 6;

    public static final double MIN_PRICE_RANGE = 3.2;
    public static final double MAX_PRICE_RANGE = 5.5;

    private boolean vehicle_for_wheelchair;
    private boolean pass_certification;

    public UberWAV(String autoname, int numberOfDoors, int maxPassengers,boolean vehicle_for_wheelchair, boolean pass_certification, double price_per_km){
        super(autoname, numberOfDoors, maxPassengers,price_per_km);
        this.vehicle_for_wheelchair=vehicle_for_wheelchair;
        this.pass_certification = pass_certification;
    }


    public boolean isValideUberWAV(){
        return vehicle_for_wheelchair && pass_certification;
    }


    public void setVehicle_for_wheelchair(boolean vehicle_for_wheelchair) {
        this.vehicle_for_wheelchair = vehicle_for_wheelchair;
    }


    public void setPass_certification(boolean pass_certification) {
        this.pass_certification = pass_certification;
    }
}
