package com.info.file.helper;

import com.info.file.application.IApplication;
import com.info.file.bean.FileBean;
import com.yline.log.LogFileUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.List;

/**
 * 这里负责加载数据
 * 缓存结束:则读取缓存
 * 未结束:读取现场
 * @author yline 2017/1/28 --> 11:47
 * @version 1.0.0
 */
public class FileHelper
{
	public FileHelper()
	{
	}

	public void getFileList(LoadListener listener, String path)
	{
		LogFileUtil.v("path = " + path);
		if (FileLoadService.isCached(IApplication.getApplication()))
		{
			FileDbLoader dbLoader = new FileDbLoader();
			dbLoader.setLoadListener(listener);
			dbLoader.execute(path);
		}
		else
		{
			FileTempLoader tempLoader = new FileTempLoader();
			tempLoader.setLoadListener(listener);
			tempLoader.execute(path);
		}
	}

	public interface LoadListener
	{
		public void onLoadFinish(List<FileBean> fileBeanList);
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

	public static Comparator<File> getsComparator()
	{
		return sComparator;
	}

	public static FileFilter getsFileFilter()
	{
		return sFileFilter;
	}

	public static FileFilter getsDirFilter()
	{
		return sDirFilter;
	}
}
