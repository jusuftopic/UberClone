package com.example.uberclone.Car;

import com.example.uberclone.Modules.Car.Car;
import com.example.uberclone.Modules.Car.InteriorType.InteriorType;
import com.example.uberclone.Modules.Car.UberLux;
import com.example.uberclone.Modules.Car.UberSUV;
import com.example.uberclone.Modules.Car.UberSelect;
import com.example.uberclone.Modules.Car.UberWAV;
import com.example.uberclone.Modules.Car.UberX;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarTest {

    private Car car;
    private UberLux uberLux;
    private UberSelect uberSelect;
    private UberWAV uberWAV;
    private UberSUV uberSUV;

    @Before
    public void setUp(){
        car = new UberX("Mazda",3,4,2.1);
        uberLux = new UberLux("Mercedes",4,4,true,true,5.5);
        uberSelect = new UberSelect("Cadillac",4,4,"Leather",3.3);
        uberWAV = new UberWAV("Combi",3,UberWAV.MAX_NUMBER_OF_PASSENGERS,true,true,3.4);
        uberSUV = new UberSUV("Subaru",4,4,"black","black",3.4);

    }
    @After
    public void tearUp(){
        car = null;
        uberLux = null;
        uberSelect = null;
        uberWAV = null;
        uberSUV = null;
    }

    @Test
    public void testValidNumberPassangersOn4_TRUE(){

     //  Assert.assertTrue(car.isValidNumberOfPassangers(UberLux.MAX_NUMBER_OF_PASSENGERS));

        Assert.assertTrue(car.isValidNumberOfPassangers(4));
    }

    @Test
    public void testValidNumberPassangersFALSE(){
        Car car = new UberX("Mazda",3,4,2.1);

        Assert.assertFalse(car.isValidNumberOfPassangers(0));
    }

    @Test
    public void testValidNumberOfDoors_TRUE(){

       Assert.assertTrue(car.isValidNumberOfDoors(UberX.MAX_NUMBER_OF_DOORS));
    }

    @Test
    public void testValidNumberOfDoors_FALSE(){
        car.setNumberOfDoors(0);

        Assert.assertFalse(car.isValidNumberOfDoors(UberX.MAX_NUMBER_OF_DOORS));
    }

    @Test
    public void testValidNumberOfDoors_FALSE2(){
        car.setNumberOfDoors(7);

        Assert.assertFalse(car.isValidNumberOfDoors(UberX.MAX_NUMBER_OF_DOORS));
    }


    @Test
    public void testValidPrice_TRUE(){

        Assert.assertTrue(uberLux.isValidPrice(UberLux.MIN_PRICE_RANGE,UberLux.MAX_PRICE_RANGE));
    }

    @Test
    public void testValidPrice_FALSE(){
        uberLux.setPrice_per_km(2.2);

        Assert.assertFalse(car.isValidPrice(UberLux.MIN_PRICE_RANGE,UberLux.MAX_PRICE_RANGE));
    }

    @Test
    public void testUberLUXValidation_TRUE(){
        Assert.assertTrue(uberLux.isValidUberLux());
    }

    @Test
    public void testUberLUXValidation_FALSE1(){
        uberLux.setCommercial_registration(true);
        uberLux.setInsurance(false);

        Assert.assertFalse(uberLux.isValidUberLux());
    }

    @Test
    public void testUberLUXValidation_FALSE2(){
        uberLux.setCommercial_registration(false);
        uberLux.setInsurance(true);

        Assert.assertFalse(uberLux.isValidUberLux());
    }

    @Test
    public void testUberLUXValidation_FALSE3(){
        uberLux.setCommercial_registration(false);
        uberLux.setInsurance(false);

        Assert.assertFalse(uberLux.isValidUberLux());
    }

    @Test
    public void testUberSelectInterior_TRUE(){
        Assert.assertTrue(uberSelect.isValidInterior());
    }

    @Test
    public void testUberSelectInterior_TRUE2(){
        uberSelect.setInterior(InteriorType.VINLY.getInteriortype());

        Assert.assertTrue(uberSelect.isValidInterior());
    }

    @Test
    public void testUberSelectInterior_FALSE(){
        uberSelect.setInterior("Alcantara");

        Assert.assertFalse(uberSelect.isValidInterior());
    }

    @Test
    public void testValideUberWAV_TRUE(){
        Assert.assertTrue(uberWAV.isValideUberWAV());
    }

    @Test
    public void testValideUberWAV_FALSE(){
        uberWAV.setPass_certification(false);
        uberWAV.setVehicle_for_wheelchair(true);

        Assert.assertFalse(uberWAV.isValideUberWAV());
    }

    @Test
    public void testValideUberWAV_FALSE2(){
        uberWAV.setPass_certification(true);
        uberWAV.setVehicle_for_wheelchair(false);

        Assert.assertFalse(uberWAV.isValideUberWAV());
    }
    @Test
    public void testValideUberWAV_FALSE3(){
        uberWAV.setPass_certification(false);
        uberWAV.setVehicle_for_wheelchair(false);

        Assert.assertFalse(uberWAV.isValideUberWAV());
    }

    @Test
    public void testUberSUVEnterior_True1(){
        String color = "black";

        Assert.assertTrue(uberSUV.isValideEnterior(color));
    }

    @Test
    public void testUberSUVEnterior_True2(){
        String color = "BLACK";

        Assert.assertTrue(uberSUV.isValideEnterior(color));
    }

    @Test
    public void testUberSUVEnterior_True3(){
        String color = "Black";

        Assert.assertTrue(uberSUV.isValideEnterior(color));
    }

    @Test
    public void testUberSUVInterior_True1(){
        String color = "black";

        Assert.assertTrue(uberSUV.isValideInterior(color));
    }

    @Test
    public void testUberSUVInterior_True2(){
        String color = "BLACK";

        Assert.assertTrue(uberSUV.isValideInterior(color));
    }

    @Test
    public void testUberSUVInterior_True3(){
        String color = "Black";

        Assert.assertTrue(uberSUV.isValideInterior(color));
    }

    @Test
    public void testUberSUVEnterior_FALSE(){
        String color = "Yellow";

        Assert.assertFalse(uberSUV.isValideEnterior(color));
    }

    @Test
    public void testUberSUVInterior_FALSE(){
        String color = "Yellow";

        Assert.assertFalse(uberSUV.isValideInterior(color));
    }

}
