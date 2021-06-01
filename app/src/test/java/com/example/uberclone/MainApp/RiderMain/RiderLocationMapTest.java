package com.example.uberclone.MainApp.RiderMain;

import com.example.uberclone.MainApp.Rider.RiderMainContent;
import com.example.uberclone.Models.Requests.RiderLocation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RiderLocationMapTest {

    private RiderMainContent riderMainContent;
    private RiderLocation aRiderLocation;

    @Before
    public void setUp(){
        riderMainContent = new RiderMainContent();
        aRiderLocation = new RiderLocation(30,30);
    }

    @After
    public void tearDown(){
        riderMainContent = null;
        aRiderLocation = null;
    }

    @Test
    public void testAddingRiderLocationInDatabase(){
    }
}
