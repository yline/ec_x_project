package com.info.file.helper;

import android.os.AsyncTask;

import com.info.file.bean.FileBean;
import com.yline.log.LogFileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 加载临时的数据,没有文件大小
 * @author yline 2017/1/28 --> 12:24
 * @version 1.0.0
 */
public class FileTempLoader extends AsyncTask<String, Void, List<FileBean>>
{
	private FileHelper.LoadListener listener;

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

		final File[] dirs = pathDir.listFiles(FileHelper.getsDirFilter());
		if (null != dirs)
		{
			Arrays.sort(dirs, FileHelper.getsComparator());

			for (File dirFile : dirs)
			{
				resultList.add(new FileBean(dirFile.getName(), dirFile.getAbsolutePath(),
						dirFile.listFiles(FileHelper.getsDirFilter()).length,
						dirFile.listFiles(FileHelper.getsFileFilter()).length,
						null));
			}
		}

		final File[] files = pathDir.listFiles(FileHelper.getsFileFilter());
		if (null != files)
		{
			Arrays.sort(files, FileHelper.getsComparator());

			for (File file : files)
			{
				resultList.add(new FileBean(file.getName(), file.getAbsolutePath(), null));
			}
		}

		return resultList;
	}

	@Override
	protected void onPostExecute(List<FileBean> fileBeen)
	{
		super.onPostExecute(fileBeen);

		callLoadListener(fileBeen);
	}

	public void callLoadListener(List<FileBean> fileBeen)
	{
		if (null != listener)
		{
			listener.onLoadFinish(fileBeen);
		}
	}

	public void setLoadListener(FileHelper.LoadListener listener)
	{
		this.listener = listener;
	}


}
