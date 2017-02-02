package com.info.file.helper;

import android.os.AsyncTask;

import com.info.file.bean.FileBean;
import com.info.file.db.DbFileBeanManager;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileDbLoader extends AsyncTask<String, Void, List<FileBean>>
{
	private FileHelper.LoadListener loadListener;

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
	}

	@Override
	protected List<FileBean> doInBackground(String... params)
	{
		String path = params[0];
		LogFileUtil.v(path);

		List<FileBean> resultList = new ArrayList<>();

		final File pathDir = new File(path);

		final File[] dirs = pathDir.listFiles(FileUtil.getsDirFilter());
		if (null != dirs)
		{
			Arrays.sort(dirs, FileUtil.getsComparator());

			for (File dirFile : dirs)
			{
				FileBean bean = DbFileBeanManager.getInstance().queryByAbsolutePath(dirFile.getAbsolutePath());
				// 更新数据
				if (null == bean)
				{
					bean = new FileBean(dirFile.getName(), dirFile.getAbsolutePath(),
							dirFile.listFiles(FileUtil.getsDirFilter()).length,
							dirFile.listFiles(FileUtil.getsFileFilter()).length,
							FileSizeUtil.getFileOrDirAutoSize(dirFile));

					DbFileBeanManager.getInstance().insert(bean);
				}
				resultList.add(bean);
			}
		}

		final File[] files = pathDir.listFiles(FileUtil.getsFileFilter());
		if (null != files)
		{
			Arrays.sort(files, FileUtil.getsComparator());

			for (File file : files)
			{
				FileBean bean = DbFileBeanManager.getInstance().queryByAbsolutePath(file.getAbsolutePath());
				// 更新数据
				if (null == bean)
				{
					bean = new FileBean(file.getName(), file.getAbsolutePath(), FileSizeUtil.getFileOrDirAutoSize(file));
					DbFileBeanManager.getInstance().insert(bean);
				}
				resultList.add(bean);
			}
		}

		return resultList;
	}

	@Override
	protected void onPostExecute(List<FileBean> been)
	{
		super.onPostExecute(been);

		callLoadListener(been);
	}

	public void setLoadListener(FileHelper.LoadListener loadListener)
	{
		this.loadListener = loadListener;
	}

	private void callLoadListener(List<FileBean> fileBeanList)
	{
		if (null != loadListener)
		{
			loadListener.onLoadFinish(fileBeanList);
		}
	}
}
