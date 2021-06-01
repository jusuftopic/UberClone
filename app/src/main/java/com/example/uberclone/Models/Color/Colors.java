package com.example.uberclone.Models.Color;

public enum Colors {

    BLACK("Black",0),
    WHITE("White",1),
    YELLOW("Yellow",2),
    GREEN("Green",3),
    RED("Red",4),
    BLUE("Blue",5),
    ORANGE("Orange",6),
    VIOLET("Violet",7),
    BROWN("Brown",8),
    GREY("Grey",9),
    PINK("Pink",10);

    private   final String colorname;
    private  final int index;

    private Colors(String colorname, int index){
        this.colorname = colorname;
        this.index = index;
    }

    public String getColorname(){
        return this.colorname;
    }

    public int getIndex(){
        return this.index;
    }


}
