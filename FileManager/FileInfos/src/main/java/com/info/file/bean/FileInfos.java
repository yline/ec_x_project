package com.info.file.bean;

import com.info.file.R;
import com.info.file.helper.FileSizeUtil;
import com.yline.utils.FileUtil;

import java.io.File;

/**
 * Created by yline on 2017/1/23.
 */
public class FileInfos
{
	private final static int ICON_FOLDER = R.drawable.filechooser_folder;

	private final static int ICON_FILE = R.drawable.filechooser_file;

	private int imageResId = ICON_FILE;

	public String fileName;

	/** 文件内容 */
	public String fileMessage;

	private String path;

	private boolean isDirectory;

	public FileInfos(File file)
	{
		setFile(file);
	}

	private void setFile(File file)
	{
		if (null != file)
		{
			this.isDirectory = file.isDirectory();
			this.fileName = file.getName();
			this.path = file.getAbsolutePath();

			if (isDirectory)
			{
				this.imageResId = ICON_FOLDER;

				int dirCount = file.listFiles(FileUtil.getsDirFilter()).length;
				int fileCount = file.listFiles(FileUtil.getsFileFilter()).length;
				
				this.fileMessage = String.format("文件夹：%d，文件：%d，大小：%s", dirCount, fileCount, FileSizeUtil.getFileOrDirAutoSize(file) + "b");
			}
			else
			{
				this.imageResId = ICON_FILE;
				this.fileMessage = String.format("大小：%s", FileSizeUtil.getFileOrDirAutoSize(file) + "b");

			}
		}
	}

	/** 显示图片的内容 */
	public int getImageResId()
	{
		return imageResId;
	}

	/** 当前文件名 */
	public String getFileName()
	{
		return fileName;
	}

	/** 获取当前文件的绝对路径 */
	public String getAbsolutePath()
	{
		return path;
	}

	public boolean isDirectory()
	{
		return isDirectory;
	}

	public String getFileMessage()
	{
		return fileMessage;
	}
}
