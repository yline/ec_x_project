package com.alive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

/**
 * @author YLine 2016/8/21 --> 1:34
 * @version 1.0.0
 */
public class WelcomeActivity extends BaseTestActivity
{
	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		addButton("To B", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				BActivity.actionStart(WelcomeActivity.this);
			}
		});
	}
	/*@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
	}*/

	public static void actionStart(Context context)
	{
	   context.startActivity(new Intent(context, WelcomeActivity.class));
	}
}

