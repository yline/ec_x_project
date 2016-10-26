package com.finance.helper;

import com.yline.log.LogFileUtil;

public class ComputeHelper
{
	public void testEqual(int principal, float interestRateMonthly, int months)
	{
		RepayCompute equalPrincipalAndInterest = new EqualPrincipalAndInterest();
		equalPrincipalAndInterest.init(principal, interestRateMonthly, months);
		log(equalPrincipalAndInterest);
	}

	public void testMatching(int principal, float interestRateMonthly, int months)
	{
		RepayCompute matchingPrincipal = new MatchingPrincipal();
		matchingPrincipal.init(principal, interestRateMonthly, months);
		log(matchingPrincipal);
	}

	private void log(RepayCompute compute)
	{
		LogFileUtil.v("compute toString = " + compute.toString());
		LogFileUtil.v("CountTotal = " + compute.getCountTotal());
		LogFileUtil.v("CountInterest = " + compute.getCountInterest());
		for (int i = 0; i < compute.getCountTotalMonthly().length; i++)
		{
			LogFileUtil.v("CountTotalMonthly = " + compute.getCountTotalMonthly(i) + ",CountPrincipalMonthly = " + compute.getCountPrincipalMonthly(i) + ",CountInterestMonthly = " + compute.getCountInterestMonthly(i));
		}
	}
}

/**
 * 贷款计算
 */
abstract class RepayCompute
{
	// 传入参数

	/** 贷款,本金,单元:元 */
	int principal;

	/** 贷款,月利率,单位:1 */
	float interestRateMonthly;

	/** 贷款,月数,单位:1 */
	int months;

	// 返回参数

	/** 贷款,总额 */
	float countTotal;

	/** 贷款,利息 */
	float countInterest;

	/** 每月总额 */
	float[] countTotalMonthly;

	/** 每月利息 */
	float[] countInterestMonthly;

	/** 每月还款本金 */
	float[] countPrincipalMonthly;

	/**
	 * 传入参数,用于计算所有回参
	 * @param principal           本金,单元:元
	 * @param interestRateMonthly 月利率,单位:1
	 * @param months              月数,单位:1
	 */
	public void init(int principal, float interestRateMonthly, int months)
	{
		LogFileUtil.v("principal = " + principal + ",interestRateMonthly = " + interestRateMonthly + ",months = " + months);
		this.principal = principal;
		this.interestRateMonthly = interestRateMonthly;
		this.months = months;

		if (!isInputValid())
		{
			LogFileUtil.e("RepayCompute", "isInputValid input = " + this.toString());
			return;
		}

		calculate();
	}

	/**
	 * 计算所有回参
	 */
	abstract void calculate();

	/**
	 * 判断输入参数是否合法
	 */
	boolean isInputValid()
	{
		if (principal < 0)
		{
			return false;
		}

		if (interestRateMonthly < 0)
		{
			return false;
		}

		if (months <= 0)
		{
			return false;
		}

		return true;
	}

	/**
	 * 需要还款,本金+利息
	 * @return
	 */
	public float getCountTotal()
	{
		return countTotal;
	}

	/**
	 * 需要还款,利息
	 * @return
	 */
	public float getCountInterest()
	{
		return countInterest;
	}

	/**
	 * 每月还款总额,本金+利息
	 * @return
	 */
	public float[] getCountTotalMonthly()
	{
		return countTotalMonthly;
	}

	/**
	 * 每月还款总额,本金+利息
	 * @return
	 */
	public float getCountTotalMonthly(int i)
	{
		if (i < 0 || i >= months)
		{
			return -1;
		}
		return countTotalMonthly[i];
	}

	/**
	 * 每月还款,利息
	 * @return
	 */
	public float[] getCountInterestMonthly()
	{
		return countInterestMonthly;
	}

	/**
	 * 每月还款,利息
	 * @return
	 */
	public float getCountInterestMonthly(int i)
	{
		if (i < 0 || i >= months)
		{
			return -1;
		}
		return countInterestMonthly[i];
	}

	/**
	 * 每月还款,本金
	 * @return
	 */
	public float[] getCountPrincipalMonthly()
	{
		return countPrincipalMonthly;
	}

	/**
	 * 每月还款,本金
	 * @return
	 */
	public float getCountPrincipalMonthly(int i)
	{
		if (i < 0 || i >= months)
		{
			return -1;
		}
		return countPrincipalMonthly[i];
	}

	@Override
	public String toString()
	{
		return "RepayCompute [principal=" + principal + ", interestRateMonthly=" + interestRateMonthly + ", months=" + months + "]";
	}
}

/**
 * 等额本金
 * 1,贷款的还款方式
 * 2,还款期内,每月偿还等同的金额以及之前金额产生的利息
 * 计算公式:
 * 每月还款金额 = (贷款本金 / 还款月数) + (本金 - 已归还本金累计额) × 每月利率
 * 测试数据:
 * 1,贷款12万元，年利率4.86%，还款年限10年
 * 2,10年后还款149403.00元，总利息29403.00元
 */
class MatchingPrincipal extends RepayCompute
{

	@Override
	void calculate()
	{
		this.countTotal = 0;
		this.countTotalMonthly = new float[months];
		this.countPrincipalMonthly = new float[months];
		this.countInterestMonthly = new float[months];
		for (int i = 0; i < countTotalMonthly.length; i++)
		{
			countPrincipalMonthly[i] = principal * 1.0f / months;
			countInterestMonthly[i] = (principal - principal * i * 1.0f / months) * interestRateMonthly;
			countTotalMonthly[i] = countPrincipalMonthly[i] + countInterestMonthly[i];
			this.countTotal += countTotalMonthly[i];
		}

		this.countInterest = countTotal - principal;
	}
}

/**
 * 等额本息:
 * 1,贷款的还款方式
 * 2,还款期内,每月偿还同等数额的贷款(包含本金和利息)
 * 计算公式:
 * 每月还款数额 = [贷款本金 * 月利率 * [(1 + 月利率)^还款月数]] ÷ [[(1 + 月利率)^还款月数] - 1]
 * 测试数据:
 * 1,贷款12万元，年利率4.86%，还款年限10年
 * 2,10年后还款151750.36元，总利息31750.36元
 */
class EqualPrincipalAndInterest extends RepayCompute
{
	/**
	 * 等额本息;公式应用
	 */
	@Override
	void calculate()
	{
		double temp = Math.pow((1 + interestRateMonthly), months);
		LogFileUtil.v("temp = " + temp);

		float tempCountTotalMonthly = (float) (principal * interestRateMonthly * temp / (temp - 1));

		this.countTotalMonthly = new float[months];
		this.countPrincipalMonthly = new float[months];
		this.countInterestMonthly = new float[months];
		for (int i = 0; i < countTotalMonthly.length; i++)
		{
			this.countTotalMonthly[i] = tempCountTotalMonthly;
			this.countPrincipalMonthly[i] = principal * 1.0f / months;
			this.countInterestMonthly[i] = this.countTotalMonthly[i] - this.countPrincipalMonthly[i];
		}
		countTotal = tempCountTotalMonthly * months;
		countInterest = countTotal - principal;
	}
}
