package com.info.file.bean;

import com.info.file.R;
import com.yline.utils.FileSizeUtil;

/**
 * Created by yline on 2017/1/27.
 */
public class FileBean
{
	private final static int DEFAULT_FILE_SIZE = -1;

	private final static String DEFAULT_FILE_SIZE_HINT = "Loading...";

	private final static int ICON_FOLDER = R.drawable.filechooser_folder;

	private final static int ICON_FILE = R.drawable.filechooser_file;

	private boolean isDirectory;

	private String fileName;

	private String fileAbsolutePath;

	private int childDirCount;

	private int childFileCount;

	private long fileSize;

	/**
	 * 新建文件 或者 文件夹
	 * @param isDirectory
	 * @param fileName
	 * @param fileAbsolutePath
	 * @param childDirCount
	 * @param childFileCount
	 * @param fileSize
	 */
	public FileBean(boolean isDirectory, String fileName, String fileAbsolutePath, int childDirCount, int childFileCount, long fileSize)
	{
		this.isDirectory = isDirectory;
		this.fileName = fileName;
		this.fileAbsolutePath = fileAbsolutePath;
		this.childDirCount = childDirCount;
		this.childFileCount = childFileCount;
		this.fileSize = fileSize;
	}

	/**
	 * 新建文件的信息
	 * @param fileName         文件名
	 * @param fileAbsolutePath 文件绝对路径
	 * @param fileSize         文件大小
	 */
	public FileBean(String fileName, String fileAbsolutePath, long fileSize)
	{
		this.isDirectory = false;
		this.fileName = fileName;
		this.fileAbsolutePath = fileAbsolutePath;
		this.fileSize = fileSize;
	}

	/**
	 * 新建 文件夹信息
	 * @param fileName         文件夹名
	 * @param fileAbsolutePath 文件夹绝对路径
	 * @param dirCount         文件夹,子目录的文件夹个数
	 * @param fileCount        文件夹,子目录的文件个数
	 * @param dirSize          该文件夹大小
	 */
	public FileBean(String fileName, String fileAbsolutePath, int dirCount, int fileCount, long dirSize)
	{
		this.isDirectory = true;
		this.fileName = fileName;
		this.fileAbsolutePath = fileAbsolutePath;
		this.childDirCount = dirCount;
		this.childFileCount = fileCount;
		this.fileSize = dirSize;
	}

	public boolean isDirectory()
	{
		return isDirectory;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String getFileAbsolutePath()
	{
		return fileAbsolutePath;
	}

	public int getChildDirCount()
	{
		return childDirCount;
	}

	public int getChildFileCount()
	{
		return childFileCount;
	}

	public long getFileSize()
	{
		return fileSize;
	}

	/** 文件夹：0，文件：0，大小：0.00B */
	public String getChildMessage()
	{
		if (isDirectory)
		{
			if (DEFAULT_FILE_SIZE == fileSize)
			{
				return String.format("文件夹：%d，文件：%d，大小：%s", childDirCount, childFileCount, DEFAULT_FILE_SIZE_HINT);
			}
			else
			{
				return String.format("文件夹：%d，文件：%d，大小：%s", childDirCount, childFileCount, FileSizeUtil.formatFileAutoSize(fileSize));
			}
		}
		else
		{
			if (DEFAULT_FILE_SIZE == fileSize)
			{
				return String.format("大小：%s", DEFAULT_FILE_SIZE_HINT);
			}
			else
			{
				return String.format("大小：%s", FileSizeUtil.formatFileAutoSize(fileSize));
			}
		}
	}


	public int getImageId()
	{
		if (isDirectory)
		{
			return ICON_FOLDER;
		}
		return ICON_FILE;
	}

	public static int getDefaultFileSize()
	{
		return DEFAULT_FILE_SIZE;
	}
}
