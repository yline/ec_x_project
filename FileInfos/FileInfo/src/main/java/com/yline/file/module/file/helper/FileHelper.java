package com.yline.file.module.file.helper;

import com.yline.file.IApplication;
import com.yline.file.module.file.model.FileBean;
import com.yline.log.LogFileUtil;

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
}
