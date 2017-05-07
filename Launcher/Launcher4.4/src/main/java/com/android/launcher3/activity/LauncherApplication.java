package com.android.launcher3.activity;

import com.android.launcher3.LauncherAppState;
import com.yline.application.BaseApplication;
import com.yline.log.LogFileUtil;

public class LauncherApplication extends BaseApplication
{
	private static final String TAG = "LauncherApplication";

	@Override
	public void onCreate()
	{
		super.onCreate();
		LogFileUtil.v(TAG, "create");
		LauncherAppState.setApplicationContext(this);
	}

	@Override
	public void onTerminate()
	{
		super.onTerminate();
		LauncherAppState.getInstance().onTerminate();
		LogFileUtil.v(TAG, "terminate");
	}
}
