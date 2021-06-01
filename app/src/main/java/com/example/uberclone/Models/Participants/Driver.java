package com.example.uberclone.Models.Participants;

import com.example.uberclone.Models.Phonenumber;

public class Driver extends User {

    public Driver(){}

    public Driver(String name,String password, String email, int age, Phonenumber phonenumber){
        super(name, password, email, age, phonenumber);
    }
}
