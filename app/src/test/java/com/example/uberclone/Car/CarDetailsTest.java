package com.example.uberclone.Car;

import com.example.uberclone.Models.Car.UberSUV;
import com.example.uberclone.Registration.DriverCarDetails.CarInformations.UberSUVDetails;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarDetailsTest {

    private UberSUVDetails uberSUVDetails;
    private UberSUV uberSUV;

    @Before
    public void setUp(){
        uberSUVDetails = new UberSUVDetails();
        uberSUV = new UberSUV("Subaru",4,4,"Black","Black",3.5);
    }

    @After
    public void tearDown(){
        uberSUVDetails = null;
        uberSUV = null;
    }

    @Test
    public void testValideInfos_True(){
        Assert.assertTrue(uberSUV.isValidNumberOfDoors(UberSUV.MAX_NUMBER_OF_DOORS));
        Assert.assertTrue(uberSUV.isValidNumberOfPassangers(4));
        Assert.assertTrue(uberSUV.isValideInterior("black"));
        Assert.assertTrue(uberSUV.isValideEnterior("black"));
        Assert.assertTrue(uberSUV.isValidPrice(UberSUV.MIN_PRICE_RANGE,UberSUV.MAX_PRICE_RANGE));
    }

    @Test
    public void testValideInfos_False1(){
        uberSUV.setNumberOfDoors(6);
        Assert.assertFalse(uberSUV.isValidNumberOfDoors(UberSUV.MAX_NUMBER_OF_DOORS));

        Assert.assertTrue(uberSUV.isValidNumberOfPassangers(4));
        Assert.assertTrue(uberSUV.isValideInterior("black"));
        Assert.assertTrue(uberSUV.isValideEnterior("black"));
        Assert.assertTrue(uberSUV.isValidPrice(UberSUV.MIN_PRICE_RANGE,UberSUV.MAX_PRICE_RANGE));
    }

    @Test
    public void testValideInfos_False2(){
        Assert.assertTrue(uberSUV.isValidNumberOfDoors(UberSUV.MAX_NUMBER_OF_DOORS));

        uberSUV.setMaxPassengers(8);
        Assert.assertFalse(uberSUV.isValidNumberOfPassangers(4));

        Assert.assertTrue(uberSUV.isValideInterior("black"));
        Assert.assertTrue(uberSUV.isValideEnterior("black"));
        Assert.assertTrue(uberSUV.isValidPrice(UberSUV.MIN_PRICE_RANGE,UberSUV.MAX_PRICE_RANGE));
    }

    @Test
    public void testValideInfos_False3(){
        Assert.assertTrue(uberSUV.isValidNumberOfDoors(UberSUV.MAX_NUMBER_OF_DOORS));
        Assert.assertTrue(uberSUV.isValidNumberOfPassangers(4));

        String color = "red";
        Assert.assertFalse(uberSUV.isValideInterior(color));

        Assert.assertTrue(uberSUV.isValideEnterior("black"));
        Assert.assertTrue(uberSUV.isValidPrice(UberSUV.MIN_PRICE_RANGE,UberSUV.MAX_PRICE_RANGE));
    }

    @Test
    public void testValideInfos_False4(){

        Assert.assertTrue(uberSUV.isValidNumberOfDoors(UberSUV.MAX_NUMBER_OF_DOORS));
        Assert.assertTrue(uberSUV.isValidNumberOfPassangers(4));
        Assert.assertTrue(uberSUV.isValideInterior("black"));

        String color = "Red";
        Assert.assertFalse(uberSUV.isValideEnterior(color));
        Assert.assertTrue(uberSUV.isValidPrice(UberSUV.MIN_PRICE_RANGE,UberSUV.MAX_PRICE_RANGE));
    }

    @Test
    public void testValideInfos_False5(){

        Assert.assertTrue(uberSUV.isValidNumberOfDoors(UberSUV.MAX_NUMBER_OF_DOORS));
        Assert.assertTrue(uberSUV.isValidNumberOfPassangers(4));
        Assert.assertTrue(uberSUV.isValideInterior("black"));
        Assert.assertTrue(uberSUV.isValideEnterior("black"));

        uberSUV.setPrice_per_km(7.7);
        Assert.assertFalse(uberSUV.isValidPrice(UberSUV.MIN_PRICE_RANGE,UberSUV.MAX_PRICE_RANGE));
    }

}
