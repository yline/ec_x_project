package com.info.file.helper;

import com.info.file.application.IApplication;
import com.info.file.bean.FileBean;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by yline on 2017/1/27.
 */
public class FileLoadRunnable implements Runnable
{
	private static final String TAG = FileLoadRunnable.class.getSimpleName();

	private static final int ERROR_SIZE = -1;

	private HashMap<String, FileBean> map = new HashMap<>();

	@Override
	public void run()
	{
		IApplication.toast("yeah");

		String rootPath = FileUtil.getPath();
		long startTime = System.currentTimeMillis();

		if (null != rootPath)
		{
			final File rootFile = new File(rootPath);
			long size = getDirSize(rootFile);

			LogFileUtil.v(TAG, formatFileAutoSize(size));
		}
		else
		{
			IApplication.toast("内存空间不存在");
			LogFileUtil.v(TAG, "rootPath is null");
		}

		LogFileUtil.v(TAG, "speedTime1 = " + (System.currentTimeMillis() - startTime));
		startTime = System.currentTimeMillis();


		LogFileUtil.v(TAG, map.keySet().size() + "");

		LogFileUtil.v(TAG, "speedTime2 = " + (System.currentTimeMillis() - startTime));
	}

	/** 一次性,全部读取所有的信息 */
	private long getDirSize(File file)
	{
		long tempSize = ERROR_SIZE;
		long totalSize = ERROR_SIZE;

		File[] childFiles = file.listFiles();
		for (int i = 0; i < childFiles.length; i++)
		{
			if (childFiles[i].isDirectory())
			{
				tempSize = getDirSize(childFiles[i]);
				if (tempSize != ERROR_SIZE)
				{
					totalSize += tempSize;
				}
			}
			else
			{
				tempSize = getFileSize(childFiles[i]);
				if (tempSize != ERROR_SIZE)
				{
					totalSize += tempSize;
				}
				final String fileKey = childFiles[i].getAbsolutePath();
				//map.put(fileKey, new FileBean(childFiles[i].getName(), fileKey, formatFileAutoSize(tempSize)));
			}
		}

		//final String fileKey = file.getAbsolutePath();
		//int dirCount = file.listFiles(FileUtil.getsDirFilter()).length;
		//int fileCount = file.listFiles(FileUtil.getsFileFilter()).length;
		//map.put(fileKey, new FileBean(file.getName(), fileKey, dirCount, fileCount, formatFileAutoSize(totalSize)));

		return totalSize;
	}

	/** 读取文件的大小 */
	private long getFileSize(File file)
	{
		if (null != file && file.exists())
		{
			FileInputStream fis = null;

			try
			{
				fis = new FileInputStream(file);
				int size = fis.available();
				return size;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				return ERROR_SIZE;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return ERROR_SIZE;
			}
			finally
			{
				if (null != fis)
				{
					try
					{
						fis.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return ERROR_SIZE;
	}

	private String formatFileAutoSize(long size)
	{
		DecimalFormat df = new DecimalFormat("0.00");
		String fileSizeStr = "0.00B";

		if (ERROR_SIZE == size)
		{
			return fileSizeStr;
		}

		if (size < 1024)
		{
			fileSizeStr = df.format(size) + "B";
		}
		else if (size < 1048576)
		{
			fileSizeStr = df.format(size / 1024.0) + "KB";
		}
		else if (size < 1073741824)
		{
			fileSizeStr = df.format(size / 1048576.0) + "MB";
		}
		else if (size < 1099511627776l)
		{
			fileSizeStr = df.format(size / 1073741824.0) + "GB";
		}
		else
		{
			fileSizeStr = df.format(size / 1099511627776.0) + "TB";
		}

		return fileSizeStr;
	}
}
