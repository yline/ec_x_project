package com.calendar.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.calendar.R;
import com.calendar.util.LunarCalendar;
import com.yline.base.BaseActivity;
import com.yline.log.LogFileUtil;

import java.util.Arrays;

public class MainActivity extends BaseActivity
{
	private TextView tvCalendar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();

		findViewById(R.id.btn_test_calendar).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String result = testCalendar();
				tvCalendar.setText(result);
			}
		});
	}
	
	private void initView()
	{
		tvCalendar = (TextView) findViewById(R.id.tv_calendar);
	}

	public String testCalendar()
	{
		// 打印常量
		LogFileUtil.v("MIN_YEAR = " + LunarCalendar.MIN_YEAR);
		LogFileUtil.v("MAX_YEAR = " + LunarCalendar.MAX_YEAR + "\n");

		// 测试打印 月份天数
		for (int i = 1; i < 13; i++)
		{
			LogFileUtil.v("2017-" + i + ": = " + LunarCalendar.daysInMonth(2017, i));
		}
		LogFileUtil.v("");

		// 测试正常转化:
		int[] day1 = LunarCalendar.lunarToSolar(2017, 1, 10, false);
		LogFileUtil.v("2017, 1, 10 阳历 = " + Arrays.toString(day1)); // 2017, 1, 10 阳历 = [2017, 2, 6]

		int[] day2 = LunarCalendar.solarToLunar(2017, 2, 6);
		LogFileUtil.v("2017, 2, 6 阴历 = " + Arrays.toString(day2) + "\n"); // 2017, 2, 6 阴历 = [2017, 1, 10, 0]

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("2020-4-7阴历,对应的阳历是:");

		// 测试不知是否是闰月时,转化 闰月
		int leapMonth = LunarCalendar.leapMonth(2020); // 获取 润的月份
		int[] day3 = LunarCalendar.lunarToSolar(2020, 4, 7, false); // 假设没有 润,获取 该月份
		boolean isLeap = (leapMonth == day3[1]);
		LogFileUtil.v(isLeap ? "当月是闰月" : "当月不是闰月");
		if (isLeap)
		{
			int[] day4 = LunarCalendar.lunarToSolar(2020, 4, 7, true);
			LogFileUtil.v("day : " + Arrays.toString(day4));
			stringBuffer.append(Arrays.toString(day4));
		}
		else
		{
			LogFileUtil.v("day : " + Arrays.toString(day3));
			stringBuffer.append(Arrays.toString(day3));
		}

		return stringBuffer.toString();
	}
}
