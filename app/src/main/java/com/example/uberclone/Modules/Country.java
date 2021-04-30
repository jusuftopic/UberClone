package com.example.uberclone.Modules;

public enum Country {

    USA("USA"),
    UK("UK"),
    GER("GER"),
    AUT("AUT"),
    ESP("ESP"),
    ITA("ITA");

    public final String coutryname;

    private Country(String coutryname){
        this.coutryname = coutryname;
    }

    public String getCoutryname(){
        return this.coutryname;
    }
}
