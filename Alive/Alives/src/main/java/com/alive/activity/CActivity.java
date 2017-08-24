package com.alive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class CActivity extends BaseTestActivity
{
	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		addButton("back To Welcome", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				WelcomeActivity.actionStart(CActivity.this);
			}
		});
	}

	public static void actionStart(Context context)
	{
	   context.startActivity(new Intent(context, CActivity.class));
	}
}
