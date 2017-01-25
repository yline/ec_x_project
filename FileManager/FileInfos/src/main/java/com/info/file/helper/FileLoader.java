/*
 * Copyright (C) 2013 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.info.file.helper;

import android.content.Context;
import android.os.FileObserver;
import android.support.v4.content.AsyncTaskLoader;

import com.info.file.bean.FileInfos;
import com.yline.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loader that returns a list of Files in a given file path.
 * @author paulburke (ipaulpro)
 * @version 2013-12-11
 */
public class FileLoader extends AsyncTaskLoader<List<FileInfos>>
{
	private static final int FILE_OBSERVER_MASK = FileObserver.CREATE
			| FileObserver.DELETE | FileObserver.DELETE_SELF
			| FileObserver.MOVED_FROM | FileObserver.MOVED_TO
			| FileObserver.MODIFY | FileObserver.MOVE_SELF;

	private FileObserver mFileObserver;

	private List<FileInfos> mData;

	private String mPath;

	public FileLoader(Context context, String path)
	{
		super(context);
		this.mPath = path;
	}

	@Override
	public List<FileInfos> loadInBackground()
	{
		ArrayList<FileInfos> list = new ArrayList<>();

		// Current directory File instance
		final File pathDir = new File(mPath);

		// List file in this directory with the directory filter
		final File[] dirs = pathDir.listFiles(FileUtil.getsDirFilter());
		if (dirs != null)
		{
			// Sort the folders alphabetically
			Arrays.sort(dirs, FileUtil.getsComparator());
			// Add each folder to the File list for the list adapter
			for (File dir : dirs)
			{
				list.add(new FileInfos(dir));
			}
		}

		// List file in this directory with the file filter
		final File[] files = pathDir.listFiles(FileUtil.getsFileFilter());
		if (files != null)
		{
			// Sort the files alphabetically
			Arrays.sort(files, FileUtil.getsComparator());
			// Add each file to the File list for the list adapter
			for (File file : files)
			{
				list.add(new FileInfos(file));
			}
		}

		return list;
	}

	@Override
	public void deliverResult(List<FileInfos> data)
	{
		if (isReset())
		{
			onReleaseResources(data);
			return;
		}

		List<FileInfos> oldData = mData;
		mData = data;

		if (isStarted())
			super.deliverResult(data);

		if (oldData != null && oldData != data)
			onReleaseResources(oldData);
	}

	@Override
	protected void onStartLoading()
	{
		if (mData != null)
			deliverResult(mData);

		if (mFileObserver == null)
		{
			mFileObserver = new FileObserver(mPath, FILE_OBSERVER_MASK)
			{
				@Override
				public void onEvent(int event, String path)
				{
					onContentChanged();
				}
			};
		}
		mFileObserver.startWatching();

		if (takeContentChanged() || mData == null)
			forceLoad();
	}

	@Override
	protected void onStopLoading()
	{
		cancelLoad();
	}

	@Override
	protected void onReset()
	{
		onStopLoading();

		if (mData != null)
		{
			onReleaseResources(mData);
			mData = null;
		}
	}

	@Override
	public void onCanceled(List<FileInfos> data)
	{
		super.onCanceled(data);

		onReleaseResources(data);
	}

	protected void onReleaseResources(List<FileInfos> data)
	{
		if (mFileObserver != null)
		{
			mFileObserver.stopWatching();
			mFileObserver = null;
		}
	}
}