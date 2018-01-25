package com.finance.helper;

import com.yline.log.LogFileUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * 放弃，Android自己分析数据的能力太差；转投Python
 *
 * @author yline 2018/1/25 -- 11:33
 * @version 1.0.0
 */
public class ComputeHelper {
    /**
     * 日利率 转 月利率
     *
     * @param interestDay 单位:万分数
     * @return 单位:千分数
     */
    public static float interestDay2Month(float interestDay) {
        return interestDay * 30.0f / 10;
    }

    /**
     * 年利率 转 月利率
     *
     * @param interestYear 单位:百分数
     * @return 单位:千分数
     */
    public static float interestYear2Month(float interestYear) {
        return interestYear * 10.0f / 12;
    }

    /**
     * 月利率 转 年利率
     *
     * @param interestMonth 单位:千分数
     * @return 单位:百分数
     */
    public static float interestMonth2Year(float interestMonth) {
        return interestMonth * 12.0f / 10;
    }

    /**
     * 支付宝-蚂蚁借呗-每月等额
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public void calculateEqualMonthlyAlipay(int principal, float interestRateMonthly, int months) {
        LogFileUtil.v("支付宝-蚂蚁借呗-每月等额");
        RepayCompute equalMonthlyAlipay = new EqualMonthlyAlipay();
        equalMonthlyAlipay.init(principal, interestRateMonthly, months);
        log(equalMonthlyAlipay);
    }

    /**
     * 常规,等额本息
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public void calculateEqualMonthly(int principal, float interestRateMonthly, int months) {
        LogFileUtil.v("常规-等额本息");
        RepayCompute equalMonthly = new EqualMonthly();
        equalMonthly.init(principal, interestRateMonthly, months);
        log(equalMonthly);
    }

    /**
     * 支付宝-蚂蚁借呗-先息后本
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public void calculateFirstInterestAlipay(int principal, float interestRateMonthly, int months) {
        LogFileUtil.v("支付宝-蚂蚁借呗-先息后本");
        RepayCompute firstInterestAlipay = new FirstInterestAlipay();
        firstInterestAlipay.init(principal, interestRateMonthly, months);
        log(firstInterestAlipay);
    }

    /**
     * 常规,等额本金
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public void calculateMatching(int principal, float interestRateMonthly, int months) {
        LogFileUtil.v("常规-等额本金");
        RepayCompute matchingPrincipal = new MatchingPrincipal();
        matchingPrincipal.init(principal, interestRateMonthly, months);
        log(matchingPrincipal);
    }

    private void log(RepayCompute compute) {
        if (compute.isDebug) {
            LogFileUtil.v("compute toString = " + compute.toString());
            LogFileUtil.v("CountTotal = " + compute.getCountTotal());
            LogFileUtil.v("CountInterest = " + compute.getCountInterest());
            for (int i = 0; i < compute.getCountTotalMonthly().length; i++) {
                LogFileUtil.v("RepayTimeMonthly = " + compute.getRepayTimeMonthly(i) + ",CountTotalMonthly = " + compute.getCountTotalMonthly(i) + ",CountPrincipalMonthly = " + compute.getCountPrincipalMonthly(i) + ",CountInterestMonthly = " + compute.getCountInterestMonthly(i));
            }
        }
    }
}

/**
 * 贷款计算
 * 1,常规都是按照月利率计算的
 * 2,支付宝两种方式都是按照日利率计算的
 */
abstract class RepayCompute {
    /**
     * 最大月份数
     */
    static final int MAX_MONTH = Calendar.getInstance().getMaximum(Calendar.MONTH) + 1;

    boolean isDebug = true;

    // 传入参数

    /**
     * 贷款,本金,单元:元
     */
    int principal;

    /**
     * 贷款,月利率,单位:千分数
     */
    float interestRateMonthly;

    /**
     * 贷款,月数,单位:1
     */
    int months;

    /**
     * 每月还款,时间
     */
    String[] repayTimeMonthly;

    // 返回参数

    /**
     * 贷款,总额
     */
    float countTotal;

    /**
     * 贷款,利息
     */
    float countInterest;

    /**
     * 每月总额
     */
    float[] countTotalMonthly;

    /**
     * 每月利息
     */
    float[] countInterestMonthly;

