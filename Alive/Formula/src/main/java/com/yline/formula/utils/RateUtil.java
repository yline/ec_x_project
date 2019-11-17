package com.yline.formula.utils;

/**
 * 计算利率统一提供类
 *
 * @author yline 2019-11-17 -- 14:42
 */
public class RateUtil {

    /**
     * 日利率 转 月利率
     *
     * @param dayInterest 日利率，单位：万分数
     * @return 月利率，单位：千分数
     */
    public static float day2Month(float dayInterest) {
        return day2MonthInner(dayInterest, 30);
    }

    /**
     * 日利率 转 月利率
     *
     * @param dayInterest 日利率，单位：万分数
     * @param year        年份
     * @param month       月份
     * @return 月利率，单位：千分数
     */
    public static float day2Month(float dayInterest, int year, int month) {
        int daysOfMonth = TimeUtil.getDaysOfMonth(year, month);
        return day2MonthInner(dayInterest, daysOfMonth);
    }

    /**
     * 日利率 转 月利率
     *
     * @param dayInterest 日利率，单位：万分数
     * @param daysOfMonth 当月天数
     * @return 月利率，单位：千分数
     */
    private static float day2MonthInner(float dayInterest, int daysOfMonth) {
        return dayInterest * daysOfMonth / 10;
    }

    /**
     * 日利率 转 年利率
     *
     * @param dayInterest 日利率，单位：万分数
     * @return 年利率，单位：百分数
     */
    public static float day2Year(float dayInterest) {
        return day2YearInner(dayInterest, 360);
    }

    /**
     * 日利率 转 年利率
     *
     * @param dayInterest 日利率，单位：万分数
     * @param year        年份
     * @return 年利率，单位：百分数
     */
    public static float day2Year(float dayInterest, int year) {
        int dayOfYear = TimeUtil.getDaysOfYear(year);
        return day2YearInner(dayInterest, dayOfYear);
    }

    /**
     * 日利率 转 年利率
     *
     * @param dayInterest 日利率，单位：万分数
     * @param dayOfYear   当年天数
     * @return 年利率，单位：百分数
     */
    private static float day2YearInner(float dayInterest, int dayOfYear) {
        return dayInterest * dayOfYear / 100;
    }

    /**
     * 年利率 转 月利率
     *
     * @param yearInterest 年利率，单位：百分数
     * @return 月利率，单位：百分数
     */
    public static float year2Month(float yearInterest) {
        return yearInterest / 12;
    }

}
