package com.example.uberclone.MainApp.RiderMain;

import com.example.uberclone.MainApp.Rider.RiderMainContent;
import com.example.uberclone.Modules.Requests.RiderLocation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RiderLocationMapTest {

    private RiderMainContent riderMainContent;
    private RiderLocation riderLocation;

    @Before
    public void setUp(){
        riderMainContent = new RiderMainContent();
        riderLocation = new RiderLocation(30,30);
    }

    @After
    public void tearDown(){
        riderMainContent = null;
        riderLocation = null;
    }

    @Test
    public void testAddingRiderLocationInDatabase(){
    }
}
