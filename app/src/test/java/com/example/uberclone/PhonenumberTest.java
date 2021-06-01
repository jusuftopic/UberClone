package com.example.uberclone;

import com.example.uberclone.Models.Country;
import com.example.uberclone.Registration.Phonenumber.DriverPhonenumber;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PhonenumberTest {

    private DriverPhonenumber driverPhonenumber;

    @Before
    public void setUp() {
        driverPhonenumber = new DriverPhonenumber();
    }

    @After
    public void tearDown(){
        driverPhonenumber = null;
    }

    @Test
    public void testCountryString(){

        String[] countries = driverPhonenumber.getNames();

        String expectedCountry = Country.USA.getCoutryname();
        String expectedCountry2 = Country.GER.getCoutryname();
        String expectedCountry3 = Country.ITA.getCoutryname();

        Assert.assertEquals(expectedCountry,countries[0]);
        Assert.assertEquals(expectedCountry2,countries[2]);
        Assert.assertEquals(expectedCountry3,countries[5]);
    }

    @Test
    public void testImages(){
        int[] images = driverPhonenumber.getImages();

        int expectedimage1 = R.drawable.usa;
        int expectedImage2 = R.drawable.germany;
        int expectedImage3 = R.drawable.ita;

        Assert.assertEquals(expectedimage1,images[0]);
        Assert.assertEquals(expectedImage2,images[2]);
        Assert.assertEquals(expectedImage3,images[5]);
    }
}
