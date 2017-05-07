package com.android.launcher3.helper;

import java.util.HashMap;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;

import com.android.launcher3.view.AppsCustomizeTabHost;
import com.android.launcher3.view.Cling;

public class HideFromAccessibilityHelper implements OnHierarchyChangeListener
{
    private HashMap<View, Integer> mPreviousValues;
    
    boolean mHide;
    
    boolean mOnlyAllApps;
    
    public HideFromAccessibilityHelper()
    {
        mPreviousValues = new HashMap<View, Integer>();
        mHide = false;
    }
    
    public void setImportantForAccessibilityToNo(View v, boolean onlyAllApps)
    {
        mOnlyAllApps = onlyAllApps;
        setImportantForAccessibilityToNoHelper(v);
        mHide = true;
    }
    
    private void setImportantForAccessibilityToNoHelper(View v)
    {
        mPreviousValues.put(v, v.getImportantForAccessibility());
        v.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        
        // Call method on children recursively
        if (v instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup)v;
            vg.setOnHierarchyChangeListener(this);
            for (int i = 0; i < vg.getChildCount(); i++)
            {
                View child = vg.getChildAt(i);
                
                if (includeView(child))
                {
                    setImportantForAccessibilityToNoHelper(child);
                }
            }
        }
    }
    
    public void restoreImportantForAccessibility(View v)
    {
        if (mHide)
        {
            restoreImportantForAccessibilityHelper(v);
        }
        mHide = false;
    }
    
    private void restoreImportantForAccessibilityHelper(View v)
    {
        Integer important = mPreviousValues.get(v);
        v.setImportantForAccessibility(important);
        mPreviousValues.remove(v);
        
        // Call method on children recursively
        if (v instanceof ViewGroup)
        {
            ViewGroup vg = (ViewGroup)v;
            
            // We assume if a class implements OnHierarchyChangeListener, it listens
            // to changes to any of its children (happens to be the case in Launcher)
            if (vg instanceof OnHierarchyChangeListener)
            {
                vg.setOnHierarchyChangeListener((OnHierarchyChangeListener)vg);
            }
            else
            {
                vg.setOnHierarchyChangeListener(null);
            }
            for (int i = 0; i < vg.getChildCount(); i++)
            {
                View child = vg.getChildAt(i);
                if (includeView(child))
                {
                    restoreImportantForAccessibilityHelper(child);
                }
            }
        }
    }
    
    public void onChildViewAdded(View parent, View child)
    {
        if (mHide && includeView(child))
        {
            setImportantForAccessibilityToNoHelper(child);
        }
    }
    
    public void onChildViewRemoved(View parent, View child)
    {
        if (mHide && includeView(child))
        {
            restoreImportantForAccessibilityHelper(child);
        }
    }
    
    private boolean includeView(View v)
    {
        return !hasAncestorOfType(v, Cling.class)
            && (!mOnlyAllApps || hasAncestorOfType(v, AppsCustomizeTabHost.class));
    }
    
    private boolean hasAncestorOfType(View v, Class<?> c)
    {
        return v != null
            && (v.getClass().equals(c) || (v.getParent() instanceof ViewGroup && hasAncestorOfType((ViewGroup)v.getParent(),
                c)));
    }
}