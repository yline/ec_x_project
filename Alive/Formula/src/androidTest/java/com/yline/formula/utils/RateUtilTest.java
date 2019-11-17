package com.yline.formula.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RateUtilTest {

    @Test
    public void main() {
        assertEquals(15, RateUtil.day2Month(5), 0.0001);

        assertEquals(18, RateUtil.day2Year(5), 0.0001);
    }
}