    /**
     * 每月还款本金
     */
    float[] countPrincipalMonthly;

    private DecimalFormat decimalFormat = new DecimalFormat(".00");

    /**
     * 传入参数,用于计算所有回参
     *
     * @param principal           本金,单元:元
     * @param interestRateMonthly 月利率,单位:千分数
     * @param months              月数,单位:1
     */
    public void init(int principal, float interestRateMonthly, int months) {
        if (isDebug) {
            LogFileUtil.v("principal = " + principal + ",interestRateMonthly = " + interestRateMonthly + ",months = " + months);
        }

        this.principal = principal;
        this.interestRateMonthly = interestRateMonthly * 1.0f / 1000;
        this.months = months;

        if (!isInputValid()) {
            LogFileUtil.e("RepayCompute", "isInputValid input = " + this.toString());
            return;
        }

        calculate();
    }

    /**
     * 计算所有回参
     */
    void calculate() {
        initRepayTimeMonthly();
    }

    /**
     * 计算还款时间
     */
    private void initRepayTimeMonthly() {
        repayTimeMonthly = new String[months];

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new java.util.Date(System.currentTimeMillis()));
        int tempYear = calendar.get(Calendar.YEAR);
        int tempMonth = calendar.get(Calendar.MONTH) + 1;
        int tempDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (isDebug) {
            LogFileUtil.v("tempYear = " + tempYear + ",tempMonth = " + tempMonth + ",tempDay = " + tempDay);
        }

        for (int i = 0; i < repayTimeMonthly.length; i++) {
            // 1 <= month <= 12; 移动月份
            if (tempMonth / MAX_MONTH != 0) {
                tempYear += 1;
                tempMonth = 1;
                calendar.clear();
                calendar.set(Calendar.YEAR, tempYear);
                calendar.set(Calendar.MONTH, (tempMonth - 1));
            } else {
                tempMonth += 1;
                calendar.clear();
                calendar.set(Calendar.MONTH, (tempMonth - 1));
            }

            repayTimeMonthly[i] = formatRepayTimeMonthly(tempYear, tempMonth, tempDay);
        }
    }

    String formatRepayTimeMonthly(int year, int month, int day) {
        return String.format("%s-%s-%s", year, month, day);
    }

    /**
     * 判断输入参数是否合法
     */
    boolean isInputValid() {
        if (principal < 0) {
            return false;
        }

        if (interestRateMonthly < 0) {
            return false;
        }

        if (months <= 0) {
            return false;
        }

        return true;
    }

    /**
     * @return 还款时间
     */
    public String getRepayTimeMonthly(int i) {
        return repayTimeMonthly[i];
    }

    /**
     * @return 还款时间
     */
    public String[] getRepayTimeMonthly() {
        return repayTimeMonthly;
    }

