package com.finance.util.finance;


import com.yline.log.LogFileUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * 贷款计算
 * 1,常规都是按照月利率计算的
 * 2,支付宝两种方式都是按照日利率计算的
 */
public abstract class RepayCompute {
    /**
     * 最大月份数
     */
    static final int MAX_MONTH = Calendar.getInstance().getMaximum(Calendar.MONTH) + 1;

    public boolean isDebug = true;

    // 传入参数

    /**
     * 贷款,本金,单元:元
     */
    int principal;

    /**
     * 贷款,月利率,单位:千分数
     */
    float interestRateMonthly;

    /**
     * 贷款,月数,单位:1
     */
    int months;

    /**
     * 每月还款,时间
     */
    String[] repayTimeMonthly;

    // 返回参数

    /**
     * 贷款,总额
     */
    float countTotal;

    /**
     * 贷款,利息
     */
    float countInterest;

    /**
     * 每月总额
     */
    float[] countTotalMonthly;

    /**
     * 每月利息
     */
    float[] countInterestMonthly;

    /**
     * 每月还款本金
     */
    float[] countPrincipalMonthly;

    private DecimalFormat decimalFormat = new DecimalFormat(".00");

    /**
     * 传入参数,用于计算所有回参
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public void init(int principal, float interestRateMonthly, int months) {
        if (isDebug) {
            LogFileUtil.v("principal = " + principal + ",interestRateMonthly = " + interestRateMonthly + ",months = " + months);
        }

        this.principal = principal;
        this.interestRateMonthly = interestRateMonthly * 1.0f / 1000;
        this.months = months;

        if (!isInputValid()) {
            LogFileUtil.e("RepayCompute", "isInputValid input = " + this.toString());
            return;
        }

        calculate();
    }

    /**
     * 计算所有回参
     */
    void calculate() {
        initRepayTimeMonthly();
    }

    /**
     * 计算还款时间
     */
    private void initRepayTimeMonthly() {
        repayTimeMonthly = new String[months];

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new java.util.Date(System.currentTimeMillis()));
        int tempYear = calendar.get(Calendar.YEAR);
        int tempMonth = calendar.get(Calendar.MONTH) + 1;
        int tempDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (isDebug) {
            LogFileUtil.v("tempYear = " + tempYear + ",tempMonth = " + tempMonth + ",tempDay = " + tempDay);
        }

        for (int i = 0; i < repayTimeMonthly.length; i++) {
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

            repayTimeMonthly[i] = formatRepayTimeMonthly(tempYear, tempMonth, tempDay);
        }
    }

    String formatRepayTimeMonthly(int year, int month, int day) {
        return String.format("%s-%s-%s", year, month, day);
    }

    /**
     * 判断输入参数是否合法
     */
    boolean isInputValid() {
        if (principal < 0) {
            return false;
        }

        if (interestRateMonthly < 0) {
            return false;
        }

        if (months <= 0) {
            return false;
        }

        return true;
    }

    /**
     * @return 还款时间
     */
    public String getRepayTimeMonthly(int i) {
        return repayTimeMonthly[i];
    }

    /**
     * @return 还款时间
     */
    public String[] getRepayTimeMonthly() {
        return repayTimeMonthly;
    }

    /**
     * 需要还款,本金+利息
     *
     * @return
     */
    public float getCountTotal() {
        return BigDecimal.valueOf(countTotal).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    /**
     * 需要还款,利息
     *
     * @return
     */
    public float getCountInterest() {
        return BigDecimal.valueOf(countInterest).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    /**
     * 每月还款总额,本金+利息
     *
     * @return
     */
    public float[] getCountTotalMonthly() {
        return countTotalMonthly;
    }

    /**
     * 每月还款总额,本金+利息
     *
     * @return
     */
    public float getCountTotalMonthly(int i) {
        if (i < 0 || i >= months) {
            return -1;
        }
        return BigDecimal.valueOf(countTotalMonthly[i]).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    /**
     * 每月还款,利息
     *
     * @return
     */
    public float[] getCountInterestMonthly() {
        return countInterestMonthly;
    }

    /**
     * 每月还款,利息
     *
     * @return
     */
    public float getCountInterestMonthly(int i) {
        if (i < 0 || i >= months) {
            return -1;
        }
        return BigDecimal.valueOf(countInterestMonthly[i]).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    /**
     * 每月还款,本金
     *
     * @return
     */
    public float[] getCountPrincipalMonthly() {
        return countPrincipalMonthly;
    }

    /**
     * 每月还款,本金
     *
     * @return
     */
    public float getCountPrincipalMonthly(int i) {
        if (i < 0 || i >= months) {
            return -1;
        }
        return BigDecimal.valueOf(countPrincipalMonthly[i]).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    @Override
    public String toString() {
        return "RepayCompute [principal=" + principal + ", interestRateMonthly=" + interestRateMonthly + ", months=" + months + "]";
    }
}
