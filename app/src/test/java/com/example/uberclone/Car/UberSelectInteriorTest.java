package com.example.uberclone.Car;

import com.example.uberclone.Registration.DriverCarDetails.CarInformations.UberSelectDetails;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UberSelectInteriorTest {

    private UberSelectDetails uberSelectDetails;
    private String[] interiors;

    @Before
    public void setUp(){
        uberSelectDetails = new UberSelectDetails();
        interiors = new String[]{"LEATHER","VINLY"};

    }
    @After
    public void tearDown(){
        uberSelectDetails = null;
        interiors = null;
    }
    @Test
    public void testInteriorsFromEnum(){
        Assert.assertEquals(interiors[0],uberSelectDetails.getInteriors()[0]);
        Assert.assertEquals(interiors[1],uberSelectDetails.getInteriors()[1]);
    }
}
