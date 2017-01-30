package com.info.file.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.info.file.application.IApplication;
import com.info.file.bean.FileBean;
import com.info.file.db.DbManager;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileUtil;
import com.yline.utils.SPUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库更新采取两种措施:
 * 1,遍历数据时,如果发现不存在在数据库中的文件夹,则进行小批量更新数据库
 * 2,每间隔一个礼拜,进行扫描一次,并且扫描时,并且采取的是,完全替换表的方式
 * @author yline 2017/1/30 --> 0:54
 * @version 1.0.0
 */
public class FileLoadService extends Service
{
	private static final String KEY_FILE_NAME = "FileLoadService";

	/** 记录是否更新完毕,替换表时,读取不到文件大小 */
	private static final String KEY_IS_CACHE = "FileLoad_Switch";

	/** 记录新的更新时间 */
	private static final String KEY_LAST_SCAN_TIME = "LastScanTime";

	/** 更新间隔时间 */
	private static final long UPDATE_DURATION = 86400 * 1000 * 7; // ms

	private boolean isStartCache()
	{

		return false;
	}

	private void endCache()
	{

	}

	private void setIsCached(Context context, boolean isCached)
	{
		SPUtil.put(context, KEY_IS_CACHE, isCached);
	}

	public static boolean isCached(Context context)
	{
		return (boolean) SPUtil.get(context, KEY_IS_CACHE, false);
	}


	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// 判断是否进行 加载文件
		boolean isStartCache = isStartCache();
		LogFileUtil.v("isStartCache = " + isStartCache);
		if (isStartCache)
		{
			new Thread(new FileLoadRunnable(this)).start();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private class FileLoadRunnable implements Runnable
	{
		private static final String TAG = "FileLoadRunnable";

		private static final int ERROR_SIZE = -1;

		private List<FileBean> resultBean = new ArrayList<>();

		private Context context;

		public FileLoadRunnable(Context context)
		{
			this.context = context;
		}

		@Override
		public void run()
		{
			setIsCached(context, false);

			String rootPath = FileUtil.getPath();
			long startTime = System.currentTimeMillis();
			LogFileUtil.v("rootPath = " + rootPath + ",startTime = " + startTime);

			if (null != rootPath)
			{
				final File rootFile = new File(rootPath);
				long size = getDirSize(rootFile);

				LogFileUtil.v(TAG, "DirSize = " + formatFileAutoSize(size));
			}
			else
			{
				IApplication.toast("内存空间不存在");
				LogFileUtil.v(TAG, "rootPath is null");
			}

			LogFileUtil.v(TAG, "ReadTime = " + (System.currentTimeMillis() - startTime) + ",fileNumber = " + resultBean.size());
			startTime = System.currentTimeMillis();

			DbManager.getInstance().fileBeanInsertAtSameMoment(resultBean);
			LogFileUtil.v(TAG, "WriteTime = " + (System.currentTimeMillis() - startTime));

			setIsCached(context, true);
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

					resultBean.add(new FileBean(childFiles[i].getName(), childFiles[i].getAbsolutePath(), formatFileAutoSize(tempSize)));
				}
			}

			resultBean.add(new FileBean(file.getName(), file.getAbsolutePath(),
					file.listFiles(FileUtil.getsDirFilter()).length,
					file.listFiles(FileUtil.getsFileFilter()).length,
					formatFileAutoSize(totalSize)));

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
					return fis.available();
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
}
