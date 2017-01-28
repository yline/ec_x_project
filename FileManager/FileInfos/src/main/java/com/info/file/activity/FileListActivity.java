package com.info.file.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.info.file.R;
import com.info.file.application.IApplication;
import com.info.file.bean.FileBean;
import com.info.file.fragment.FileListFragment;
import com.yline.base.BaseAppCompatActivity;
import com.yline.utils.FileUtil;

public class FileListActivity extends BaseAppCompatActivity
{
	private static final String TAG_PATH = "path";

	private FragmentManager mFragmentManager;

	private BroadcastReceiver mStorageListener = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			IApplication.toast(context.getResources().getText(R.string.storage_removed).toString());
			finishWithResult(null);
		}
	};

	private String mPath;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mFragmentManager = getSupportFragmentManager();

		if (savedInstanceState == null)
		{
			mPath = FileUtil.getPath();
			addFragment();
		}
		else
		{
			mPath = savedInstanceState.getString(TAG_PATH);
		}

		setTitle(mPath);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.item_file_list_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				// mFragmentManager.popBackStack();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		unregisterStorageListener();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		registerStorageListener();
	}

	/**
	 * Add the initial Fragment with given path.
	 */
	private void addFragment()
	{
		FileListFragment fragment = FileListFragment.newInstance(mPath);
		mFragmentManager.beginTransaction().add(android.R.id.content, fragment).commit();
	}

	/**
	 * Register the external storage BroadcastReceiver.
	 */
	private void registerStorageListener()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		registerReceiver(mStorageListener, filter);
	}

	/**
	 * Unregister the external storage BroadcastReceiver.
	 */
	private void unregisterStorageListener()
	{
		unregisterReceiver(mStorageListener);
	}

	/**
	 * Finish this Activity with a result code and URI of the selected file.
	 * @param fileBean The file selected.
	 */
	private void finishWithResult(FileBean fileBean)
	{
		if (fileBean != null)
		{
			Intent intent = new Intent().putExtra(TAG_PATH, fileBean.getFileAbsolutePath());
			setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	public static String getTagPath()
	{
		return TAG_PATH;
	}
}
