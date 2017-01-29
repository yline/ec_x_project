package com.info.file.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yline.log.LogFileUtil;

/**
 * @author yline 2017/1/29 --> 18:43
 * @version 1.0.0
 */
public class SQLiteHelper extends SQLiteOpenHelper
{
	/* 数据库常量 */
	private static final String DATABASE_NAME = "SampleQueryPage.db";

	private static final int DATABASE_VERSION = 1;

	/* 数据表 FileBean常量 */
	private static final String FILE_TABLE_NAME = "FileBean";

	private static final String FILE_IS_DIRECTORY = "isDirectory";

	private static final String FILE_NAME = "fileName";

	private static final String FILE_ABSOLUTE_PATH = "fileAbsolutePath";

	private static final String FILE_CHILD_MESSAGE = "childMessage";

	/* 其它数据表内容 */

	public SQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		LogFileUtil.v("onCreate");

		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		LogFileUtil.v("onUpgrade oldVersion = " + oldVersion + ", newVersion = " + newVersion);
	}

	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
		// LogFileUtil.v("onOpen"); 如果频繁的写入,则打印会浪费很多时间
	}

	/** 创建数据表 */
	private void createTable(SQLiteDatabase db)
	{
		String tableSQLite = String.format("create table %s(%s Boolean, %s varchar(128), %s varchar(512) Unique, %s varchar(128))",
				FILE_TABLE_NAME, FILE_IS_DIRECTORY, FILE_NAME, FILE_ABSOLUTE_PATH, FILE_CHILD_MESSAGE);
		db.execSQL(tableSQLite);
	}

	public static String getFileTableName()
	{
		return FILE_TABLE_NAME;
	}

	public static String getFileIsDirectory()
	{
		return FILE_IS_DIRECTORY;
	}

	public static String getFileName()
	{
		return FILE_NAME;
	}

	public static String getFileAbsolutePath()
	{
		return FILE_ABSOLUTE_PATH;
	}

	public static String getFileChildMessage()
	{
		return FILE_CHILD_MESSAGE;
	}
}
