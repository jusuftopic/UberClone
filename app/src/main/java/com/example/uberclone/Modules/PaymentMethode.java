package com.example.uberclone.Modules;

public enum PaymentMethode {

    DEBITCARD("Debit card"),
    CREDITCARD("Credit card");

    public final String paymentmethode;

     private PaymentMethode(String paymentmethode){
        this.paymentmethode = paymentmethode;
    }

}

