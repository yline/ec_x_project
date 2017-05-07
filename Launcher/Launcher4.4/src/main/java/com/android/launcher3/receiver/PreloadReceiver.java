package com.android.launcher3.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.LauncherProvider;
import com.yline.base.BaseReceiver;

public class PreloadReceiver extends BaseReceiver
{
    private static final String TAG = "Launcher.PreloadReceiver";
    
    private static final boolean LOGD = false;
    
    public static final String EXTRA_WORKSPACE_NAME = "com.android.launcher3.action.EXTRA_WORKSPACE_NAME";
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        final LauncherProvider provider = LauncherAppState.getLauncherProvider();
        if (provider != null)
        {
            String name = intent.getStringExtra(EXTRA_WORKSPACE_NAME);
            final int workspaceResId =
                !TextUtils.isEmpty(name) ? context.getResources().getIdentifier(name, "xml", "com.android.launcher3")
                    : 0;
            if (LOGD)
            {
                Log.d(TAG, "workspace name: " + name + " id: " + workspaceResId);
            }
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    provider.loadDefaultFavoritesIfNecessary(workspaceResId);
                }
            }).start();
        }
    }
}
