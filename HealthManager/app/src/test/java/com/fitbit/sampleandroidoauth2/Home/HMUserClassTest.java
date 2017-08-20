package com.fitbit.sampleandroidoauth2.Home;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by pag11 on 8/10/2017.
 */
public class HMUserClassTest {

    HMUserClass user;

    public void setuserDesiredWeight() throws Exception {
        user.setuserDesiredWeight(75);
    }

    @Test
    public void getuserDesiredWeight() throws Exception {
        setuserDesiredWeight();
        assertEquals(75,user.getuserDesiredWeight());
    }

    public void setUserTarget() throws Exception {
        user.setUserTarget("Reduce");
    }

    @Test
    public void getUserTarget() throws Exception {
        setUserTarget();
        assertEquals("Reduce",user.getUserTarget());
    }

    public void setUserGender() throws Exception {
        user.setUserGender("Male");
    }

    @Test
    public void getUserGender() throws Exception {
        setUserGender();
        assertEquals("Male",user.getUserGender());
    }

    public void setnoOfWeeks() throws Exception {
        user.setnoOfWeeks(5);
    }

    @Test
    public void getnoOfWeeks() throws Exception {
        setnoOfWeeks();
        assertEquals(5,user.getnoOfWeeks());
    }
}