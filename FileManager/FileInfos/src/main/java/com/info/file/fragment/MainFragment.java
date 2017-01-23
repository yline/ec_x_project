package com.info.file.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.info.file.R;
import com.info.file.activity.MainActivity;
import com.info.file.adapter.FileListAdapter;
import com.info.file.bean.FileInfos;
import com.yline.base.BaseFragment;
import com.yline.utils.FileUtil;

/**
 * Created by yline on 2017/1/23.
 */
public class MainFragment extends BaseFragment
{
	private ListView listView;

	private FileListAdapter fileListAdapter;

	private String path;

	public static MainFragment newInstance(String path)
	{
		MainFragment mainFragment = new MainFragment();

		Bundle bundle = new Bundle();
		bundle.putString(MainActivity.getTagPath(), path);
		mainFragment.setArguments(bundle);

		return mainFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (null == getArguments())
		{
			this.path = FileUtil.getPath();
		}
		else
		{
			this.path = getArguments().getString(MainActivity.getTagPath());
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		listView = (ListView) view.findViewById(R.id.lv_file);

		fileListAdapter = new FileListAdapter(getContext());
		listView.setAdapter(fileListAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (null != parent.getAdapter())
				{
					FileInfos fileInfos = (FileInfos) parent.getAdapter().getItem(position);
					path = fileInfos.getFile().getPath();
					// 通知Activity更改内容
				}
			}
		});

		getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks()
		{
			@Override
			public Loader onCreateLoader(int id, Bundle args)
			{
				return null;
			}

			@Override
			public void onLoadFinished(Loader loader, Object data)
			{

			}

			@Override
			public void onLoaderReset(Loader loader)
			{

			}
		});
	}
}
