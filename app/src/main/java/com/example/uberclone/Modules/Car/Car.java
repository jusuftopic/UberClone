package com.example.uberclone.Modules.Car;

public abstract class Car {

    private String autoname;
    private int numberOfDoors;
    private int maxPassengers;
    private double price_per_km;

    public Car(){}

    public Car(String autoname, int numberOfDoors, int maxPassengers,double price_per_km){
        this.autoname = autoname;
        this.numberOfDoors = numberOfDoors;
        this.maxPassengers = maxPassengers;
        this.price_per_km = price_per_km;
    }


    public boolean isValidNumberOfPassangers(int passangers){
        if (this.maxPassengers >= 1 && this.maxPassengers <= passangers){
            return true;
        }
        return false;
    }

    public boolean isValidNumberOfDoors(int maxNumOfDoors){
        return this.numberOfDoors > 0 && this.numberOfDoors <= maxNumOfDoors;
    }

    public boolean isValidPrice(double minRange,double maxRange){
        return this.price_per_km >= minRange && this.price_per_km <= maxRange;
    }

    public void setAutoname(String autoname){
        this.autoname = autoname;
    }
    public void setNumberOfDoors(int numberOfDoors){
        this.numberOfDoors=numberOfDoors;
    }
    public void setMaxPassengers(int maxPassengers){
        this.maxPassengers = maxPassengers;
    }

    public void setPrice_per_km(double price_per_km) {
        this.price_per_km = price_per_km;
    }

    public String getAutoname(){
        return this.autoname;
    }
    public int getNumberOfDoors(){
        return this.numberOfDoors;
    }
    public int getMaxPassengers(){
        return this.maxPassengers;
    }

    public double getPrice_per_km() {
        return price_per_km;
    }
}
