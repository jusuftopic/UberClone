package com.example.uberclone.Modules.Card;


import com.braintreepayments.cardform.utils.CardType;

public class Card {

    private String cardholder;
    private String cardnumber;
    private String expirationMonth;
    private String expiarationYear;
    private String cvv;
    private com.braintreepayments.cardform.utils.CardType cardType;



    public Card(String cardholder, String cardnumber, String expirationMonth, String expirationYear, String cvv, CardType cardType){
        this.cardholder = cardholder;
        this.cardnumber = cardnumber;
        this.expirationMonth = expirationMonth;
        this.expiarationYear = expirationYear;
        this.cvv = cvv;
        this.cardType = cardType;
    }

    public void setCardholder(String cardholder){
        this.cardholder = cardholder;
    }
    public void setCardnumber(String cardnumber){
        this.cardnumber = cardnumber;
    }
    public void setExpirationMonth(String expirationMonth){
        this.expirationMonth = expirationMonth;
    }
    public void setExpiarationYear(String expiarationYear){
        this.expiarationYear = expiarationYear;
    }
    public void setCvc(String cvc){
        this.cvv = cvc;
    }
    public void setCardType(CardType cardType){
        this.cardType = cardType;
    }


    public String getCardholder(){
        return this.cardholder;
    }
    public String getCardnumber(){
        return this.cardnumber;
    }
    public String getExpirationMonth(){
        return this.expirationMonth;
    }
    public String getExpiarationYear(){
        return this.expiarationYear;
    }
    public String getCvc(){
        return this.cvv;
    }
    public CardType getCardType(){
        return this.cardType;
    }



}
