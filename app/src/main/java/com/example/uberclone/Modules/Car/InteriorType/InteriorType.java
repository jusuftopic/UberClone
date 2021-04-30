package com.example.uberclone.Modules.Car.InteriorType;

public enum InteriorType {

    LEATHER("LEATHER"),
    VINLY("VINLY");

    public final String interiortype;

    private InteriorType(String interiortype){
        this.interiortype = interiortype;
    }

    public String getInteriortype(){
        return this.interiortype;
    }

}
