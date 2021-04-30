package com.example.uberclone.Modules.Card;

public enum CardType {

    VISA("VISA", 4),
    MASTERCARD("MASTERCARD", 5),
    AMERICAN_EXPRESS("AMERICAN EXPRESS", 3),
    DISCOVER_CARDS("DISCOVERD CARDS", 6);

    public final String cardname;
    public final int firstdigits;

    private CardType(String cardname, int firstdigits) {
        this.cardname = cardname;
        this.firstdigits = firstdigits;
    }

    public String getCardname(){
        return this.cardname;
    }
    public int getFirstdigits(){
        return this.firstdigits;
    }
}
