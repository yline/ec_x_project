package com.info.file.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.info.file.helper.FileLoadRunnable;
import com.yline.base.BaseActivity;

/**
 * Created by yline on 2017/1/27.
 */
public class TestActivity extends BaseActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		TextView textView = new TextView(this);
		textView.setText("fuck you");
		setContentView(textView);

		new Thread(new FileLoadRunnable()).start();
	}


}
