package com.finance.util;

import com.finance.util.finance.EqualMonthly;
import com.finance.util.finance.EqualMonthlyAlipay;
import com.finance.util.finance.FirstInterestAlipay;
import com.finance.util.finance.MatchingPrincipal;
import com.finance.util.finance.RepayCompute;
import com.yline.log.LogFileUtil;

/**
 * 计算工具类
 *
 * @author yline 2018/5/22 -- 19:31
 * @version 1.0.0
 */
public class FinanceUtils {
    /**
     * 日利率 转 月利率
     *
     * @param interestDay 单位:万分数
     * @return 单位:千分数
     */
    public static float interestDay2Month(float interestDay) {
        return interestDay * 30.0f / 10;
    }

    /**
     * 年利率 转 月利率
     *
     * @param interestYear 单位:百分数
     * @return 单位:千分数
     */
    public static float interestYear2Month(float interestYear) {
        return interestYear * 10.0f / 12;
    }

    /**
     * 月利率 转 年利率
     *
     * @param interestMonth 单位:千分数
     * @return 单位:百分数
     */
    public static float interestMonth2Year(float interestMonth) {
        return interestMonth * 12.0f / 10;
    }

    /**
     * 支付宝-蚂蚁借呗-每月等额
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public static void calculateEqualMonthlyAlipay(int principal, float interestRateMonthly, int months) {
        LogFileUtil.v("支付宝-蚂蚁借呗-每月等额");
        RepayCompute equalMonthlyAlipay = new EqualMonthlyAlipay();
        equalMonthlyAlipay.init(principal, interestRateMonthly, months);
        log(equalMonthlyAlipay);
    }

    /**
     * 常规,等额本息
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public static void calculateEqualMonthly(int principal, float interestRateMonthly, int months) {
        LogFileUtil.v("常规-等额本息");
        RepayCompute equalMonthly = new EqualMonthly();
        equalMonthly.init(principal, interestRateMonthly, months);
        log(equalMonthly);
    }

    /**
     * 支付宝-蚂蚁借呗-先息后本
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public static void calculateFirstInterestAlipay(int principal, float interestRateMonthly, int months) {
        LogFileUtil.v("支付宝-蚂蚁借呗-先息后本");
        RepayCompute firstInterestAlipay = new FirstInterestAlipay();
        firstInterestAlipay.init(principal, interestRateMonthly, months);
        log(firstInterestAlipay);
    }

    /**
     * 常规,等额本金
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public static void calculateMatching(int principal, float interestRateMonthly, int months) {
        LogFileUtil.v("常规-等额本金");
        RepayCompute matchingPrincipal = new MatchingPrincipal();
        matchingPrincipal.init(principal, interestRateMonthly, months);
        log(matchingPrincipal);
    }

    private static void log(RepayCompute compute) {
        if (compute.isDebug) {
            LogFileUtil.v("compute toString = " + compute.toString());
            LogFileUtil.v("CountTotal = " + compute.getCountTotal());
            LogFileUtil.v("CountInterest = " + compute.getCountInterest());
            for (int i = 0; i < compute.getCountTotalMonthly().length; i++) {
                LogFileUtil.v("RepayTimeMonthly = " + compute.getRepayTimeMonthly(i) + ",CountTotalMonthly = " + compute.getCountTotalMonthly(i) + ",CountPrincipalMonthly = " + compute.getCountPrincipalMonthly(i) + ",CountInterestMonthly = " + compute.getCountInterestMonthly(i));
            }
        }
    }
}
