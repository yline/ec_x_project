package com.yline.formula.module.finance;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.yline.utils.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FinanceManagerTest {

    @Test
    public void sameInterestMonthly() {
        SameInterestMonthly.Result resultA = FinanceManager.getSameInterestMonthly(1_0000_00, 6.55f / 12, 6);
        assertEquals(10191_91, resultA.getTotalAmount()); // 还款总额
        assertEquals(191_91, resultA.getTotalInterest()); // 利息总额
        assertEquals(1698_65, resultA.getMonth(0).getAmount()); // 每月还款
        assertEquals(9_22, resultA.getMonth(5).getInterest()); // 最后一个月，月供利息
        assertEquals(0, resultA.getMonth(5).getRestPrincipal()); // 最后一个月，本金余额
        LogUtil.v("A 分月导致的误差：" + resultA.getTotalAmountDiff() + "分");

        SameInterestMonthly.Result resultB = FinanceManager.getSameInterestMonthly(70_0000_00, 4.9f / 12, 240);
        assertEquals(109_9466_00, resultB.getTotalAmount(), 2); // 还款总额，误差2分
        assertEquals(39_9466_00, resultB.getTotalInterest(), 2); // 利息总额，误差2分
        assertEquals(4581_11, resultB.getMonth(0).getAmount()); // 每月还款
        assertEquals(18_63, resultB.getMonth(239).getInterest()); // 最后一个月，月供利息
        assertEquals(0, resultB.getMonth(239).getRestPrincipal(), 64); // 最后一个月，本金余额，误差-64分
        LogUtil.v("B 分月导致的误差：" + resultB.getTotalAmountDiff() + "分");
    }

    @Test
    public void samePrincipalMonthly() {
        SamePrincipalMonthly.Result resultA = FinanceManager.getSamePrincipalMonthly(1_0000_00, 6.55f / 12, 6);
        assertEquals(10191_04, resultA.getTotalAmount()); // 还款总额
        assertEquals(191_04, resultA.getTotalInterest()); // 利息总额
        assertEquals(1666_67, resultA.getMonth(0).getPrincipal()); // 每月还款
        assertEquals(9_10, resultA.getMonth(5).getInterest()); // 最后一个月，月供利息
        assertEquals(0, resultA.getMonth(5).getRestPrincipal(),2); // 最后一个月，本金余额

        SamePrincipalMonthly.Result resultB = FinanceManager.getSamePrincipalMonthly(70_0000_00, 4.9f / 12, 240);
        assertEquals(104_4429_17, resultB.getTotalAmount(), 137); // 还款总额，误差37分
        assertEquals(34_4429_17, resultB.getTotalInterest(), 37); // 利息总额，误差37分
        assertEquals(2916_67, resultB.getMonth(0).getPrincipal()); // 每月还款
        assertEquals(11_91, resultB.getMonth(239).getInterest()); // 最后一个月，月供利息
        assertEquals(0, resultB.getMonth(239).getRestPrincipal(), 80); // 最后一个月，本金余额，误差-80分
    }
}
