package com.finance.util.finance;

import com.yline.log.LogFileUtil;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 支付宝-蚂蚁借呗-每月等额:
 * 1,采取类似常规的等额本息方式
 * 2,计算采取的是 首月特殊计算,之后常规等额本息计算
 * 3,该方式计算存在偏差,大概1年多出2块(相对支付宝)
 */
public class EqualMonthlyAlipay extends RepayCompute {
    /**
     * 每个月的断点时间
     */
    private static final int BREAKPOINT_TIME = 20;

    /**
     * 日利率
     */
    private double interestRateDayly;

    /**
     * 每月总时间
     */
    private int[] timeMonthly;

    @Override
    void calculate() {
        initRateDayly();
        initTimeMonthly();

        // 第一个月利息
        float firstMonthInterest = (float) (principal * interestRateDayly * timeMonthly[0]);

        // 剩余天数
        int resetMonthDayNumbers = 0;
        for (int i = 1; i < timeMonthly.length; i++) {
            resetMonthDayNumbers += timeMonthly[i];
        }

        // 先算出来 少1个月时, 每月还款数额
        double temp = BigDecimal.valueOf(1 + interestRateDayly).pow(resetMonthDayNumbers).doubleValue();
        float tempCountTotalDayly = (float) (principal * interestRateDayly * temp / (temp - 1));

        // 计算出总额 = (每月还款数额 * 少1个月时月数 + 第一个月利息)
        this.countTotal = tempCountTotalDayly * resetMonthDayNumbers + firstMonthInterest;
        this.countInterest = countTotal - principal;

        // 再平均,计算出每月还款数额
        float tempCountTotalMonthly = countTotal / months;

        if (isDebug) {
            LogFileUtil.v("temp = " + temp + ",firstMonthInterest = " + firstMonthInterest + ",tempCountTotalMonthly = " + tempCountTotalMonthly + ",resetMonthDayNumbers = " + resetMonthDayNumbers);
        }

        // 再计算出,每个月的 本金和利息分配
        /** 已还本金 */
        float tempRepayedPrincipal = 0;

        this.countTotalMonthly = new float[months];
        this.countPrincipalMonthly = new float[months];
        this.countInterestMonthly = new float[months];
        for (int i = 0; i < countTotalMonthly.length; i++) {
            this.countTotalMonthly[i] = tempCountTotalMonthly;
            if (i == 0) {
                this.countInterestMonthly[i] = firstMonthInterest;
            } else {
                this.countInterestMonthly[i] = (principal - tempRepayedPrincipal) * interestRateMonthly;
            }
            this.countPrincipalMonthly[i] = this.countTotalMonthly[i] - this.countInterestMonthly[i];

            tempRepayedPrincipal += this.countPrincipalMonthly[i];
        }
    }

    /**
     * 通过月利率,计算得到日利率; 没有单位的转换
     */
    private void initRateDayly() {
        interestRateDayly = interestRateMonthly / 30.0f;
    }

    /**
     * 计算得到每月的时间长度
     */
    private void initTimeMonthly() {
        repayTimeMonthly = new String[months];
        timeMonthly = new int[months];

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new java.util.Date(System.currentTimeMillis()));
        int tempYear = calendar.get(Calendar.YEAR);
        int tempMonth = calendar.get(Calendar.MONTH) + 1;
        int tempDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (isDebug) {
            LogFileUtil.v("tempYear = " + tempYear + ",tempMonth = " + tempMonth + ",tempDay = " + tempDay);
        }

        for (int i = 0; i < timeMonthly.length; i++) {
            // 计算赋值,当前月份数,特殊处理2月份
            if (tempYear % 4 == 0 && tempMonth == 2 && tempYear % 100 != 0) {
                timeMonthly[i] = 29;
            } else {
                timeMonthly[i] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            // 第一月特殊计算
            if (i == 0) {
                if (tempDay >= BREAKPOINT_TIME) {
                    timeMonthly[i] = BREAKPOINT_TIME + (timeMonthly[i] - tempDay);
                } else {
                    timeMonthly[i] = BREAKPOINT_TIME - tempDay;
                }
            }

            if (isDebug) {
                LogFileUtil.v("tempYear = " + tempYear + ",tempMonth = " + tempMonth + ",timeMonthly[" + i + "] = " + timeMonthly[i]);
            }

            // 1 <= month <= 12; 移动月份
            if (tempMonth / MAX_MONTH != 0) {
                tempYear += 1;
                tempMonth = 1;
                calendar.clear();
                calendar.set(Calendar.YEAR, tempYear);
                calendar.set(Calendar.MONTH, (tempMonth - 1));
            } else {
                tempMonth += 1;
                calendar.clear();
                calendar.set(Calendar.MONTH, (tempMonth - 1));
            }

            repayTimeMonthly[i] = super.formatRepayTimeMonthly(tempYear, tempMonth, BREAKPOINT_TIME);
        }
    }
}
