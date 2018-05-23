package com.finance.util.finance;

import com.yline.log.LogFileUtil;

import java.math.BigDecimal;

/**
 * 常规,等额本息:
 * 1,贷款的还款方式
 * 2,还款期内,每月偿还同等数额的贷款(包含本金和利息)
 * 3,偏差,1年0.3块(相对易代宝)
 * 计算公式:
 * 每月还款数额 = [贷款本金 * 月利率 * [(1 + 月利率)^还款月数]] ÷ [[(1 + 月利率)^还款月数] - 1]
 * 测试数据:
 * 1,贷款12万元，年利率4.86%，还款年限10年
 * 2,10年后还款151750.36元，总利息31750.36元
 */
public class EqualMonthly extends RepayCompute {
    // 等额本息;公式应用
    @Override
    void calculate() {
        super.calculate();
        double temp = BigDecimal.valueOf(1 + interestRateMonthly).pow(months).doubleValue();
        float tempCountTotalMonthly = (float) (principal * interestRateMonthly * temp / (temp - 1));
        if (isDebug) {
            LogFileUtil.v("temp = " + temp);
        }

        /** 已还本金 */
        float tempRepayedPrincipal = 0;

        this.countTotalMonthly = new float[months];
        this.countPrincipalMonthly = new float[months];
        this.countInterestMonthly = new float[months];
        for (int i = 0; i < countTotalMonthly.length; i++) {
            this.countTotalMonthly[i] = tempCountTotalMonthly;

            this.countInterestMonthly[i] = (principal - tempRepayedPrincipal) * interestRateMonthly;
            this.countPrincipalMonthly[i] = this.countTotalMonthly[i] - this.countInterestMonthly[i];

            tempRepayedPrincipal += this.countPrincipalMonthly[i];
        }
        this.countTotal = tempCountTotalMonthly * months;
        this.countInterest = countTotal - principal;
    }
}
