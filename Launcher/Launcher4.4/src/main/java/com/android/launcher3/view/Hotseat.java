package com.android.launcher3.view;

import java.util.ArrayList;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.launcher3.DynamicGrid.DeviceProfile;
import com.android.launcher3.LauncherAppState;
import com.android.launcher3.LauncherSettings;
import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.activity.Launcher;
import com.android.launcher3.helper.IconCache;
import com.android.launcher3.helper.FocusHelper.HotseatIconKeyEventListener;
import com.android.launcher3.info.AppInfo;
import com.android.launcher3.info.FolderInfo;
import com.android.launcher3.info.ShortcutInfo;
import com.android.launcher3.receiver.LauncherModel;
import com.yline.base.BaseFrameLayout;
import com.yline.log.LogFileUtil;

public class Hotseat extends BaseFrameLayout
{
    private static final String TAG = "Hotseat";
    
    private CellLayout mContent;
    
    private Launcher mLauncher;
    
    private int mAllAppsButtonRank;
    
    private boolean mTransposeLayoutWithOrientation;
    
    private boolean mIsLandscape;
    
    public Hotseat(Context context)
    {
        this(context, null);
    }
    
    public Hotseat(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public Hotseat(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        
        Resources r = context.getResources();
        mTransposeLayoutWithOrientation = r.getBoolean(R.bool.hotseat_transpose_layout_with_orientation);
        mIsLandscape = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
    
    public void setup(Launcher launcher)
    {
        mLauncher = launcher;
        setOnKeyListener(new HotseatIconKeyEventListener());
    }
    
    public CellLayout getLayout()
    {
        return mContent;
    }
    
    /**
     * Registers the specified listener on the cell layout of the hotseat.
     */
    @Override
    public void setOnLongClickListener(OnLongClickListener l)
    {
        mContent.setOnLongClickListener(l);
    }
    
    private boolean hasVerticalHotseat()
    {
        return (mIsLandscape && mTransposeLayoutWithOrientation);
    }
    
    /** Get the orientation invariant order of the item in the hotseat for persistence. */
    public int getOrderInHotseat(int x, int y)
    {
        return hasVerticalHotseat() ? (mContent.getCountY() - y - 1) : x;
    }
    
    /** Get the orientation specific coordinates given an invariant order in the hotseat. */
    public int getCellXFromOrder(int rank)
    {
        return hasVerticalHotseat() ? 0 : rank;
    }
    
    public int getCellYFromOrder(int rank)
    {
        return hasVerticalHotseat() ? (mContent.getCountY() - (rank + 1)) : 0;
    }
    
    public boolean isAllAppsButtonRank(int rank)
    {
        if (AppsCustomizePagedView.DISABLE_ALL_APPS)
        {
            return false;
        }
        else
        {
            return rank == mAllAppsButtonRank;
        }
    }
    
    /** This returns the coordinates of an app in a given cell, relative to the DragLayer */
    public Rect getCellCoordinates(int cellX, int cellY)
    {
        Rect coords = new Rect();
        mContent.cellToRect(cellX, cellY, 1, 1, coords);
        int[] hotseatInParent = new int[2];
        Utilities.getDescendantCoordRelativeToParent(this, mLauncher.getDragLayer(), hotseatInParent, false);
        coords.offset(hotseatInParent[0], hotseatInParent[1]);
        
        // Center the icon
        int cWidth = mContent.getShortcutsAndWidgets().getCellContentWidth();
        int cHeight = mContent.getShortcutsAndWidgets().getCellContentHeight();
        int cellPaddingX = (int)Math.max(0, ((coords.width() - cWidth) / 2f));
        int cellPaddingY = (int)Math.max(0, ((coords.height() - cHeight) / 2f));
        coords.offset(cellPaddingX, cellPaddingY);
        
        return coords;
    }
    
    /* 自我生命周期,未被调用;当它的子布局,布局结束后,执行 */
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        LauncherAppState app = LauncherAppState.getInstance();
        DeviceProfile grid = app.getDynamicGrid().getDeviceProfile();
        
        mAllAppsButtonRank = grid.hotseatAllAppsRank; // 4
        mContent = (CellLayout)findViewById(R.id.layout);
        if (grid.isLandscape && !grid.isLargeTablet())
        {
            mContent.setGridSize(1, (int)grid.numHotseatIcons);
        }
        else
        {
            mContent.setGridSize((int)grid.numHotseatIcons, 1); // T1,to do 
        }
        mContent.setIsHotseat(true);
        
        resetLayout();
    }
    
    public void resetLayout()
    {
        LogFileUtil.v(Launcher.TAG, "resetLayout init");
        mContent.removeAllViewsInLayout();
        
        if (!AppsCustomizePagedView.DISABLE_ALL_APPS)
        {
            // Add the Apps button
            Context context = getContext();
            
            LayoutInflater inflater = LayoutInflater.from(context);
            TextView allAppsButton = (TextView)inflater.inflate(R.layout.all_apps_button, mContent, false);
            @SuppressWarnings("deprecation")
            Drawable d = context.getResources().getDrawable(R.drawable.all_apps_button_icon);
            Utilities.resizeIconDrawable(d);
            allAppsButton.setCompoundDrawables(null, d, null, null);
            
            allAppsButton.setContentDescription(context.getString(R.string.all_apps_button_label));
            if (mLauncher != null)
            {
                allAppsButton.setOnTouchListener(mLauncher.getHapticFeedbackTouchListener());
            }
            allAppsButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(android.view.View v)
                {
                    if (mLauncher != null)
                    {
                        mLauncher.onClickAllAppsButton(v);
                    }
                }
            });
            
            // Note: We do this to ensure that the hotseat is always laid out in the orientation of
            // the hotseat in order regardless of which orientation they were added
            int x = getCellXFromOrder(mAllAppsButtonRank);
            int y = getCellYFromOrder(mAllAppsButtonRank);
            CellLayout.LayoutParams lp = new CellLayout.LayoutParams(x, y, 1, 1);
            lp.canReorder = false;
            mContent.addViewToCellLayout(allAppsButton, -1, 0, lp, true);
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        // We don't want any clicks to go through to the hotseat unless the workspace is in
        // the normal state.
        if (mLauncher.getWorkspace().isSmall())
        {
            return true;
        }
        return false;
    }
    
