package com.example.uberclone.MainApp.RiderMain;

import com.example.uberclone.MainApp.Rider.RiderMainContentLobby;
import com.example.uberclone.Models.Requests.RiderLocation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RiderLocationMapTest {

    private RiderMainContentLobby riderMainContentLobby;
    private RiderLocation aRiderLocation;

    @Before
    public void setUp(){
        riderMainContentLobby = new RiderMainContentLobby();
        aRiderLocation = new RiderLocation(30,30);
    }

    @After
    public void tearDown(){
        riderMainContentLobby = null;
        aRiderLocation = null;
    }

    @Test
    public void testAddingRiderLocationInDatabase(){
    }
}
