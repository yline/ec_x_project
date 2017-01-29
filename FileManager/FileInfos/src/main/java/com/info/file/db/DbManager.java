package com.info.file.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.info.file.bean.FileBean;
import com.yline.log.LogFileUtil;

import java.util.List;

/**
 * Created by yline on 2017/1/29.
 */
public class DbManager
{
	private SQLiteHelper sqLiteHelper;

	private SQLiteDatabase database;

	private DbManager()
	{
	}

	public static DbManager getInstance()
	{
		return DbManagerHolder.sInstance;
	}

	private static class DbManagerHolder
	{
		private static final DbManager sInstance = new DbManager();
	}

	public void init(Context context)
	{
		sqLiteHelper = new SQLiteHelper(context);
	}

	public void fileBeanInsertAtSameMoment(List<FileBean> beanList)
	{
		LogFileUtil.v("FileBean insert to Db, number = " + beanList.size());

		database = sqLiteHelper.getWritableDatabase();
		database.beginTransaction();

		for (FileBean bean : beanList)
		{
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.getFileIsDirectory(), bean.isDirectory());
			values.put(SQLiteHelper.getFileName(), bean.getFileName());
			values.put(SQLiteHelper.getFileAbsolutePath(), bean.getFileAbsolutePath());
			values.put(SQLiteHelper.getFileChildMessage(), bean.getChildMessage());

			database.insert(SQLiteHelper.getFileTableName(), null, values);
		}

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
	}

	public void fileBeanInsert(FileBean bean)
	{
		database = sqLiteHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.getFileIsDirectory(), bean.isDirectory());
		values.put(SQLiteHelper.getFileName(), bean.getFileName());
		values.put(SQLiteHelper.getFileAbsolutePath(), bean.getFileAbsolutePath());
		values.put(SQLiteHelper.getFileChildMessage(), bean.getChildMessage());

		database.insert(SQLiteHelper.getFileTableName(), null, values);

		database.close();
	}

	public FileBean fileBeanQueryByPath(String path)
	{
		Cursor cursor = sqLiteHelper.getWritableDatabase().query(SQLiteHelper.getFileTableName(), null,
				SQLiteHelper.getFileAbsolutePath() + "=" + path, null, null, null, null);
		if (null != cursor)
		{
			boolean isDirectory = (cursor.getInt(cursor.getColumnIndex(SQLiteHelper.getFileIsDirectory())) > 0);
			String name = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getFileName()));
			String childMessage = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getFileChildMessage()));

			return new FileBean(isDirectory, name, path, childMessage);
		}
		return null;
	}
}
