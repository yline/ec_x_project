package com.info.file.bean;

import com.info.file.R;

import java.io.File;

/**
 * Created by yline on 2017/1/23.
 */
public class FileInfos
{
	private final static int ICON_FOLDER = R.drawable.filechooser_folder;

	private final static int ICON_FILE = R.drawable.filechooser_file;

	private int imageResId = ICON_FILE;

	private File file;

	public String fileName;

	/** 文件内容 */
	public String fileInfo;

	private int fileCount;

	private int folderCount;

	private long fileSize;

	public FileInfos(File file)
	{
		setFile(file);
	}

	private void setFile(File file)
	{
		this.file = file;
		if (null != file)
		{
			if (file.isDirectory())
			{
				this.imageResId = ICON_FOLDER;
			}
			else
			{
				this.imageResId = ICON_FILE;
			}

			this.fileName = file.getName();

			this.fileInfo = String.format("文件：%s，文件夹：%s，大小：%s", "0", "0", "0");
		}
	}

	public int getImageResId()
	{
		return imageResId;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String getFileInfo()
	{
		return fileInfo;
	}

	public File getFile()
	{
		return file;
	}
}
