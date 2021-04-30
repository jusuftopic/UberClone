package com.example.uberclone.Modules.Participants;

import com.example.uberclone.Modules.Participants.User;
import com.example.uberclone.Modules.Phonenumber;

public class Driver extends User {

    public Driver(){}

    public Driver(String name,String password, String email, int age, Phonenumber phonenumber){
        super(name, password, email, age, phonenumber);
    }
}
