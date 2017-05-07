package com.android.launcher3.receiver;

import android.content.Context;
import android.content.Intent;

import com.yline.base.BaseReceiver;

/**
 * Takes care of setting initial wallpaper for a user, by selecting the
 * first wallpaper that is not in use by another user.
 */
public class UserInitializeReceiver extends BaseReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        // TODO: initial wallpaper now that wallpapers are owned by another app
    }
}
