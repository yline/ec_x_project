package com.finance.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.finance.R;
import com.finance.helper.ComputeHelper;
import com.yline.log.LogFileUtil;

public class MainActivity extends AppCompatActivity
{
	private ComputeHelper computeHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		computeHelper = new ComputeHelper();
		findViewById(R.id.btn_compute_equal).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_compute_equal");
				computeHelper.testEqual(12 * 10000, 4.86f / 100 / 12, 10 * 12);
			}
		});
		findViewById(R.id.btn_compute_matching).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogFileUtil.v("btn_compute_matching");
				computeHelper.testMatching(12 * 10000, 4.86f / 100 / 12, 10 * 12);
			}
		});
	}
}
