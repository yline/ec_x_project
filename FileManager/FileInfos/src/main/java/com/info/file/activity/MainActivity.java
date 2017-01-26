package com.info.file.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.info.file.R;
import com.info.file.application.IApplication;
import com.info.file.bean.FileInfos;
import com.info.file.fragment.MainFragment;
import com.yline.base.BaseAppCompatActivity;
import com.yline.utils.FileUtil;

public class MainActivity extends BaseAppCompatActivity implements FragmentManager.OnBackStackChangedListener, MainFragment.Callbacks
{
	private static final String TAG_PATH = "path";

	private static final boolean HAS_ACTIONBAR = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

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
		mFragmentManager.addOnBackStackChangedListener(this);

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

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putString(TAG_PATH, mPath);
	}

	@Override
	public void onBackStackChanged()
	{
		int count = mFragmentManager.getBackStackEntryCount();
		if (count > 0)
		{
			FragmentManager.BackStackEntry fragment = mFragmentManager.getBackStackEntryAt(count - 1);
			mPath = fragment.getName();
		}
		else
		{
			mPath = FileUtil.getPath();
		}

		setTitle(mPath);
		if (HAS_ACTIONBAR)
		{
			invalidateOptionsMenu();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (HAS_ACTIONBAR)
		{
			boolean hasBackStack = mFragmentManager.getBackStackEntryCount() > 0;

			ActionBar actionBar = getActionBar();
			if (null != actionBar)
			{
				actionBar.setDisplayHomeAsUpEnabled(hasBackStack);
				actionBar.setHomeButtonEnabled(hasBackStack);
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				mFragmentManager.popBackStack();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Add the initial Fragment with given path.
	 */
	private void addFragment()
	{
		MainFragment fragment = MainFragment.newInstance(mPath);
		mFragmentManager.beginTransaction().add(android.R.id.content, fragment).commit();
	}

	/**
	 * "Replace" the existing Fragment with a new one using given path. We're
	 * really adding a Fragment to the back stack.
	 * @param fileInfos The file (directory) to display.
	 */
	private void replaceFragment(FileInfos fileInfos)
	{
		mPath = fileInfos.getAbsolutePath();

		MainFragment fragment = MainFragment.newInstance(mPath);
		mFragmentManager.beginTransaction().replace(android.R.id.content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(mPath).commit();
	}

	/**
	 * Finish this Activity with a result code and URI of the selected file.
	 * @param fileInfos The file selected.
	 */
	private void finishWithResult(FileInfos fileInfos)
	{
		if (fileInfos != null)
		{
			Intent intent = new Intent().putExtra(MainActivity.getTagPath(), fileInfos.getAbsolutePath());
			setResult(RESULT_OK, intent);
			finish();
		}
		else
		{
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	/**
	 * Called when the user selects a File
	 * @param fileInfos The file that was selected
	 */
	@Override
	public void onFileSelected(FileInfos fileInfos)
	{
		if (fileInfos != null)
		{
			if (fileInfos.isDirectory())
			{
				replaceFragment(fileInfos);
			}
			else
			{
				finishWithResult(fileInfos);
			}
		}
		else
		{
			IApplication.toast(getResources().getText(R.string.error_selecting_file).toString());
		}
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

	public static String getTagPath()
	{
		return TAG_PATH;
	}
}
