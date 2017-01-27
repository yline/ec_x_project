package com.info.file.bean;

import com.info.file.R;

/**
 * Created by yline on 2017/1/27.
 */
public class FileBean
{
	private final static int ICON_FOLDER = R.drawable.filechooser_folder;

	private final static int ICON_FILE = R.drawable.filechooser_file;

	private boolean isDirectory;

	private String fileName;

	private String fileAbsolutePath;

	/** 文件夹：0，文件：0，大小：0.00B */
	private String childMessage;

	/**
	 * @param isDirectory      目录
	 * @param fileName         文件名
	 * @param fileAbsolutePath 文件绝对路径
	 * @param childMessage     显示的信息
	 */
	public FileBean(boolean isDirectory, String fileName, String fileAbsolutePath, String childMessage)
	{
		this.isDirectory = isDirectory;
		this.fileName = fileName;
		this.fileAbsolutePath = fileAbsolutePath;
		this.childMessage = childMessage;
	}

	/**
	 * 新建文件的信息
	 * @param fileName         文件名
	 * @param fileAbsolutePath 文件绝对路径
	 * @param fileSize         文件大小
	 */
	public FileBean(String fileName, String fileAbsolutePath, String fileSize)
	{
		this.isDirectory = false;
		this.fileName = fileName;
		this.fileAbsolutePath = fileAbsolutePath;
		this.childMessage = String.format("大小：%s", fileSize);
	}

	/**
	 * 新建 文件夹信息
	 * @param fileName         文件夹名
	 * @param fileAbsolutePath 文件夹绝对路径
	 * @param dirCount         文件夹,子目录的文件夹个数
	 * @param fileCount        文件夹,子目录的文件个数
	 * @param dirSize          该文件夹大小
	 */
	public FileBean(String fileName, String fileAbsolutePath, int dirCount, int fileCount, String dirSize)
	{
		this.isDirectory = true;
		this.fileName = fileName;
		this.fileAbsolutePath = fileAbsolutePath;
		this.childMessage = String.format("文件夹：%d，文件：%d，大小：%s", dirCount, fileCount, dirSize);
	}

	public boolean isDirectory()
	{
		return isDirectory;
	}

	public String getFileName()
	{
		return fileName;
	}

	public String getChildMessage()
	{
		return childMessage;
	}

	public String getFileAbsolutePath()
	{
		return fileAbsolutePath;
	}

	public int getImageId()
	{
		if (isDirectory)
		{
			return ICON_FOLDER;
		}
		return ICON_FILE;
	}

	@Override
	public String toString()
	{
		return "FileBean{" +
				"isDirectory=" + isDirectory +
				", fileName='" + fileName + '\'' +
				", fileAbsolutePath='" + fileAbsolutePath + '\'' +
				", childMessage='" + childMessage + '\'' +
				'}';
	}
}
