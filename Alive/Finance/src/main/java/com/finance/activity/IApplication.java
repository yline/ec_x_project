package com.finance.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2016/10/26.
 */
public class IApplication extends BaseApplication
{
	public static final String TAG = "Finance金融计算";

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);
		sdkConfig.setLog(false);
		sdkConfig.setLogSystem(true);
		return sdkConfig;
	}
}
