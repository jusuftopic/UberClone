package com.example.uberclone.Models;

public class Phonenumber {

    private String phonenumber;
    private Country country;

    public Phonenumber(){}

    public Phonenumber(String phonenumber, Country country){
        this.phonenumber = phonenumber;
        this.country = country;
    }

    public void setPhonenumber(String phonenumber){
        this.phonenumber = phonenumber;
    }
    public void setCounty(Country country){
        this.country = country;
    }
    public String getPhonenumber(){
        return this.phonenumber;
    }
    public Country getCountry(){
        return this.country;
    }

}
