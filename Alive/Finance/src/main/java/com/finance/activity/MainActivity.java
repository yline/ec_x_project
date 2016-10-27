package com.finance.activity;

import android.os.Bundle;
import android.view.View;

import com.finance.R;
import com.finance.helper.ComputeHelper;
import com.yline.base.BaseActivity;

public class MainActivity extends BaseActivity
{
	private ComputeHelper computeHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		computeHelper = new ComputeHelper();
		findViewById(R.id.btn_equal_alipay).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				computeHelper.calculateEqualMonthlyAlipay(10000, ComputeHelper.interestDay2Month(4.0f), 12);
			}
		});

		findViewById(R.id.btn_equal).setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				computeHelper.calculateEqualMonthly(10000, ComputeHelper.interestDay2Month(4.0f), 12);
			}
		});

		findViewById(R.id.btn_first_alipay).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				computeHelper.calculateFirstInterestAlipay(10000, ComputeHelper.interestDay2Month(4.0f), 12);
			}
		});

		findViewById(R.id.btn_matching).setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				computeHelper.calculateMatching(10000, ComputeHelper.interestDay2Month(4.0f), 12);
			}
		});
	}
}
