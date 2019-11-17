package com.yline.formula.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MoneyUtilTest {

    @Test
    public void main() {
        assertEquals(256, MoneyUtil.yuan2fen(2.56));
        assertEquals(256, MoneyUtil.yuan2fen(2.561));
        assertEquals(256, MoneyUtil.yuan2fen(2.564));
        assertEquals(256, MoneyUtil.yuan2fen(2.555));
        assertEquals(256, MoneyUtil.yuan2fen(2.556));
    }
}
