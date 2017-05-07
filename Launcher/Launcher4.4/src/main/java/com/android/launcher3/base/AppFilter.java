package com.android.launcher3.base;

import android.content.ComponentName;
import android.text.TextUtils;

import com.android.launcher3.activity.Launcher;
import com.yline.log.LogFileUtil;

public abstract class AppFilter
{
    private static final String TAG = "AppFilter";
    
    public abstract boolean shouldShowApp(ComponentName app);
    
    public static AppFilter loadByName(String className)
    {
        if (TextUtils.isEmpty(className))
            return null;
        LogFileUtil.v(Launcher.TAG, "Loading AppFilter: " + className);
        try
        {
            Class<?> cls = Class.forName(className);
            return (AppFilter)cls.newInstance();
        }
        catch (ClassNotFoundException e)
        {
            LogFileUtil.e(TAG, "Bad AppFilter class", e);
            return null;
        }
        catch (InstantiationException e)
        {
            LogFileUtil.e(TAG, "Bad AppFilter class", e);
            return null;
        }
        catch (IllegalAccessException e)
        {
            LogFileUtil.e(TAG, "Bad AppFilter class", e);
            return null;
        }
        catch (ClassCastException e)
        {
            LogFileUtil.e(TAG, "Bad AppFilter class", e);
            return null;
        }
    }
    
}
