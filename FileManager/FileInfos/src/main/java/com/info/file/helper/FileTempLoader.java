package com.info.file.helper;

import android.os.AsyncTask;

import com.info.file.bean.FileBean;
import com.yline.log.LogFileUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 加载临时的数据,没有文件大小
 * @author yline 2017/1/28 --> 12:24
 * @version 1.0.0
 */
public class FileTempLoader extends AsyncTask<String, Void, List<FileBean>>
{
	private LoadListener listener;

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

		final File[] dirs = pathDir.listFiles(sDirFilter);
		if (null != dirs)
		{
			Arrays.sort(dirs, sComparator);

			for (File dirFile : dirs)
			{
				resultList.add(new FileBean(dirFile.getName(), dirFile.getAbsolutePath(),
						dirFile.listFiles(sDirFilter).length,
						dirFile.listFiles(sFileFilter).length,
						null));
			}
		}

		final File[] files = pathDir.listFiles(sFileFilter);
		if (null != files)
		{
			Arrays.sort(files, sComparator);

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

	public void setLoadListener(LoadListener listener)
	{
		this.listener = listener;
	}

	public interface LoadListener
	{
		public void onLoadFinish(List<FileBean> fileBeen);
	}

	private static Comparator<File> sComparator = new Comparator<File>()
	{
		public int compare(File f1, File f2)
		{
			return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
		}
	};

	private static FileFilter sFileFilter = new FileFilter()
	{
		public boolean accept(File file)
		{
			/*
			String fileName = file.getName();
			return file.isFile() && !fileName.startsWith(".");
			*/
			return file.isFile();
		}
	};

	private static FileFilter sDirFilter = new FileFilter()
	{
		public boolean accept(File file)
		{
			/*
			String fileName = file.getName();
			return file.isDirectory() && !fileName.startsWith(".");
			*/
			return file.isDirectory();
		}
	};
}
