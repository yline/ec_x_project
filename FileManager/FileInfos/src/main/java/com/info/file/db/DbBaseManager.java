package com.info.file.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbBaseManager<T>
{
	protected SQLiteHelper sqLiteHelper;

	protected SQLiteDatabase database;

	/** 初始化数据库,初始化sqLiteHelper */
	public abstract void init(Context context);

	/** data to Value */
	public abstract ContentValues dataToValues(T t);

	/** Cursor to Data */
	public abstract T cursorToData(Cursor cursor);

	/** 创建数据表 */
	public abstract void createTable(SQLiteDatabase db);
}
