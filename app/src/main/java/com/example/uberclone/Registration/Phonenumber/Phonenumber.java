package com.example.uberclone.Registration.Phonenumber;

import android.view.View;

public interface Phonenumber {

    public void addNumberToDatabase(View view);
    public String mergePhonenumber(String prefix, String phonenumber);
    public String getNameOfUser();
    public String[] getNames();
    public int[] getImages();
    public boolean fieldsEmpty();
    public void restartFields();
    public void addNumberLater(View view);


}
