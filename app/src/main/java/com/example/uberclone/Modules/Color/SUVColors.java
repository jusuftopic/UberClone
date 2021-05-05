package com.example.uberclone.Modules.Color;

public enum SUVColors {

    BLACK("Black"),
    WHITE("White"),
    YELLOW("Yellow"),
    GREEN("Green"),
    RED("Red"),
    BLUE("Blue"),
    ORANGE("Orange"),
    VIOLET("Violet"),
    BROWN("Brown"),
    GREY("Grey"),
    PINK("Pink");

    public  final String colorname;

    private SUVColors(String colorname){
        this.colorname = colorname;
    }

    public String getColorname(){
        return this.colorname;
    }


}
