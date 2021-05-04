package com.example.uberclone.Modules.Car.CarMarks;

public enum SUVMarks {
    SUBARU("Subaru"),
    HONDA("HONDA"),
    KIA("KIA"),
    FORD("FORD"),
    Volvo("VOLVO"),
    LandRover("Land Rover"),
    Lincoln("Lincoln");

    public final String suvmark;

    private SUVMarks(String mark){
        this.suvmark = mark;
    }

    public String getSUVmark(){
        return this.suvmark;
    }

    public static boolean isValideSUVMark(String suvmark){
        for (SUVMarks suvMark : SUVMarks.values()){
            if (suvMark.getSUVmark().equalsIgnoreCase(suvmark)){
                return true;
            }
        }

        return false;
    }
}
