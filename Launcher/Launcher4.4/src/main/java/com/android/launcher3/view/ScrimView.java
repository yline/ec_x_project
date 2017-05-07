package com.android.launcher3.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.android.launcher3.base.Insettable;
import com.yline.base.BaseFrameLayout;

public class ScrimView extends BaseFrameLayout implements Insettable
{
    
    public ScrimView(Context context)
    {
        this(context, null, 0);
    }
    
    public ScrimView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public ScrimView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    @Override
    public void setInsets(Rect insets)
    {
        // Do nothing
    }
}
