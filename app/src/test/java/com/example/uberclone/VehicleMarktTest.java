package com.example.uberclone;

import com.example.uberclone.Modules.Car.CarMarks.CarMarks;
import com.example.uberclone.Modules.Car.CarMarks.SUVMarks;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VehicleMarktTest {

    private String rightmarkt_car1;
    private String rightmarkt_car2;
    private String rightmarkt_car3;

    private String wrongmark_car1;
    private String wrongmark_car2;
    private String wrongmark_car3;

    private String rightmark_SUV1;
    private String rightmark_SUV2;
    private String rightmark_SUV3;

    private String wrongmark_SUV1;
    private String wrongmark_SUV2;
    private String wrongmark_SUV3;


    @Before
    public void setUp() {
        rightmarkt_car1 = "AUDI";
        rightmarkt_car2 = "audi";
        rightmarkt_car3 = "Honda";

        wrongmark_car1 = "AAudi";
        wrongmark_car1 = "Land Rover";
        wrongmark_car1 = "Subaru";

        rightmark_SUV1 = "SUBARU";
        rightmark_SUV2 = "subaru";
        rightmark_SUV3 = "Volvo";

        wrongmark_SUV1 = "Ssubaru";
        wrongmark_SUV2 = "Cube";
        wrongmark_SUV3 = "Lada";
    }

    @After
    public void tearDown() {
        rightmarkt_car1 = null;
        rightmarkt_car2 = null;
        rightmarkt_car3 = null;

        wrongmark_car1 = null;
        wrongmark_car2 = null;
        wrongmark_car3 = null;

        rightmark_SUV1 = null;
        rightmark_SUV2 = null;
        rightmark_SUV3 = null;

        wrongmark_SUV1 = null;
        wrongmark_SUV2 = null;
        wrongmark_SUV3 = null;
    }

    @Test
    public void testRightCar1() {
        Assert.assertTrue(CarMarks.isValideCarMarkt(rightmarkt_car1));
    }

    @Test
    public void testRightCar2() {
        Assert.assertTrue(CarMarks.isValideCarMarkt(rightmarkt_car2));
    }

    @Test
    public void testRightCar3() {
        Assert.assertTrue(CarMarks.isValideCarMarkt(rightmarkt_car3));
    }

    @Test
    public void testFalseCar1() {
        Assert.assertFalse(CarMarks.isValideCarMarkt(wrongmark_car1));
    }

    @Test
    public void testFalseCar2() {
        Assert.assertFalse(CarMarks.isValideCarMarkt(wrongmark_car2));
    }

    @Test
    public void testFalseCar3() {
        Assert.assertFalse(CarMarks.isValideCarMarkt(wrongmark_car3));
    }

    @Test
    public void testRightSUV1(){
        Assert.assertTrue(SUVMarks.isValideSUVMark(rightmark_SUV1));
    }

    @Test
    public void testRightSUV2(){
        Assert.assertTrue(SUVMarks.isValideSUVMark(rightmark_SUV2));
    }

    @Test
    public void testRightSUV3(){
        Assert.assertTrue(SUVMarks.isValideSUVMark(rightmark_SUV3));
    }

    @Test
    public void testFalseSUV1(){
        Assert.assertFalse(SUVMarks.isValideSUVMark(wrongmark_SUV1));
    }

    @Test
    public void testFalseSUV2(){
        Assert.assertFalse(SUVMarks.isValideSUVMark(wrongmark_car2));
    }

    @Test
    public void testFalseSUV3(){
        Assert.assertFalse(SUVMarks.isValideSUVMark(wrongmark_SUV3));
    }

}
