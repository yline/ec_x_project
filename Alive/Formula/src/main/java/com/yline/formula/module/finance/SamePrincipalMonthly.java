package com.yline.formula.module.finance;

import com.yline.formula.utils.MoneyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 等额本金：每个月还款相同的本金
 * <p>
 * 假设本金A，利息β，每月还款X，n个月
 * X = A / n
 *
 * @author yline 2019-11-17 -- 18:37
 */
public class SamePrincipalMonthly {
    /**
     * 等额本金，计算公式
     *
     * @param principal 本金，单位：分
     * @param rate      月利率，单位：百分比
     * @param terms     期限，总月数
     */
    public static Result calculate(int principal, float rate, int terms) {
        Result result = new Result(terms);

        // 计算月利率
        double monthRate = rate / 100;

        int totalInterest = 0;

        // 计算每个月，还的本金
        int monthPrincipal = MoneyUtil.fen2fen(principal * 1.0f / terms);
        int lastPrincipal = principal;
        for (int i = 0; i < terms; i++) {
            int monthInterest = MoneyUtil.fen2fen(lastPrincipal * monthRate);
            lastPrincipal -= monthPrincipal;
            int monthAmount = monthInterest + monthPrincipal;

            result.addMonth(monthInterest, monthPrincipal, monthAmount, lastPrincipal);

            totalInterest += monthInterest;
        }

        result.totalInterest = totalInterest;
        result.totalAmount = (totalInterest + principal);

        return result;
    }

    public static class Result {
        private final int terms;
        private final List<Month> dataList;

        private int totalAmount; // 还款总本金 + 还款总利息，单位：分
        private int totalInterest; // 还款总利息，单位：分

        public Result(int terms) {
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
