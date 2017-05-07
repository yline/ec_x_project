package com.android.launcher3.base;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;

public abstract class LauncherAnimatorUpdateListener implements AnimatorUpdateListener
{
    public void onAnimationUpdate(ValueAnimator animation)
    {
        final float b = (Float)animation.getAnimatedValue();
        final float a = 1f - b;
        onAnimationUpdate(a, b);
    }
    
    public abstract void onAnimationUpdate(float a, float b);
}