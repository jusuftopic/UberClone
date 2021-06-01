package com.example.uberclone.Models.Participants;

import com.example.uberclone.Models.PaymentMethode;
import com.example.uberclone.Models.Phonenumber;

public class Rider extends User {

    private PaymentMethode paymentMethode;

    public Rider(){}

    public Rider(String name, String password, String email, int age, Phonenumber phonenumber, PaymentMethode paymentMethode){
        super(name, password, email, age,phonenumber);
        this.paymentMethode = paymentMethode;
    }


    public void setPaymentMethode(PaymentMethode paymentMethode){
        this.paymentMethode = paymentMethode;
    }
    public PaymentMethode getPaymentMethode(){
        return this.paymentMethode;
    }

}
