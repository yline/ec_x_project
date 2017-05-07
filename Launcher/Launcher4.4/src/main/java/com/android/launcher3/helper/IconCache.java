package com.android.launcher3.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.android.launcher3.DynamicGrid.DeviceProfile;
import com.android.launcher3.Utilities;
import com.android.launcher3.activity.Launcher;
import com.android.launcher3.info.AppInfo;
import com.android.launcher3.receiver.LauncherModel;
import com.yline.log.LogFileUtil;

/**
 * Cache of application icons.  Icons can be made from any thread.
 */
public class IconCache
{
    @SuppressWarnings("unused")
    private static final String TAG = "Launcher.IconCache";
    
    private static final int INITIAL_ICON_CACHE_CAPACITY = 50;
    
    private static class CacheEntry
    {
        public Bitmap icon;
        
        public String title;
    }
    
    private final Bitmap mDefaultIcon;
    
    private final Context mContext;
    
    private final PackageManager mPackageManager;
    
    private final HashMap<ComponentName, CacheEntry> mCache = new HashMap<ComponentName, CacheEntry>(
        INITIAL_ICON_CACHE_CAPACITY);
    
    private int mIconDpi;
    
    public IconCache(Context context)
    {
        LogFileUtil.v(Launcher.TAG, "IconCache construct init");
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        
        mContext = context;
        mPackageManager = context.getPackageManager();
        mIconDpi = activityManager.getLauncherLargeIconDensity(); // T1 240
        
        // need to set mIconDpi before getting default icon
        mDefaultIcon = makeDefaultIcon();
    }
    
    public Drawable getFullResDefaultActivityIcon()
    {
        return getFullResIcon(Resources.getSystem(), android.R.mipmap.sym_def_app_icon);
    }
    
    @SuppressWarnings("deprecation")
    public Drawable getFullResIcon(Resources resources, int iconId)
    {
        Drawable d;
        try
        {
            d = resources.getDrawableForDensity(iconId, mIconDpi);
        }
        catch (Resources.NotFoundException e)
        {
            d = null;
        }
        
        return (d != null) ? d : getFullResDefaultActivityIcon();
    }
    
    public Drawable getFullResIcon(String packageName, int iconId)
    {
        Resources resources;
        try
        {
            resources = mPackageManager.getResourcesForApplication(packageName);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            resources = null;
        }
        if (resources != null)
        {
            if (iconId != 0)
            {
                return getFullResIcon(resources, iconId);
            }
        }
        return getFullResDefaultActivityIcon();
    }
    
    public Drawable getFullResIcon(ResolveInfo info)
    {
        return getFullResIcon(info.activityInfo);
    }
    
    public Drawable getFullResIcon(ActivityInfo info)
    {
        
        Resources resources;
        try
        {
            resources = mPackageManager.getResourcesForApplication(info.applicationInfo);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            resources = null;
        }
        if (resources != null)
        {
            int iconId = info.getIconResource();
            if (iconId != 0)
            {
                return getFullResIcon(resources, iconId);
            }
        }
        return getFullResDefaultActivityIcon();
    }
    
    private Bitmap makeDefaultIcon()
    {
        Drawable d = getFullResDefaultActivityIcon();
        Bitmap b =
            Bitmap.createBitmap(Math.max(d.getIntrinsicWidth(), 1),
                Math.max(d.getIntrinsicHeight(), 1),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        d.setBounds(0, 0, b.getWidth(), b.getHeight());
        d.draw(c);
        c.setBitmap(null);
        return b;
    }
    
    /**
     * Remove any records for the supplied ComponentName.
     */
    public void remove(ComponentName componentName)
    {
        synchronized (mCache)
        {
            mCache.remove(componentName);
        }
    }
    
    /**
     * Empty out the cache.
     */
    public void flush()
    {
        synchronized (mCache)
        {
            mCache.clear();
        }
    }
    
    /**
     * Empty out the cache that aren't of the correct grid size
     */
    public void flushInvalidIcons(DeviceProfile grid)
    {
        synchronized (mCache)
        {
            LogFileUtil.v(Launcher.TAG, "flushInvalidIcons synchronized in");
            Iterator<Entry<ComponentName, CacheEntry>> it = mCache.entrySet().iterator();
            while (it.hasNext())
            {
                final CacheEntry e = it.next().getValue();
                if (e.icon.getWidth() != grid.iconSizePx || e.icon.getHeight() != grid.iconSizePx)
                {
                    it.remove();
                }
            }
        }
    }
    
    /**
     * Fill in "application" with the icon and label for "info."
     */
    public void getTitleAndIcon(AppInfo application, ResolveInfo info, HashMap<Object, CharSequence> labelCache)
    {
        synchronized (mCache)
        {
            CacheEntry entry = cacheLocked(application.componentName, info, labelCache);
            
            application.title = entry.title;
            application.iconBitmap = entry.icon;
        }
    }
    
    public Bitmap getIcon(Intent intent)
    {
        synchronized (mCache)
        {
            final ResolveInfo resolveInfo = mPackageManager.resolveActivity(intent, 0);
            ComponentName component = intent.getComponent();
            
            if (resolveInfo == null || component == null)
            {
                return mDefaultIcon;
            }
            
            CacheEntry entry = cacheLocked(component, resolveInfo, null);
            return entry.icon;
        }
    }
    
    public Bitmap getIcon(ComponentName component, ResolveInfo resolveInfo, HashMap<Object, CharSequence> labelCache)
    {
        synchronized (mCache)
        {
            if (resolveInfo == null || component == null)
            {
                return null;
            }
            
            CacheEntry entry = cacheLocked(component, resolveInfo, labelCache);
            return entry.icon;
        }
    }
    
    public boolean isDefaultIcon(Bitmap icon)
    {
        return mDefaultIcon == icon;
    }
    
    private CacheEntry cacheLocked(ComponentName componentName, ResolveInfo info,
        HashMap<Object, CharSequence> labelCache)
    {
        CacheEntry entry = mCache.get(componentName);
        if (entry == null)
        {
            entry = new CacheEntry();
            
            mCache.put(componentName, entry);
            
            ComponentName key = LauncherModel.getComponentNameFromResolveInfo(info);
            if (labelCache != null && labelCache.containsKey(key))
            {
                entry.title = labelCache.get(key).toString();
            }
            else
            {
                entry.title = info.loadLabel(mPackageManager).toString();
                if (labelCache != null)
                {
                    labelCache.put(key, entry.title);
                }
            }
            if (entry.title == null)
            {
                entry.title = info.activityInfo.name;
            }
            
            entry.icon = Utilities.createIconBitmap(getFullResIcon(info), mContext);
        }
        return entry;
    }
    
    public HashMap<ComponentName, Bitmap> getAllIcons()
    {
        synchronized (mCache)
        {
            HashMap<ComponentName, Bitmap> set = new HashMap<ComponentName, Bitmap>();
            for (ComponentName cn : mCache.keySet())
            {
                final CacheEntry e = mCache.get(cn);
                set.put(cn, e.icon);
            }
            return set;
        }
    }
}
