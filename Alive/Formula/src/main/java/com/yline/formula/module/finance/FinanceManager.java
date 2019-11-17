package com.yline.formula.module.finance;

/**
 * 1，等额本息
 * 2，等额本金
 *
 * @author yline 2019-11-17 -- 18:02
 */
public class FinanceManager {
    /**
     * 等额本息，计算公式
     *
     * @param principal 本金，单位：分
     * @param monthRate 月利率，单位：百分比
     * @param terms     期限，总月数
     */
    public static SameInterestMonthly.Result getSameInterestMonthly(int principal, float monthRate, int terms) {
        return SameInterestMonthly.calculate(principal, monthRate, terms);
    }

    /**
     * 等额本金，计算公式
     *
     * @param principal 本金，单位：分
     * @param monthRate 月利率，单位：百分比
     * @param terms     期限，总月数
     */
    public static SamePrincipalMonthly.Result getSamePrincipalMonthly(int principal, float monthRate, int terms) {
        return SamePrincipalMonthly.calculate(principal, monthRate, terms);
    }
}
