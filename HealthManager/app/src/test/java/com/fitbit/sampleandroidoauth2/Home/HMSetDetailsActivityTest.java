package com.fitbit.sampleandroidoauth2.Home;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pag11 on 8/7/2017.
 */
public class HMSetDetailsActivityTest {
    @Test
    public void isEmailValid() throws Exception {
        String email="abc@gmail.com";
        assertTrue(HMSetDetailsActivity.isEmailValid(email));
    }

    @Test
    public void isPasswordValid() throws Exception {
        String password= "abcdefgh";
        assertTrue(HMSetDetailsActivity.isPasswordValid(password));
    }

    @Test
    public void getBMR() throws Exception {
        String gender="Male";
        int weight=78;
        int height=160;
        int age=23;
        double BMR=HMSetDetailsActivity.getBMR(gender,weight,height,age);
        assertEquals(1670,BMR,0.1);
    }

    @Test
    public void getGoal() throws Exception {
        String target="Gain";
        double BMR=1670;
        int desiredWeight=88;
        int currentWeight=78;
        int noOfWeeks=10;
        double goal=HMSetDetailsActivity.getGoal(target,BMR,desiredWeight,currentWeight,noOfWeeks);
        assertEquals(3120.7,goal,0.1);
    }

}