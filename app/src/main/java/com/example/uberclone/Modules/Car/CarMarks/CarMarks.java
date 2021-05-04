package com.example.uberclone.Modules.Car.CarMarks;

public enum CarMarks {
    MERCEDES("Mercedes"),
    AUDI("Audi"),
    BMW("BMW"),
    TESLA("Tesla"),
    RollsRoyce("Rolls Royce"),
    Bentley("Bentley"),
    Lexus("Lexus"),
    Toyota("Toyota"),
    Mazda("Mazda"),
    Ford("Ford"),
    Fiat("Fiat"),
    Jaguar("Jaguar"),
    Volvo("Volvo"),
    Nissan("Nissan"),
    Honda("Honda");

    public final String carmark_name;

    private CarMarks(String carmark_mark){
        this.carmark_name = carmark_mark;
    }

    public String getCarmark_name(){
        return this.carmark_name;
    }

    public static boolean isValideCarMarkt(String mark){

        for (CarMarks carMark : CarMarks.values()){
            if (carMark.getCarmark_name().equalsIgnoreCase(mark)){
                return true;
            }
        }
        return false;
    }


}
