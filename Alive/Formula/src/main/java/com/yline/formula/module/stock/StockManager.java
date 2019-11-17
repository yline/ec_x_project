package com.yline.formula.module.stock;

/**
 * 1,
 *
 * @author yline 2019-11-17 -- 19:40
 */
public class StockManager {
    /**
     * @param operateCashFlow       经营性现金流，单位：分
     * @param capitalOutlayCashFlow 资本支出现金流，单位：分
     * @return 自由现金流，单位：分
     */
    public static int getFreeCashFlow(int operateCashFlow, int capitalOutlayCashFlow) {
        return operateCashFlow - capitalOutlayCashFlow;
    }

    /**
     * 每1美元，销售收入，能产生多少利润
     *
     * @param netIncome   净收入，单位：分
     * @param salesIncome 销售收入，单位：分
     * @return 净利润，单位：元
     */
    public static float getNetProfits(int netIncome, int salesIncome) {
        return netIncome * 1.0f / salesIncome;
    }

    /**
     * @param netIncome   净收入，单位：分
     * @param ownerEquity 所有者权益，单位：分
     * @return 净资产收益率
     */
    public static float getReturnOnEquity(int netIncome, int ownerEquity) {
        return netIncome * 1.0f / ownerEquity;
    }

    /**
     * @param netIncome     净收入，单位：分
     * @param companyEquity 公司全部资产，单位：分
     * @return 资产收益率
     */
    public static float getReturnOnAssets(int netIncome, int companyEquity) {
        return netIncome * 1.0f / companyEquity;
    }

    /**
     * n年后的收益 = (1 + n年的收益率)^n
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
    public static double getIncomeRateByPERatio(float profit, float profitGrow, float bonus, float bonusGrow, int year, double nowPERatio, double afterPERatio) {
        return PriceValuation.getIncomeRateByPERatio(profit, profitGrow, bonus, bonusGrow, year, nowPERatio, afterPERatio);
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
    public static double getIncomeByPERatio(float profit, float profitGrow, float bonus, float bonusGrow, int year, double nowPERatio, double afterPERatio) {
        return PriceValuation.getIncomeByPERatio(profit, profitGrow, bonus, bonusGrow, year, nowPERatio, afterPERatio);
    }

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
        return AssessmentValuation.discountRateModel(rate, year);
    }


    /**
     * 折现率模型
     *
     * @param rate   折现率
     * @param border 当每年的现金流，相对于今年的比率小于该值时，不再执行
     * @return 最终的倍率
     */
    public static double discountRateModel(double rate, double border) {
        return AssessmentValuation.discountRateModel(rate, border);
    }
}
