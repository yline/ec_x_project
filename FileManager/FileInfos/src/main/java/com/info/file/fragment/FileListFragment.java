package com.info.file.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.info.file.R;
import com.info.file.adapter.FileListAdapter;
import com.info.file.helper.FileHelper;
import com.yline.base.BaseListFragment;

/**
 * Created by yline on 2017/1/27.
 */
public class FileListFragment extends BaseListFragment
{
	private String path;

	private FileListAdapter mAdapter;

	private FileHelper fileHelper;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		fileHelper = new FileHelper();

		mAdapter = new FileListAdapter(getActivity());
		setListAdapter(mAdapter);

		setEmptyText(getString(R.string.empty_directory));
		setListShown(false);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		// 获取数据、更新数据
		mAdapter.set(fileHelper.getFileList(path));
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
	}
}
