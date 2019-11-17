package com.yline.formula.utils;

import java.util.Calendar;

/**
 * 时间相关类
 *
 * @author yline 2019-11-17 -- 14:52
 */
public class TimeUtil {
    /**
     * 获取 某年某月 的天数
     *
     * @param year  年份
     * @param month 月份
     * @return 天数
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取 某年 的天数
     *
     * @param year 年份
     * @return 天数
     */
    public static int getDaysOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
    }
}
