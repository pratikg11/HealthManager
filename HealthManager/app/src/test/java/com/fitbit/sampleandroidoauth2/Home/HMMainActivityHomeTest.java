package com.fitbit.sampleandroidoauth2.Home;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pag11 on 8/8/2017.
 */
public class HMMainActivityHomeTest {
    @Test
    public void checkFoodName() throws Exception {
        String food="Rice";
        assertTrue(HMMainActivityHome.checkFoodName(food));
    }

    @Test
    public void checkFromValue() throws Exception {
        String fromValue="100";
        assertTrue(HMMainActivityHome.checkFromValue(fromValue));
    }

    @Test
    public void checkToValue() throws Exception {
        String toValue="800";
        assertTrue(HMMainActivityHome.checkToValue(toValue));
    }

}