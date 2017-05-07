package com.android.launcher3.receiver;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.LauncherSettings;
import com.android.launcher3.R;
import com.android.launcher3.activity.Launcher;
import com.yline.base.BaseReceiver;

public class UninstallShortcutReceiver extends BaseReceiver
{
    private static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    
    // The set of shortcuts that are pending uninstall
    private static ArrayList<PendingUninstallShortcutInfo> mUninstallQueue =
        new ArrayList<PendingUninstallShortcutInfo>();
    
    // Determines whether to defer uninstalling shortcuts immediately until
    // disableAndFlushUninstallQueue() is called.
    private static boolean mUseUninstallQueue = false;
    
    private static class PendingUninstallShortcutInfo
    {
        Intent data;
        
        public PendingUninstallShortcutInfo(Intent rawData)
        {
            data = rawData;
        }
    }
    
    public void onReceive(Context context, Intent data)
    {
        super.onReceive(context, data);
        if (!ACTION_UNINSTALL_SHORTCUT.equals(data.getAction()))
        {
            return;
        }
        
        PendingUninstallShortcutInfo info = new PendingUninstallShortcutInfo(data);
        if (mUseUninstallQueue)
        {
            mUninstallQueue.add(info);
        }
        else
        {
            processUninstallShortcut(context, info);
        }
    }
    
    public static void enableUninstallQueue()
    {
        mUseUninstallQueue = true;
    }
    
    public static void disableAndFlushUninstallQueue(Context context)
    {
        mUseUninstallQueue = false;
        Iterator<PendingUninstallShortcutInfo> iter = mUninstallQueue.iterator();
        while (iter.hasNext())
        {
            processUninstallShortcut(context, iter.next());
            iter.remove();
        }
    }
    
    private static void processUninstallShortcut(Context context, PendingUninstallShortcutInfo pendingInfo)
    {
        final Intent data = pendingInfo.data;
        
        LauncherAppState.setApplicationContext(context.getApplicationContext());
        LauncherAppState app = LauncherAppState.getInstance();
        synchronized (app)
        { // TODO: make removeShortcut internally threadsafe
            removeShortcut(context, data);
        }
    }
    
    private static void removeShortcut(Context context, Intent data)
    {
        Intent intent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
        String name = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        boolean duplicate = data.getBooleanExtra(Launcher.EXTRA_SHORTCUT_DUPLICATE, true);
        
        if (intent != null && name != null)
        {
            final ContentResolver cr = context.getContentResolver();
            Cursor c =
                cr.query(LauncherSettings.Favorites.CONTENT_URI,
                    new String[] {LauncherSettings.Favorites._ID, LauncherSettings.Favorites.INTENT},
                    LauncherSettings.Favorites.TITLE + "=?",
                    new String[] {name},
                    null);
            
            final int intentIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites.INTENT);
            final int idIndex = c.getColumnIndexOrThrow(LauncherSettings.Favorites._ID);
            
            boolean changed = false;
            
            try
            {
                while (c.moveToNext())
                {
                    try
                    {
                        if (intent.filterEquals(Intent.parseUri(c.getString(intentIndex), 0)))
                        {
                            final long id = c.getLong(idIndex);
                            final Uri uri = LauncherSettings.Favorites.getContentUri(id, false);
                            cr.delete(uri, null, null);
                            changed = true;
                            if (!duplicate)
                            {
                                break;
                            }
                        }
                    }
                    catch (URISyntaxException e)
                    {
                        // Ignore
                    }
                }
            }
            finally
            {
                c.close();
            }
            
            if (changed)
            {
                cr.notifyChange(LauncherSettings.Favorites.CONTENT_URI, null);
                Toast.makeText(context, context.getString(R.string.shortcut_uninstalled, name), Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }
}
