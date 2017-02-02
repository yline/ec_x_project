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
	private static SQLiteHelper sInstance;

	/* 数据库常量 */
	private static final String DATABASE_NAME = "SampleQueryPage.db";

	private static final int DATABASE_VERSION = 1;

	private SQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static SQLiteHelper getInstance(Context context)
	{
		if (null == sInstance)
		{
			synchronized (SQLiteHelper.class)
			{
				if (null == sInstance)
				{
					sInstance = new SQLiteHelper(context);
				}
			}
		}
		return sInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		LogFileUtil.v("onCreate");

		DbFileBeanManager.getInstance().createTable(db);
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
}
