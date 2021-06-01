package com.example.uberclone.Models.Car.InteriorType;

public enum InteriorType {

    LEATHER("LEATHER"),
    VINLY("VINLY"),
    POLYESTER("POLYESTER"),
    NYLON("NYLON");


    public final String interiortype;

    private InteriorType(String interiortype){
        this.interiortype = interiortype;
    }

    public String getInteriortype(){
        return this.interiortype;
    }

}
