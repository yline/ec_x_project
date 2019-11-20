package com.yline.formula.module.stock;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.yline.utils.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class StockManagerTest {

    @Test
    public void testPriceValuation() {
        double incomeRateA = StockManager.getIncomeRateByPERatio(1.5f, 0.06f, 1, 0.06f, 5, 20, 20);
        double incomeRateB = StockManager.getIncomeRateByPERatio(1.5f, 0.06f, 1, 0.06f, 5, 20, 15);
        double incomeRateC = StockManager.getIncomeRateByPERatio(1.5f, 0.06f, 1, 0.06f, 5, 20, 25);
        LogUtil.v("A = " + incomeRateA + ", B = " + incomeRateB + ", C = " + incomeRateC);
        assertEquals(0.088, incomeRateA, 0.001);
        assertEquals(0.036, incomeRateB, 0.001);
        assertEquals(0.132, incomeRateC, 0.001);
    }

    @Test
    public void testAssessmentValuation() {
        double valueA10 = StockManager.discountRateModel(1, 0.09, 10);
        double valueA20 = StockManager.discountRateModel(1, 0.09, 20);
        double valueAN = StockManager.discountRateModel(1, 0.09, 0.001);
        LogUtil.v("A10 = " + valueA10 + ", A20 = " + valueA20 + ", AN = " + valueAN);

        double valueB10 = StockManager.discountRateModel(1, 0.07, 10);
        double valueB20 = StockManager.discountRateModel(1, 0.07, 20);
        double valueBN = StockManager.discountRateModel(1, 0.07, 0.001);
        LogUtil.v("B10 = " + valueB10 + ", B20 = " + valueB20 + ", BN = " + valueBN);

        double valueC10 = StockManager.discountRateModel(1, 0.03, 10);
        double valueC20 = StockManager.discountRateModel(1, 0.03, 20);
        double valueCN = StockManager.discountRateModel(1, 0.03, 0.001);
        LogUtil.v("C10 = " + valueC10 + ", C20 = " + valueC20 + ", CN = " + valueCN);

        AssessmentValuation.Result resultA = StockManager.discountRateModel(630_00, 0.05, 0.09, 0.03, 10, 221_00);
        LogUtil.v("predict price = " + resultA.getStockPredictPrice());
        assertEquals(54.30, resultA.getStockPredictPrice(), 0.01);
    }
}
