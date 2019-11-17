package com.yline.formula.module.finance;

import com.yline.formula.utils.MoneyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 等额本息（月利率为基准）：每个月的还款金额相同
 * <p>
 * 假设本金A，利息β，每月还款X，n个月
 * fun(0) = A
 * fun(1) = fun(0)*(1+β) - X
 * fun(k) = fun(k-1)*(1+β) - X
 * fun(n) = 0
 * <p>
 * f(n) = fun(0)*(1+β)^n - X*[1 + (1+β)^1 + ... + (1+β)^(n-1)]
 * X = [A * (1+β)^n * β] / [(1+β)^n - 1]
 *
 * @author yline 2019-11-17 -- 16:31
 */
public class SameInterestMonthly {

    /**
     * 等额本息，计算公式
     *
     * @param principal 本金，单位：分
     * @param rate      月利率，单位：百分比
     * @param terms     期限，总月数
     */
    public static Result calculate(int principal, float rate, int terms) {
        Result result = new Result(terms);

        // 计算每个月还款总额
        double monthRate = rate / 100;
        double termsFact = Math.pow(1 + monthRate, terms);
        double X = principal * termsFact * monthRate / (termsFact - 1);

        // 计算还款总额
        double totalSum = X * terms;
        int totalAmount = MoneyUtil.fen2fen(totalSum);
        result.totalAmount = totalAmount;

        // 每月还款总额，分为单位，这里会产生误差
        int monthAmount = MoneyUtil.fen2fen(X);

        // 计算，由于每月还款转为分，导致的误差金额
        result.totalAmountDiff = totalAmount - monthAmount * terms;

        // 计算，还款总利息
        result.totalInterest = totalAmount - principal;

        // 计算，每个月的具体情况
        int lastPrincipal = principal;
        for (int i = 0; i < terms; i++) {
            int monthInterest = MoneyUtil.fen2fen(lastPrincipal * monthRate);
            int monthPrincipal = monthAmount - monthInterest;

            lastPrincipal -= monthPrincipal;
            result.addMonth(monthInterest, monthPrincipal, monthAmount, lastPrincipal);
        }

        return result;
    }

    public static class Result {
        private final int terms; // 总月数

        private int totalAmountDiff; // 每月的还款(分)，和总金额(分)，计算得到的误差，单位：分

        private int totalAmount; // 还款总本金 + 还款总利息，单位：分
        private int totalInterest; // 还款总利息，单位：分
        private final List<Month> dataList; // 每月数据

        private Result(int terms) {
            this.terms = terms;
            this.dataList = new ArrayList<>(terms);
        }

        private void addMonth(int interest, int principal, int amount, int restPrincipal) {
            dataList.add(new Month(interest, principal, amount, restPrincipal));
        }

        /**
         * @param index 第一月 - 0
         * @return 每月数据
         */
        public Month getMonth(int index) {
            if (index < 0 || index >= terms) {
                return null;
            }
            return dataList.get(index);
        }

        /**
         * 还款总本金 + 还款总利息，单位：分
         *
         * @return 还款总本金 + 还款总利息
         */
        public int getTotalAmount() {
            return totalAmount;
        }

        /**
         * 还款总利息，单位：分
         *
         * @return 还款总利息
         */
        public int getTotalInterest() {
            return totalInterest;
        }

        /**
         * 还款总额，和每月还款的乘积，的差额，单位：分
         *
         * @return 误差
         */
        public int getTotalAmountDiff() {
            return totalAmountDiff;
        }
    }

    public static class Month {
        private int interest; // 每月利息，单位：分
        private int principal; // 每月本金，单位：分
        private int amount; // 每月 利息+本金，单位：分
        private int restPrincipal; // 每个月剩余待还款额度(本金余额)，单位：分

        private Month(int interest, int principal, int amount, int restPrincipal) {
            this.interest = interest;
            this.principal = principal;
            this.amount = amount;
            this.restPrincipal = restPrincipal;
        }

        /**
         * @return 每月利息，单位：分
         */
        public int getInterest() {
            return interest;
        }

        /**
         * @return 每月本金，单位：分
         */
        public int getPrincipal() {
            return principal;
        }

        /**
         * @return 每月 利息+本金，单位：分
         */
        public int getAmount() {
            return amount;
        }

        /**
         * @return 每个月剩余待还款额度，单位：分
         */
        public int getRestPrincipal() {
            return restPrincipal;
        }
    }
}
