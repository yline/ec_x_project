package com.info.file.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.info.file.application.IApplication;
import com.info.file.fragment.MainFragment;
import com.yline.base.BaseAppCompatActivity;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;

public class MainActivity extends BaseAppCompatActivity implements FragmentManager.OnBackStackChangedListener
{
	private static final String TAG_PATH = "Path";

	private FragmentManager fragmentManager;

	/** 当前位置的 path */
	private String path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		fragmentManager = getSupportFragmentManager();
		fragmentManager.addOnBackStackChangedListener(this);

		if (null == savedInstanceState)
		{
			path = FileUtil.getPath();
			if (null == path)
			{
				IApplication.toast("SDCard cannot be used");
				// update the fragment

				MainFragment mainFragment = MainFragment.newInstance(path);
				fragmentManager.beginTransaction().add(android.R.id.content, mainFragment).commit();
			}
		}
		else
		{
			path = savedInstanceState.getString(TAG_PATH);
		}

		setTitle(path);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		outState.putString(TAG_PATH, path);
	}

	@Override
	public void onBackStackChanged()
	{
		LogFileUtil.v("Fragment onBackStackChanged");
	}

	public static String getTagPath()
	{
		return TAG_PATH;
	}
}
