package com.yline.formula.module.stock;

import java.util.ArrayList;
import java.util.List;

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
     * @param cashNum 当前现金流
     * @param rate    折现率
     * @param year    年份
     * @return n年后的倍率
     */
    public static double discountRateModel(double cashNum, double rate, int year) {
        double sum = 1;
        double thisOne = 1;
        for (int i = 0; i < year; i++) {
            thisOne /= (1 + rate);
            sum += thisOne;
        }
        return sum;
    }

    /**
     * 折现率模型，终值
     *
     * @param cashNum 当前现金流
     * @param rate    折现率
     * @param border  当每年的现金流，相对于今年的比率小于该值时，不再执行
     * @return 最终的倍率
     */
    public static double discountRateModel(double cashNum, double rate, double border) {
        double sum = 1;
        double thisOne = 1;
        while (thisOne > border) {
            thisOne /= (1 + rate);
            sum += thisOne;
        }
        return sum;
    }


    /**
     * 使用，折线现金流模型，对公司进行估值
     *
     * @param freeCashNum    下一年度自由现金流(财报报表)，单位：万
     * @param freeCashRate   自由现金流增长率，单位：1
     * @param countRate      每年折现率，单位：1
     * @param perpetuityRate 永续年金，增长率(参考国债增长率)，单位：1
     * @param year           年份，单位：1
     * @param stockCount     发行在外的股份数，单位：万
     * @return 股价估值，单位：1
     */
    public static Result discountRateModel(double freeCashNum, double freeCashRate, double countRate, double perpetuityRate, int year, double stockCount) {
        Result result = new Result(year);

        // 计算每年自由现金流、每年折现现金流
        double lastFreeCash = freeCashNum;
        for (int i = 0; i < year; i++) {
            double countCashRate = lastFreeCash / (Math.pow(1 + countRate, i + 1));
            result.addYear(lastFreeCash, countCashRate);

            lastFreeCash *= (1 + freeCashRate);
        }

        // 所有年份的，折现现金流总和
        double sumOfCountCash = 0;
        for (int i = 0; i < result.dataList.size(); i++) {
            sumOfCountCash += result.dataList.get(i).countFreeCash;
        }

        // 计算，永续年金价值；= CF(n) * (1 + perpetuityRate) / (countRate - perpetuityRate)
        double cFn = result.dataList.get(result.dataList.size() - 1).freeCash; // n年后的自由现金流
        double perpetuity = cFn * (1 + perpetuityRate) / (countRate - perpetuityRate);

        // 计算，永续年金对应的当前值
        double countPerpetuity = perpetuity / Math.pow(1 + countRate, year);

        // 计算，所有者权益
        double ownEquity = sumOfCountCash + countPerpetuity;

        // 计算，股价
        result.stockPredictcPrice = ownEquity / stockCount;

        return result;
    }

    public static class Result {
        private int year; // 总年数
        private List<Yearly> dataList; // 每年数据
        private double stockPredictcPrice;

        private Result(int year) {
            this.year = year;
            this.dataList = new ArrayList<>(year);
        }

        private void addYear(double freeCash, double countFreeCash) {
            dataList.add(new Yearly(freeCash, countFreeCash));
        }

        public int getYear() {
            return year;
        }

        public Yearly getYearly(int index) {
            return dataList.get(index);
        }

        public double getStockPredictPrice() {
            return stockPredictcPrice;
        }
    }

    public static class Yearly {
        private double freeCash; // 自由现金流，单位：万
        private double countFreeCash; // 折线自由现金流，单位：万

        private Yearly(double freeCash, double countFreeCash) {
            this.freeCash = freeCash;
            this.countFreeCash = countFreeCash;
        }

        public double getFreeCash() {
            return freeCash;
        }

        public void setFreeCash(double freeCash) {
            this.freeCash = freeCash;
        }

        public double getCountFreeCash() {
            return countFreeCash;
        }

        public void setCountFreeCash(double countFreeCash) {
            this.countFreeCash = countFreeCash;
        }
    }
}
