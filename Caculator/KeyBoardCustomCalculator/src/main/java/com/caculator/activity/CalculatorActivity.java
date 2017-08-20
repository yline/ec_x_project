package com.caculator.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.caculator.R;
import com.caculator.fragment.CalculatorFragment;
import com.caculator.model.Calculator;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;

/**
 * 缺点：
 * 1,输入的框不是EditText;因此没法进行光标控制
 * 2,由于按键是自定义的;因此,可能会出现逻辑漏洞
 * 3,解析输入的字符串,成float类型,并未考虑转成int类型
 * 4,解析输入的字符串,如果解析前数字过大,则可能造成溢出的问题,程序提示输入数字错误,暂时用try catch处理掉了
 */
public class CalculatorActivity extends BaseAppCompatActivity implements CalculatorFragment.ICalculateCallback
{
	private FragmentManager fragmentManager;

	private CalculatorFragment calculatorFragment;

	private Calculator calculator;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);

		calculatorFragment = new CalculatorFragment();

		fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().add(R.id.ll_keyboard, calculatorFragment).commit();

		calculator = new Calculator();
	}

	@Override
	public String onResult(String first, String symbol)
	{
		LogFileUtil.v("2", first + symbol);

		return calculator.getResult(first, symbol);
	}

	@Override
	public String onResult(String first, String symbol, String second)
	{
		LogFileUtil.v("3", first + symbol + second);
		return calculator.getResult(first, symbol, second);
	}
}
