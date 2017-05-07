package com.android.launcher3.receiver;

import android.content.Context;
import android.content.Intent;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.base.WidgetPreviewLoader;
import com.yline.base.BaseReceiver;

public class PackageChangedReceiver extends BaseReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        super.onReceive(context, intent);
        final String packageName = intent.getData().getSchemeSpecificPart();
        
        if (packageName == null || packageName.length() == 0)
        {
            // they sent us a bad intent
            return;
        }
        // in rare cases the receiver races with the application to set up LauncherAppState
        LauncherAppState.setApplicationContext(context.getApplicationContext());
        LauncherAppState app = LauncherAppState.getInstance();
        WidgetPreviewLoader.removePackageFromDb(app.getWidgetPreviewCacheDb(), packageName);
    }
}
