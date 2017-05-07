package com.android.launcher3.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.android.launcher3.activity.WallpaperPickerActivity;
import com.yline.base.BaseRelativeLayout;

public class WallpaperRootView extends BaseRelativeLayout
{
    private final WallpaperPickerActivity a;
    
    public WallpaperRootView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        a = (WallpaperPickerActivity)context;
    }
    
    public WallpaperRootView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        a = (WallpaperPickerActivity)context;
    }
    
    protected boolean fitSystemWindows(Rect insets)
    {
        a.setWallpaperStripYOffset(insets.bottom);
        return true;
    }
}
