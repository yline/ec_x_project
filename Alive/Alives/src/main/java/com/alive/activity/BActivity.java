package com.alive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.test.BaseTestActivity;

public class BActivity extends BaseTestActivity
{
	@Override
	public void testStart(View view, Bundle savedInstanceState)
	{
		addButton("To C", new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				CActivity.actionStart(BActivity.this);
			}
		})	;
	}
	
	public static void actionStart(Context context)
	{
	   context.startActivity(new Intent(context, BActivity.class));
	}
}