    /**
     * 需要还款,本金+利息
     *
     * @return
     */
    public float getCountTotal() {
        return BigDecimal.valueOf(countTotal).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    /**
     * 需要还款,利息
     *
     * @return
     */
    public float getCountInterest() {
        return BigDecimal.valueOf(countInterest).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    /**
     * 每月还款总额,本金+利息
     *
     * @return
     */
    public float[] getCountTotalMonthly() {
        return countTotalMonthly;
    }

    /**
     * 每月还款总额,本金+利息
     *
     * @return
     */
    public float getCountTotalMonthly(int i) {
        if (i < 0 || i >= months) {
            return -1;
        }
        return BigDecimal.valueOf(countTotalMonthly[i]).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    /**
     * 每月还款,利息
     *
     * @return
     */
    public float[] getCountInterestMonthly() {
        return countInterestMonthly;
    }

    /**
     * 每月还款,利息
     *
     * @return
     */
    public float getCountInterestMonthly(int i) {
        if (i < 0 || i >= months) {
            return -1;
        }
        return BigDecimal.valueOf(countInterestMonthly[i]).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    /**
     * 每月还款,本金
     *
     * @return
     */
    public float[] getCountPrincipalMonthly() {
        return countPrincipalMonthly;
    }

    /**
     * 每月还款,本金
     *
     * @return
     */
    public float getCountPrincipalMonthly(int i) {
        if (i < 0 || i >= months) {
            return -1;
        }
        return BigDecimal.valueOf(countPrincipalMonthly[i]).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    @Override
    public String toString() {
        return "RepayCompute [principal=" + principal + ", interestRateMonthly=" + interestRateMonthly + ", months=" + months + "]";
    }
}

/**
 * 支付宝-蚂蚁借呗-先息后本:
 * 1,采取类似常规的等额本金方式
 * 2,计算采取的是使用日利率为基础,而且以某个时间点为断点,计算的
 */
class FirstInterestAlipay extends RepayCompute {
    /**
     * 每个月的断点时间
     */
    private static final int BREAKPOINT_TIME = 20;

    /**
     * 日利率
     */
    private float interestRateDayly;

    /**
     * 每月总时间
     */
    private int[] timeMonthly;

    @Override
    void calculate() {
        initRateDayly();
        initTimeMonthly();

        this.countTotal = 0;
        this.countTotalMonthly = new float[months];
        this.countPrincipalMonthly = new float[months];
        this.countInterestMonthly = new float[months];
        for (int i = 0; i < countTotalMonthly.length; i++) {
            if (i == countTotalMonthly.length - 1) {
                countPrincipalMonthly[i] = principal;
            } else {
                countPrincipalMonthly[i] = 0;
            }
            countInterestMonthly[i] = principal * interestRateDayly * timeMonthly[i];
            countTotalMonthly[i] = countPrincipalMonthly[i] + countInterestMonthly[i];
            this.countTotal += countTotalMonthly[i];
        }

        this.countInterest = countTotal - principal;
    }

    /**
     * 通过月利率,计算得到日利率; 没有单位的转换
     */
    private void initRateDayly() {
        interestRateDayly = interestRateMonthly / 30.0f;
    }

    /**
     * 计算得到每月的时间长度
     */
    private void initTimeMonthly() {
        repayTimeMonthly = new String[months];
        timeMonthly = new int[months];

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new java.util.Date(System.currentTimeMillis()));
        int tempYear = calendar.get(Calendar.YEAR);
        int tempMonth = calendar.get(Calendar.MONTH) + 1;
        int tempDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (isDebug) {
            LogFileUtil.v("tempYear = " + tempYear + ",tempMonth = " + tempMonth + ",tempDay = " + tempDay);
        }

        for (int i = 0; i < timeMonthly.length; i++) {
            // 计算赋值,当前月份数,特殊处理2月份
            if (tempYear % 4 == 0 && tempMonth == 2 && tempYear % 100 != 0) {
                timeMonthly[i] = 29;
            } else {
                timeMonthly[i] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            // 第一月特殊计算
            if (i == 0) {
                if (tempDay >= BREAKPOINT_TIME) {
                    timeMonthly[i] = BREAKPOINT_TIME + (timeMonthly[i] - tempDay);
                } else {
                    timeMonthly[i] = BREAKPOINT_TIME - tempDay;
                }
            }

            if (isDebug) {
                LogFileUtil.v("tempYear = " + tempYear + ",tempMonth = " + tempMonth + ",timeMonthly[" + i + "] = " + timeMonthly[i]);
            }

            // 1 <= month <= 12; 移动月份
            if (tempMonth / MAX_MONTH != 0) {
                tempYear += 1;
                tempMonth = 1;
                calendar.clear();
                calendar.set(Calendar.YEAR, tempYear);
                calendar.set(Calendar.MONTH, (tempMonth - 1));
            } else {
                tempMonth += 1;
                calendar.clear();
                calendar.set(Calendar.MONTH, (tempMonth - 1));
            }

            repayTimeMonthly[i] = super.formatRepayTimeMonthly(tempYear, tempMonth, BREAKPOINT_TIME);
        }
    }
}

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
class MatchingPrincipal extends RepayCompute {

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

/**
 * 支付宝-蚂蚁借呗-每月等额:
 * 1,采取类似常规的等额本息方式
 * 2,计算采取的是 首月特殊计算,之后常规等额本息计算
 * 3,该方式计算存在偏差,大概1年多出2块(相对支付宝)
 */
class EqualMonthlyAlipay extends RepayCompute {
    /**
     * 每个月的断点时间
     */
    private static final int BREAKPOINT_TIME = 20;

    /**
     * 日利率
     */
    private double interestRateDayly;

    /**
     * 每月总时间
     */
    private int[] timeMonthly;

    @Override
    void calculate() {
        initRateDayly();
        initTimeMonthly();

        // 第一个月利息
        float firstMonthInterest = (float) (principal * interestRateDayly * timeMonthly[0]);

        // 剩余天数
        int resetMonthDayNumbers = 0;
        for (int i = 1; i < timeMonthly.length; i++) {
            resetMonthDayNumbers += timeMonthly[i];
        }

        // 先算出来 少1个月时, 每月还款数额
        double temp = BigDecimal.valueOf(1 + interestRateDayly).pow(resetMonthDayNumbers).doubleValue();
        float tempCountTotalDayly = (float) (principal * interestRateDayly * temp / (temp - 1));

        // 计算出总额 = (每月还款数额 * 少1个月时月数 + 第一个月利息)
        this.countTotal = tempCountTotalDayly * resetMonthDayNumbers + firstMonthInterest;
        this.countInterest = countTotal - principal;

        // 再平均,计算出每月还款数额
        float tempCountTotalMonthly = countTotal / months;

        if (isDebug) {
            LogFileUtil.v("temp = " + temp + ",firstMonthInterest = " + firstMonthInterest + ",tempCountTotalMonthly = " + tempCountTotalMonthly + ",resetMonthDayNumbers = " + resetMonthDayNumbers);
        }

        // 再计算出,每个月的 本金和利息分配
        /** 已还本金 */
        float tempRepayedPrincipal = 0;

        this.countTotalMonthly = new float[months];
        this.countPrincipalMonthly = new float[months];
        this.countInterestMonthly = new float[months];
        for (int i = 0; i < countTotalMonthly.length; i++) {
            this.countTotalMonthly[i] = tempCountTotalMonthly;
            if (i == 0) {
                this.countInterestMonthly[i] = firstMonthInterest;
            } else {
                this.countInterestMonthly[i] = (principal - tempRepayedPrincipal) * interestRateMonthly;
            }
            this.countPrincipalMonthly[i] = this.countTotalMonthly[i] - this.countInterestMonthly[i];

            tempRepayedPrincipal += this.countPrincipalMonthly[i];
        }
    }

    /**
     * 通过月利率,计算得到日利率; 没有单位的转换
     */
    private void initRateDayly() {
        interestRateDayly = interestRateMonthly / 30.0f;
    }

    /**
     * 计算得到每月的时间长度
     */
    private void initTimeMonthly() {
        repayTimeMonthly = new String[months];
        timeMonthly = new int[months];

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new java.util.Date(System.currentTimeMillis()));
        int tempYear = calendar.get(Calendar.YEAR);
        int tempMonth = calendar.get(Calendar.MONTH) + 1;
        int tempDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (isDebug) {
            LogFileUtil.v("tempYear = " + tempYear + ",tempMonth = " + tempMonth + ",tempDay = " + tempDay);
        }

        for (int i = 0; i < timeMonthly.length; i++) {
            // 计算赋值,当前月份数,特殊处理2月份
            if (tempYear % 4 == 0 && tempMonth == 2 && tempYear % 100 != 0) {
                timeMonthly[i] = 29;
            } else {
                timeMonthly[i] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            // 第一月特殊计算
            if (i == 0) {
                if (tempDay >= BREAKPOINT_TIME) {
                    timeMonthly[i] = BREAKPOINT_TIME + (timeMonthly[i] - tempDay);
                } else {
                    timeMonthly[i] = BREAKPOINT_TIME - tempDay;
                }
            }

            if (isDebug) {
                LogFileUtil.v("tempYear = " + tempYear + ",tempMonth = " + tempMonth + ",timeMonthly[" + i + "] = " + timeMonthly[i]);
            }

            // 1 <= month <= 12; 移动月份
            if (tempMonth / MAX_MONTH != 0) {
                tempYear += 1;
                tempMonth = 1;
                calendar.clear();
                calendar.set(Calendar.YEAR, tempYear);
                calendar.set(Calendar.MONTH, (tempMonth - 1));
            } else {
                tempMonth += 1;
                calendar.clear();
                calendar.set(Calendar.MONTH, (tempMonth - 1));
            }

            repayTimeMonthly[i] = super.formatRepayTimeMonthly(tempYear, tempMonth, BREAKPOINT_TIME);
        }
    }
}

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
class EqualMonthly extends RepayCompute {
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
