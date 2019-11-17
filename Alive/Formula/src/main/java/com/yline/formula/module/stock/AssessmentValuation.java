package com.yline.formula.module.stock;

/**
 * 通过内在价值估值
 *
 * @author yline 2019-11-17 -- 21:41
 */
public class AssessmentValuation {

    /**
     * 折现率模型
     * 第0年，1元
     * 第1年，1/(1+rate)元
     * 第2年，1/(1+rate)^2元
     * 第n年，1/(1+rate)^n元
     * 则求和
     *
     * @param rate 折现率
     * @param year 年份
     * @return n年后的倍率
     */
    public static double discountRateModel(double rate, int year) {
        double sum = 1;
        double thisOne = 1;
        for (int i = 0; i < year; i++) {
            thisOne /= (1 + rate);
            sum += thisOne;
        }
        return sum;
    }

    /**
     * 折现率模型
     *
     * @param rate   折现率
     * @param border 当每年的现金流，相对于今年的比率小于该值时，不再执行
     * @return 最终的倍率
     */
    public static double discountRateModel(double rate, double border) {
        double sum = 1;
        double thisOne = 1;
        while (thisOne > border) {
            thisOne /= (1 + rate);
            sum += thisOne;
        }
        return sum;
    }
}
