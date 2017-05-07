package com.android.launcher3;

import java.lang.ref.WeakReference;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;

import com.android.launcher3.DynamicGrid.DeviceProfile;
import com.android.launcher3.activity.Launcher;
import com.android.launcher3.base.AppFilter;
import com.android.launcher3.base.WidgetPreviewLoader;
import com.android.launcher3.helper.IconCache;
import com.android.launcher3.receiver.LauncherModel;
import com.yline.log.LogFileUtil;

/**
 * 生命周期与 LauncherApplication同步
 */
public class LauncherAppState
{
    /** sharePreferences key */
    private static final String SHARED_PREFERENCES_KEY = "com.android.launcher3.prefs";
    
    /**
     * 数据的操作类
     */
    private LauncherModel mModel;
    
    /**
     * 图标缓存
     */
    private IconCache mIconCache;
    
    private AppFilter mAppFilter;
    
    private WidgetPreviewLoader.CacheDb mWidgetPreviewCacheDb;
    
    private boolean mIsScreenLarge;
    
    private float mScreenDensity;
    
    private int mLongPressTimeout = 300;
    
    private static WeakReference<LauncherProvider> sLauncherProvider;
    
    private static Context sContext;
    
    /**
     * LauncherAppState实例
     */
    private static LauncherAppState INSTANCE;
    
    private DynamicGrid mDynamicGrid;
    
    /** 被调用次数非常多;1,CellLayout中onMeasure + onChildMeasure + onDraw中调用了 */
    public static LauncherAppState getInstance()
    {
        LogFileUtil.v(Launcher.TAG, "LauncherAppState -> getInstance");
        if (INSTANCE == null)
        {
            INSTANCE = new LauncherAppState();
        }
        return INSTANCE;
    }
    
    public static LauncherAppState getInstanceNoCreate()
    {
        return INSTANCE;
    }
    
    public Context getContext()
    {
        return sContext;
    }
    
    public static void setApplicationContext(Context context)
    {
        if (sContext != null)
        {
            LogFileUtil.v("setApplicationContext called twice! old=" + sContext + " new=" + context);
        }
        sContext = context.getApplicationContext();
    }
    
    private LauncherAppState()
    {
        LogFileUtil.v(Launcher.TAG, "LauncherAppState Construct start");
        if (sContext == null)
        {
            throw new IllegalStateException("LauncherAppState inited before app context set");
        }
        
        if (sContext.getResources().getBoolean(R.bool.debug_memory_enabled)) // false
        {
            MemoryTracker.startTrackingMe(sContext, "L");
        }
        
        // set sIsScreenXLarge and mScreenDensity *before* creating icon cache
        mIsScreenLarge = isScreenLarge(sContext.getResources()); // pad is true
        mScreenDensity = sContext.getResources().getDisplayMetrics().density; // T1 1.0
        
        mWidgetPreviewCacheDb = new WidgetPreviewLoader.CacheDb(sContext);
        mIconCache = new IconCache(sContext);
        
        mAppFilter = AppFilter.loadByName(sContext.getString(R.string.app_filter_class)); // ""
        mModel = new LauncherModel(this, mIconCache, mAppFilter);
        LogFileUtil.v(Launcher.TAG, "LauncherAppState register broadcast");
        // Register intent receivers; 没有分层过滤的.
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        sContext.registerReceiver(mModel, filter);
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        filter.addAction(Intent.ACTION_LOCALE_CHANGED);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        sContext.registerReceiver(mModel, filter);
        filter = new IntentFilter();
        filter.addAction(SearchManager.INTENT_GLOBAL_SEARCH_ACTIVITY_CHANGED);
        sContext.registerReceiver(mModel, filter);
        filter = new IntentFilter();
        filter.addAction(SearchManager.INTENT_ACTION_SEARCHABLES_CHANGED);
        sContext.registerReceiver(mModel, filter);
        
        // Register for changes to the favorites
        ContentResolver resolver = sContext.getContentResolver();
        resolver.registerContentObserver(LauncherSettings.Favorites.CONTENT_URI, true, mFavoritesObserver);
        LogFileUtil.v(Launcher.TAG, "LauncherAppState Construct end");
    }
    
    /**
     * Call from Application.onTerminate(), which is not guaranteed to ever be called.
     */
    public void onTerminate()
    {
        sContext.unregisterReceiver(mModel);
        
        ContentResolver resolver = sContext.getContentResolver();
        resolver.unregisterContentObserver(mFavoritesObserver);
    }
    
    /**
     * Receives notifications whenever the user favorites have changed.
     */
    private final ContentObserver mFavoritesObserver = new ContentObserver(new Handler())
    {
        @Override
        public void onChange(boolean selfChange)
        {
            // If the database has ever changed, then we really need to force a reload of the
            // workspace on the next load
            mModel.resetLoadedState(false, true);
            mModel.startLoaderFromBackground();
        }
    };
    
    public LauncherModel setLauncher(Launcher launcher)
    {
        if (mModel == null)
        {
            throw new IllegalStateException("setLauncher() called before init()");
        }
        mModel.initialize(launcher);
        LogFileUtil.v(Launcher.TAG, "setLauncher -> LauncherModel initialize");
        return mModel;
    }
    
    public IconCache getIconCache()
    {
        return mIconCache;
    }
    
    public LauncherModel getModel()
    {
        return mModel;
    }
    
    public boolean shouldShowAppOrWidgetProvider(ComponentName componentName)
    {
        return mAppFilter == null || mAppFilter.shouldShowApp(componentName);
    }
    
    public WidgetPreviewLoader.CacheDb getWidgetPreviewCacheDb()
    {
        return mWidgetPreviewCacheDb;
    }
    
    static void setLauncherProvider(LauncherProvider provider)
    {
        sLauncherProvider = new WeakReference<LauncherProvider>(provider);
    }
    
    public static LauncherProvider getLauncherProvider()
    {
        return sLauncherProvider.get();
    }
    
    public static String getSharedPreferencesKey()
    {
        return SHARED_PREFERENCES_KEY;
    }
    
    public DeviceProfile initDynamicGrid(Context context, int minWidth, int minHeight, int width, int height,
        int availableWidth, int availableHeight)
    {
        LogFileUtil.v(Launcher.TAG, "start mDynamicGrid = " + mDynamicGrid);
        if (mDynamicGrid == null)
        {
            mDynamicGrid =
                new DynamicGrid(context, context.getResources(), minWidth, minHeight, width, height, availableWidth,
                    availableHeight);
        }
        
        // Update the icon size
        DeviceProfile grid = mDynamicGrid.getDeviceProfile();
        Utilities.setIconSize(grid.iconSizePx); // 80
        grid.updateFromConfiguration(context.getResources(), width, height, availableWidth, availableHeight);
        LogFileUtil.v(Launcher.TAG, "end ");
        return grid;
    }
    
    public DynamicGrid getDynamicGrid()
    {
        return mDynamicGrid;
    }
    
    public boolean isScreenLarge()
    {
        return mIsScreenLarge;
    }
    
    // Need a version that doesn't require an instance of LauncherAppState for the wallpaper picker
    public static boolean isScreenLarge(Resources res)
    {
        return res.getBoolean(R.bool.is_large_tablet);
    }
    
    public static boolean isScreenLandscape(Context context)
    {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
    
    public float getScreenDensity()
    {
        return mScreenDensity;
    }
    
    public int getLongPressTimeout()
    {
        return mLongPressTimeout;
    }
}
