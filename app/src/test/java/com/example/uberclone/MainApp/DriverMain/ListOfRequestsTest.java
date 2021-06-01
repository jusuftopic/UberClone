package com.example.uberclone.MainApp.DriverMain;

import com.example.uberclone.MainApp.Driver.ShowNearestRequesters;
import com.example.uberclone.Models.Requests.RiderLocation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ListOfRequestsTest {

    private ShowNearestRequesters showNearestRequesters;
    private RiderLocation riderLocation;
    private String addressInfos;

    @Before
    public void setUp(){
        showNearestRequesters = new ShowNearestRequesters();
        riderLocation = new RiderLocation(37.4035483333344,-123.10114666677666);
        addressInfos = "";
    }

    @After
    public void tearDown(){
        showNearestRequesters = null;
        riderLocation = null;
        addressInfos = null;
    }

    @Test
    public void testAddressInfos(){
        addressInfos = showNearestRequesters.getAdressFromLocation(riderLocation);

        Assert.assertNotEquals("",addressInfos);
    }
}
