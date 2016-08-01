package com.file.chooser.activity;

import com.yline.application.BaseApplication;
import com.yline.application.SDKConfig;

import android.os.Message;

public class IApplication extends BaseApplication
{
    public static final String TAG = "FileChooser";
    
    @Override
    protected void handlerDefault(Message msg)
    {
        
    }
    
    @Override
    protected SDKConfig initConfig()
    {
        SDKConfig appConfig = new SDKConfig();
        appConfig.setLogFilePath("FileChooser"); // 默认开启日志,并写到文件中
        return appConfig;
    }
}
