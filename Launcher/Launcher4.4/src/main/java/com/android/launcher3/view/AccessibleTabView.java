package com.android.launcher3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.android.launcher3.helper.FocusHelper;
import com.yline.base.BaseTextView;

/**
 * We use a custom tab view to process our own focus traversals.
 */
public class AccessibleTabView extends BaseTextView
{
    public AccessibleTabView(Context context)
    {
        super(context);
    }
    
    public AccessibleTabView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public AccessibleTabView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return FocusHelper.handleTabKeyEvent(this, keyCode, event) || super.onKeyDown(keyCode, event);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return FocusHelper.handleTabKeyEvent(this, keyCode, event) || super.onKeyUp(keyCode, event);
    }
}
