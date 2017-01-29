package com.info.file.application;

import android.content.Intent;

import com.info.file.db.DbManager;
import com.info.file.helper.FileLoadService;
import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

/**
 * Created by yline on 2017/1/22.
 */
public class IApplication extends BaseApplication
{
	public static final String TAG = "FileInfos";

	@Override
	public void onCreate()
	{
		super.onCreate();

		// 初始化数据库
		DbManager.getInstance().init(this);

		// 开启Service服务,准备缓存文件
		startService(new Intent(this, FileLoadService.class));
	}

	@Override
	protected SDKConfig initConfig()
	{
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setLogFilePath(TAG);

		sdkConfig.setLogSystem(true);
		sdkConfig.setLog(false);

		return sdkConfig;
	}
}
