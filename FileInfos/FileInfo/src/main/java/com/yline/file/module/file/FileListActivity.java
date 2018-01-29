package com.yline.file.module.file;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.file.IApplication;
import com.yline.file.R;
import com.yline.file.module.file.model.FileModel;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;

import java.io.File;

/**
 * 文件管理器
 *
 * @author yline 2018/1/25 -- 13:44
 * @version 1.0.0
 */
public class FileListActivity extends BaseAppCompatActivity implements FragmentManager.OnBackStackChangedListener, FileListFragment.onFileSelectedCallback {
    private static final String TAG_PATH = "path";

    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, FileListActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private FragmentManager mFragmentManager;
    private FileListFragment fileListFragment;

    private BroadcastReceiver mStorageListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SDKManager.toast(context.getResources().getText(R.string.storage_removed).toString());
            finishWithResult(null);
        }
    };

    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            mPath = FileUtil.getPathTop();
            addFragment();
        } else {
            mPath = savedInstanceState.getString(TAG_PATH);
        }

        setTitle(mPath);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterStorageListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerStorageListener();
    }

    @Override
    public void onBackStackChanged() {
        LogFileUtil.v("onBackStackChanged");

        int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            FragmentManager.BackStackEntry entry = mFragmentManager.getBackStackEntryAt(count - 1);
            mPath = entry.getName();
        } else {
            mPath = FileUtil.getPathTop();
        }

        fileListFragment.refreshFragment(mPath);
        setTitle(mPath);
    }

    /**
     * Add the initial Fragment with given path.
     */
    private void addFragment() {
        fileListFragment = FileListFragment.newInstance(mPath);
        mFragmentManager.beginTransaction().add(android.R.id.content, fileListFragment).commit();
    }

    /**
     * Register the external storage BroadcastReceiver.
     */
    private void registerStorageListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        registerReceiver(mStorageListener, filter);
    }

    /**
     * Unregister the external storage BroadcastReceiver.
     */
    private void unregisterStorageListener() {
        unregisterReceiver(mStorageListener);
    }

    /**
     * Finish this Activity with a result code and URI of the selected file.
     *
     * @param fileBean The file selected.
     */
    private void finishWithResult(FileModel fileBean) {
        if (fileBean != null) {
            Intent intent = new Intent().putExtra(TAG_PATH, fileBean.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onFileSelected(String path) {
        LogFileUtil.v("select path = " + path);

        File file = new File(path);
        if (file.isDirectory()) {
            this.mPath = path;
            // 以下方法会调用 onBackStackChanged,因此不需要再次手动刷新一次FileListFragment
            mFragmentManager.beginTransaction().addToBackStack(path).commit();
        } else {
            IApplication.toast("文件名为：" + file.getName());
            LogFileUtil.v("path = " + file.getAbsolutePath());
        }
    }

    public static String getTagPath() {
        return TAG_PATH;
    }
}
