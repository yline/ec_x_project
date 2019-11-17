package com.yline.formula.module.stock;

/**
 * 依据股价进行估值
 *
 * @author yline 2019-11-17 -- 20:12
 */
public class PriceValuation {

    /**
     * n年后的收益率 = (当前股价 + n年后的收益) / 当前股价
     * n年后的收益率 = (1 + n年的平均收益率)^n
     *
     * @param profit       每股盈利
     * @param profitGrow   盈利增长率
     * @param bonus        当前分红
     * @param bonusGrow    分红增长率
     * @param year         经历年份
     * @param nowPERatio   当前的市盈率
     * @param afterPERatio n年后的市盈率
     * @return n年的收益率
     */
    static double getIncomeRateByPERatio(float profit, float profitGrow, float bonus, float bonusGrow, int year, double nowPERatio, double afterPERatio) {
        double income = getIncomeByPERatio(profit, profitGrow, bonus, bonusGrow, year, nowPERatio, afterPERatio);
        double incomeRate = (profit * nowPERatio + income) / (profit * nowPERatio);
        return Math.pow(incomeRate, 1.0f / year) - 1;
    }

    /**
     * n年后的收益 = (n年后的理论股价 - 当前股价) + n年后的分红总数
     *
     * @param profit       每股盈利
     * @param profitGrow   盈利增长率
     * @param bonus        当前分红
     * @param bonusGrow    分红增长率
     * @param year         经历年份
     * @param nowPERatio   当前的市盈率
     * @param afterPERatio n年后的市盈率
     * @return n年后的收益
     */
    static double getIncomeByPERatio(float profit, float profitGrow, float bonus, float bonusGrow, int year, double nowPERatio, double afterPERatio) {
        double nowStockPrice = profit * nowPERatio;
        double afterStockPrice = getPriceByPERatio(profit, profitGrow, year, afterPERatio);
        double bonusIncome = getBonusIncome(bonus, bonusGrow, year);
        return afterStockPrice - nowStockPrice + bonusIncome;
    }

    /**
     * n年后的理论股价 = n年后的理论盈利 * n年后的市盈率
     *
     * @param profit     每股盈利
     * @param profitGrow 盈利增长率
     * @param year       经历年份
     * @param peRatio    n年后的市盈率
     * @return n年后的理论股价
     */
    private static double getPriceByPERatio(float profit, float profitGrow, int year, double peRatio) {
        return recursiveProduct(profit, profitGrow, year) * peRatio;
    }

    /**
     * n年后的分红总数 = 第1年分红 + ... + 第n-1年分红
     *
     * @param bonus     当前分红
     * @param bonusGrow 分红增长率
     * @param year      经历年份
     * @return n年后的分红总数
     */
    private static double getBonusIncome(float bonus, float bonusGrow, int year) {
        return recursiveSum(bonus, bonusGrow, year);
    }

    /**
     * 递归乘积；每年增长固定比率，则n年后，数值为多少
     *
     * @param data 数据
     * @param grow 数据增长率
     * @param year 年份
     * @return n年后的数据
     */
    private static double recursiveProduct(float data, float grow, int year) {
        return data * Math.pow(1 + grow, year);
    }

    /**
     * 递归求和；每年增长固定比率，则n年后，积累了多少
     *
     * @param data 数据
     * @param grow 数据增长率
     * @param year 年份
     * @return n年后，共累计的数值
     */
    private static double recursiveSum(float data, float grow, int year) {
        return data * (Math.pow(1 + grow, year) - 1) / grow;
    }
}
