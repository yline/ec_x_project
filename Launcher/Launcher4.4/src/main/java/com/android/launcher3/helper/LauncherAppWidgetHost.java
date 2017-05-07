package com.android.launcher3.helper;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

import com.android.launcher3.activity.Launcher;
import com.android.launcher3.receiver.LauncherModel;
import com.android.launcher3.view.LauncherAppWidgetHostView;
import com.yline.log.LogFileUtil;

/**
 * Specific {@link AppWidgetHost} that creates our {@link LauncherAppWidgetHostView}
 * which correctly captures all long-press events. This ensures that users can
 * always pick up and move widgets.
 * 
 * 1,监听来自AppWidgetService的事件,处理update和provider_changed事件
 * 2,用于创建AppWidghtHostView
 */
public class LauncherAppWidgetHost extends AppWidgetHost
{
    private Launcher mLauncher;
    
    public LauncherAppWidgetHost(Launcher launcher, int hostId)
    {
        super(launcher, hostId);
        LogFileUtil.v(Launcher.TAG, "LauncherAppWidgetHost construct inited hostId = " + hostId);
        mLauncher = launcher;
        // LogFileUtil.v(Launcher.TAG, "LauncherAppWidgetHost Construct end");
    }
    
    @Override
    protected AppWidgetHostView onCreateView(Context context, int appWidgetId, AppWidgetProviderInfo appWidget)
    {
        return new LauncherAppWidgetHostView(context);
    }
    
    @Override
    public void stopListening()
    {
        super.stopListening();
        clearViews();
    }
    
    protected void onProvidersChanged()
    {
        // Once we get the message that widget packages are updated, we need to rebind items
        // in AppsCustomize accordingly.
        mLauncher.bindPackagesUpdated(LauncherModel.getSortedWidgetsAndShortcuts(mLauncher));
    }
}
