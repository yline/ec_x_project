package com.info.file.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.info.file.R;
import com.info.file.activity.MainActivity;
import com.info.file.adapter.FileListAdapter;
import com.info.file.bean.FileInfos;
import com.info.file.helper.FileLoader;
import com.yline.base.BaseListFragment;
import com.yline.utils.FileUtil;

import java.util.List;

/**
 * Created by yline on 2017/1/23.
 */
public class MainFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<List<FileInfos>>
{
	/**
	 * Interface to listen for events.
	 */
	public interface Callbacks
	{
		/**
		 * Called when a file is selected from the list.
		 * @param fileInfos The file selected
		 */
		public void onFileSelected(FileInfos fileInfos);
	}

	private static final int LOADER_ID = 0;

	private FileListAdapter mAdapter;

	private String mPath;

	private Callbacks mListener;

	/**
	 * Create a new instance with the given file path.
	 * @param path The absolute path of the file (directory) to display.
	 * @return A new Fragment with the given file path.
	 */
	public static MainFragment newInstance(String path)
	{
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putString(MainActivity.getTagPath(), path);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			mListener = (Callbacks) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement FileListFragment.Callbacks");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mAdapter = new FileListAdapter(getActivity());
		mPath = getArguments() != null ? getArguments().getString(MainActivity.getTagPath()) : FileUtil.getPath();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		setEmptyText(getString(R.string.empty_directory));
		setListAdapter(mAdapter);
		setListShown(false);

		getLoaderManager().initLoader(LOADER_ID, null, this);

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		FileListAdapter adapter = (FileListAdapter) l.getAdapter();
		if (adapter != null)
		{
			FileInfos fileInfo = (FileInfos) adapter.getItem(position);
			mPath = fileInfo.getAbsolutePath();
			mListener.onFileSelected(fileInfo);
		}
	}

	@Override
	public Loader<List<FileInfos>> onCreateLoader(int id, Bundle args)
	{
		return new FileLoader(getActivity(), mPath);
	}

	@Override
	public void onLoadFinished(Loader<List<FileInfos>> loader, List<FileInfos> data)
	{
		mAdapter.set(data);

		if (isResumed())
		{
			setListShown(true);
		}
		else
		{
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<FileInfos>> loader)
	{
		mAdapter.clear();
	}
}
