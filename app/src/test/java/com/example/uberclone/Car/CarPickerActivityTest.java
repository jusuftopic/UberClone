package com.example.uberclone.Car;

import com.example.uberclone.Modules.Car.UberBlack;
import com.example.uberclone.Modules.Car.UberExpressPool;
import com.example.uberclone.Modules.Car.UberLux;
import com.example.uberclone.Modules.Car.UberSUV;
import com.example.uberclone.Modules.Car.UberSelect;
import com.example.uberclone.Modules.Car.UberWAV;
import com.example.uberclone.Modules.Car.UberX;
import com.example.uberclone.Modules.Car.UberXL;
import com.example.uberclone.Registration.DriverCarDetails.CarTypePicker;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarPickerActivityTest {

    private CarTypePicker carTypePicker;
    private String[] cartype;
    @Before
    public void setUp(){
        carTypePicker = new CarTypePicker();
        cartype = new String[8];

        cartype[0] = "UberX";
        cartype[1] = "UberXL";
        cartype[2] = "UberSUV";
        cartype[3] = "UberBlack";
        cartype[4] = "UberSelect";
        cartype[5] = "UberExpressPool";
        cartype[6] = "UberLux";
        cartype[7] = "UberWAV";

    }

    @After
    public void tearDown(){
        carTypePicker = null;
        cartype = null;
    }

    @Test
    public void testVehicleTypeString(){
        Assert.assertEquals(cartype,carTypePicker.getVehicleTypes());
    }

    @Test
    public void testPriceRange_1(){
        String uberXprice = UberX.MIN_PRICE_RANGE+"-"+UberX.MAX_PRICE_RANGE+"$";

        String[] pricerange = carTypePicker.getPricerange();

        Assert.assertEquals(uberXprice, pricerange[0]);
    }

    @Test
    public void testPriceRange_3(){
        String uberExpessPoolprice = UberExpressPool.MIN_PRICE_RANGE+"-"+UberExpressPool.MAX_PRICE_RANGE+"$";

        String[] pricerange = carTypePicker.getPricerange();

        Assert.assertEquals(uberExpessPoolprice, pricerange[5]);
    }
    @Test
    public void testPriceRange_2(){
        String uberSUVprice = UberSUV.MIN_PRICE_RANGE+"-"+UberSUV.MAX_PRICE_RANGE+"$";

        String[] pricerange = carTypePicker.getPricerange();

        Assert.assertEquals(uberSUVprice, pricerange[2]);
    }

    @Test
    public void testPriceRange_4(){
        String uberWAVprice = UberWAV.MIN_PRICE_RANGE+"-"+UberWAV.MAX_PRICE_RANGE+"$";

        String[] pricerange = carTypePicker.getPricerange();

        Assert.assertEquals(uberWAVprice, pricerange[7]);
    }
}
