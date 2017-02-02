package com.info.file.activity;

import android.os.Bundle;
import android.view.View;

import com.info.file.R;
import com.yline.base.BaseAppCompatActivity;

/**
 * Created by yline on 2017/2/2.
 */
public class MainActivity extends BaseAppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_browse_file).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FileListActivity.actionStart(MainActivity.this);
			}
		});
	}
}
