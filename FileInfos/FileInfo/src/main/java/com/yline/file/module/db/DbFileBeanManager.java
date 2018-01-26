package com.yline.file.module.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yline.file.module.file.model.FileBean;
import com.yline.log.LogFileUtil;

import java.util.List;

/**
 * FileBean
 *
 * @author yline 2017/2/2 --> 14:42
 * @version 1.0.0
 */
public class DbFileBeanManager extends DbBaseManager<FileBean> {
    /* 数据表 FileBean常量 */
    private static final String TABLE_NAME = "FileBean";

    private static final String IS_DIRECTORY = "isDirectory";
    private static final String FILE_NAME = "fileName";
    private static final String FILE_ABSOLUTE_PATH = "absolutePath";
    private static final String CHILD_DIR_COUNT = "childDirCount";
    private static final String CHILD_FILE_COUNT = "childFileCount";
    private static final String FILE_SIZE = "fileSize";

    private DbFileBeanManager() {
    }

    public static DbFileBeanManager getInstance() {
        return DbFileBeanManagerHolder.sInstance;
    }

    private static class DbFileBeanManagerHolder {
        private static final DbFileBeanManager sInstance = new DbFileBeanManager();
    }

    @Override
    public void init(Context context) {
        sqLiteHelper = SQLiteHelper.getInstance(context);
    }

    @Override
    public ContentValues dataToValues(FileBean bean) {
        ContentValues values = new ContentValues();
        values.put(IS_DIRECTORY, bean.isDirectory());
        values.put(FILE_NAME, bean.getFileName());
        values.put(FILE_ABSOLUTE_PATH, bean.getFileAbsolutePath());
        values.put(CHILD_DIR_COUNT, bean.getChildDirCount());
        values.put(CHILD_FILE_COUNT, bean.getChildFileCount());
        values.put(FILE_SIZE, bean.getFileSize());
        return values;
    }

    @Override
    public FileBean cursorToData(Cursor cursor) {
        if (null != cursor && cursor.moveToFirst()) {
            boolean isDirectory = (cursor.getInt(cursor.getColumnIndex(IS_DIRECTORY)) > 0);
            String fileName = cursor.getString(cursor.getColumnIndex(FILE_NAME));
            String fileAbsolutePath = cursor.getString(cursor.getColumnIndex(FILE_ABSOLUTE_PATH));
            int childDirCount = cursor.getInt(cursor.getColumnIndex(CHILD_DIR_COUNT));
            int childFileCount = cursor.getInt(cursor.getColumnIndex(CHILD_FILE_COUNT));
            long fileSize = cursor.getLong(cursor.getColumnIndex(FILE_SIZE));

            cursor.close();

            return new FileBean(isDirectory, fileName, fileAbsolutePath, childDirCount, childFileCount, fileSize);
        }
        return null;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        String createSql = String.format("create table %s(%s boolean, %s varchar(256), %s varchar(512) Unique, %s int, %s int, %s int)",
                TABLE_NAME, IS_DIRECTORY, FILE_NAME, FILE_ABSOLUTE_PATH, CHILD_DIR_COUNT, CHILD_FILE_COUNT, FILE_SIZE);
        db.execSQL(createSql);
    }

    public void insertAtSameMoment(List<FileBean> beanList) {
        LogFileUtil.v("FileBean insert to Db, number = " + beanList.size());

        database = sqLiteHelper.getWritableDatabase();
        database.beginTransaction();

        for (FileBean bean : beanList) {
            ContentValues values = dataToValues(bean);

            database.insert(TABLE_NAME, null, values);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public void insert(FileBean bean) {
        database = sqLiteHelper.getWritableDatabase();

        ContentValues values = dataToValues(bean);
        database.insert(TABLE_NAME, null, values);

        database.close();
    }

    public FileBean queryByAbsolutePath(String absolutePath) {
        Cursor cursor = sqLiteHelper.getWritableDatabase().query(TABLE_NAME, null,
                FILE_ABSOLUTE_PATH + "=?", new String[]{absolutePath}, null, null, null);
        return cursorToData(cursor);
    }
}
