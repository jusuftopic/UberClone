package com.example.uberclone.Modules.Participants;

import com.example.uberclone.Modules.Phonenumber;

public abstract class User {

    private String name;
    private String password;
    private String email;
    private int age;
    private Phonenumber phonenumber;


    public User(){}

    public User(String name, String password, String email, int age, Phonenumber phonenumber){

        this.name = name;
        this.password = password;
        this.email = email;
        this.age = age;
        this.phonenumber = phonenumber;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setAge(int age){
        this.age = age;
    }
    public void setPhonenumber(Phonenumber phonenumber){
        this.phonenumber = phonenumber;
    }

    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }
    public int getAge(){
        return this.age;
    }
    public Phonenumber getPhonenumber(){
        return this.phonenumber;
    }
}
