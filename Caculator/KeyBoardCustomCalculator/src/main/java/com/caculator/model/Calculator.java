package com.caculator.model;

import android.text.TextUtils;

import com.caculator.application.IApplication;
import com.caculator.fragment.CalculatorFragment;

/**
 * Created by yline on 2017/1/9.
 */
public class Calculator
{
	public String getResult(String first, String symbol)
	{
		float result = 0;
		float firstParam = parseString(first);
		switch (symbol)
		{
			case CalculatorFragment.SYMBOL_RADICALS:
				result = (float) Math.pow(firstParam, 1.0 / 2);
				break;
			case CalculatorFragment.SYMBOL_SQUARE:
				result = (float) Math.pow(firstParam, 2);
				break;
		}

		return result + "";
	}

	public String getResult(String first, String symbol, String second)
	{
		float result = 0;
		float firstParam = parseString(first);
		float secondParam = parseString(second);
		switch (symbol)
		{
			case CalculatorFragment.SYMBOL_ADD:
				result = firstParam + secondParam;
				break;
			case CalculatorFragment.SYMBOL_REDUCE:
				result = firstParam - secondParam;
				break;
			case CalculatorFragment.SYMBOL_MULTIPLY:
				result = firstParam * secondParam;
				break;
			case CalculatorFragment.SYMBOL_DIVIDE:
				result = (float) (firstParam * 1.0 / secondParam);
				break;
		}

		return result + "";
	}

	private float parseString(String str)
	{
		if (TextUtils.isEmpty(str))
		{
			return 0;
		}

		try
		{
			if (str.contains(CalculatorFragment.SYMBOL_POINT_USE))
			{
				return Float.parseFloat(str);
			}
			else
			{
				return Integer.parseInt(str);
			}
		}
		catch (Exception ex)
		{
			IApplication.toast("Parse Input Number Error，please calculate again");
			return 0;
		}
	}
}
