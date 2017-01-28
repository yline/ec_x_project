package com.info.file.helper;

/**
 * 这里负责加载数据
 * 缓存结束:则读取缓存
 * 未结束:读取现场
 * @author yline 2017/1/28 --> 11:47
 * @version 1.0.0
 */
public class FileHelper
{
	public void getFileList(FileTempLoader.LoadListener listener, String path)
	{
		FileTempLoader loader = new FileTempLoader();
		loader.setLoadListener(listener);
		loader.execute(path);
	}
}