    public void addAllAppsFolder(IconCache iconCache, ArrayList<AppInfo> allApps, ArrayList<ComponentName> onWorkspace,
        Launcher launcher, Workspace workspace)
    {
        if (AppsCustomizePagedView.DISABLE_ALL_APPS)
        {
            FolderInfo fi = new FolderInfo();
            
            fi.cellX = getCellXFromOrder(mAllAppsButtonRank);
            fi.cellY = getCellYFromOrder(mAllAppsButtonRank);
            fi.spanX = 1;
            fi.spanY = 1;
            fi.container = LauncherSettings.Favorites.CONTAINER_HOTSEAT;
            fi.screenId = mAllAppsButtonRank;
            fi.itemType = LauncherSettings.Favorites.ITEM_TYPE_FOLDER;
            fi.title = "More Apps";
            LauncherModel.addItemToDatabase(launcher, fi, fi.container, fi.screenId, fi.cellX, fi.cellY, false);
            FolderIcon folder = FolderIcon.fromXml(R.layout.folder_icon, launcher, getLayout(), fi, iconCache);
            workspace.addInScreen(folder, fi.container, fi.screenId, fi.cellX, fi.cellY, fi.spanX, fi.spanY);
            
            for (AppInfo info : allApps)
            {
                ComponentName cn = info.intent.getComponent();
                if (!onWorkspace.contains(cn))
                {
                    Log.d(TAG, "Adding to 'more apps': " + info.intent);
                    ShortcutInfo si = info.makeShortcut();
                    fi.add(si);
                }
            }
        }
    }
    
    /* 暂时,没地方,调用 */
    public void addAppsToAllAppsFolder(ArrayList<AppInfo> apps)
    {
        if (AppsCustomizePagedView.DISABLE_ALL_APPS)
        {
            View v = mContent.getChildAt(getCellXFromOrder(mAllAppsButtonRank), getCellYFromOrder(mAllAppsButtonRank));
            FolderIcon fi = null;
            
            if (v instanceof FolderIcon)
            {
                fi = (FolderIcon)v;
            }
            else
            {
                return;
            }
            
            FolderInfo info = fi.getFolderInfo();
            for (AppInfo a : apps)
            {
                ShortcutInfo si = a.makeShortcut();
                info.add(si);
            }
        }
    }
}
