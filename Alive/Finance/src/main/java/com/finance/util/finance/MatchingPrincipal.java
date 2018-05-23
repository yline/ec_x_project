package com.finance.util.finance;

/**
 * 常规,等额本金
 * 1,贷款的还款方式
 * 2,还款期内,每月偿还等同的金额以及之前金额产生的利息
 * 计算公式:
 * 每月还款金额 = (贷款本金 / 还款月数) + (本金 - 已归还本金累计额) × 每月利率
 * 测试数据:
 * 1,贷款12万元，年利率4.86%，还款年限10年
 * 2,10年后还款149403.00元，总利息29403.00元
 */
public class MatchingPrincipal extends RepayCompute {
    @Override
    void calculate() {
        super.calculate();
        this.countTotal = 0;
        this.countTotalMonthly = new float[months];
        this.countPrincipalMonthly = new float[months];
        this.countInterestMonthly = new float[months];
        for (int i = 0; i < countTotalMonthly.length; i++) {
            countPrincipalMonthly[i] = principal * 1.0f / months;
            countInterestMonthly[i] = (principal - principal * i * 1.0f / months) * interestRateMonthly;
            countTotalMonthly[i] = countPrincipalMonthly[i] + countInterestMonthly[i];
            this.countTotal += countTotalMonthly[i];
        }

        this.countInterest = countTotal - principal;
    }
}
