package com.finance.util.finance;

import com.yline.log.LogFileUtil;

import java.util.Calendar;

/**
 * 支付宝-蚂蚁借呗-先息后本:
 * 1,采取类似常规的等额本金方式
 * 2,计算采取的是使用日利率为基础,而且以某个时间点为断点,计算的
 */
public class FirstInterestAlipay extends RepayCompute {
    /**
     * 每个月的断点时间
     */
    private static final int BREAKPOINT_TIME = 20;

    /**
     * 日利率
     */
    private float interestRateDayly;

    /**
     * 每月总时间
     */
    private int[] timeMonthly;

    @Override
    void calculate() {
        initRateDayly();
        initTimeMonthly();

        this.countTotal = 0;
        this.countTotalMonthly = new float[months];
        this.countPrincipalMonthly = new float[months];
        this.countInterestMonthly = new float[months];
        for (int i = 0; i < countTotalMonthly.length; i++) {
            if (i == countTotalMonthly.length - 1) {
                countPrincipalMonthly[i] = principal;
            } else {
                countPrincipalMonthly[i] = 0;
            }
            countInterestMonthly[i] = principal * interestRateDayly * timeMonthly[i];
            countTotalMonthly[i] = countPrincipalMonthly[i] + countInterestMonthly[i];
            this.countTotal += countTotalMonthly[i];
        }

        this.countInterest = countTotal - principal;
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
