package com.yline.formula.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TimeUtilTest {

    @Test
    public void main() {
        assertEquals(30, TimeUtil.getDaysOfMonth(2019, 11));
        assertEquals(31, TimeUtil.getDaysOfMonth(2019, 10));
        assertEquals(28, TimeUtil.getDaysOfMonth(2019, 2));

        assertEquals(365, TimeUtil.getDaysOfYear(2019));
        assertEquals(365, TimeUtil.getDaysOfYear(2018));
        assertEquals(366, TimeUtil.getDaysOfYear(2020));
    }
}
