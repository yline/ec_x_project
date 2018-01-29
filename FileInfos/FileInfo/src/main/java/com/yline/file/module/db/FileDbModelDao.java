package com.yline.file.module.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.common.Column;

import java.util.HashMap;

/**
 * 文件，数据库，管理类
 *
 * @author yline 2018/1/29 -- 11:32
 * @version 1.0.0
 */
public class FileDbModelDao extends AbstractSafelyDao<String, FileDbModel> {
    private static final long TRUE = 1L;
    private static final long FALSE = 0L;

    public static final String TABLE_NAME = "FileDbModelDao";

    private static class Table {
        private final static Column AbsolutePath = new Column(0, String.class, "AbsolutePath", true, "AbsolutePath");
        private final static Column IsDir = new Column(1, Boolean.class, "IsDir", false, "IsDir");
        private final static Column Data = new Column(2, Object.class, "Data", false, "Data");
    }

    public FileDbModelDao(SQLiteDatabase db) {
        super(db, TABLE_NAME, new Column[]{Table.AbsolutePath, Table.IsDir, Table.Data}, new Column[]{Table.AbsolutePath});
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "if not exists" : "";
        String sql = String.format("create table %s %s(%s long primary key, %s boolean, %s blob);",
                constraint, TABLE_NAME, Table.AbsolutePath.getColumnName(), Table.IsDir.getColumnName(), Table.Data.getColumnName());
        db.execSQL(sql);
    }

    public static void attachSession(HashMap<String, AbstractSafelyDao> hashMap, SQLiteDatabase db) {
        hashMap.put(TABLE_NAME, new FileDbModelDao(db));
    }

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = String.format("drop table %s %s", ifExists ? "if exists" : "", TABLE_NAME);
        db.execSQL(sql);
    }

    @Override
    public String getKey(FileDbModel fileDbModel) {
        return fileDbModel.getAbsolutePath();
    }

    @Override
    protected String readKey(Cursor cursor) {
        return cursor.isNull(Table.AbsolutePath.getOrdinal()) ? null : cursor.getString(Table.AbsolutePath.getOrdinal());
    }

    @Override
    protected FileDbModel readModel(Cursor cursor) {
        String absolutePath = cursor.isNull(Table.AbsolutePath.getOrdinal()) ? null : cursor.getString(Table.AbsolutePath.getOrdinal());
        long isDir = cursor.isNull(Table.IsDir.getOrdinal()) ? FALSE : cursor.getLong(Table.IsDir.getOrdinal());
        byte[] data = cursor.isNull(Table.Data.getOrdinal()) ? null : cursor.getBlob(Table.Data.getOrdinal());
        return new FileDbModel(absolutePath, (isDir == TRUE), data);
    }

    @Override
    protected boolean bindValues(SQLiteStatement stmt, FileDbModel fileDbModel) {
        String absolutePath = fileDbModel.getAbsolutePath();
        if (null != absolutePath){
            stmt.bindString(1 + Table.AbsolutePath.getOrdinal(), absolutePath);
        }

        stmt.bindLong(1 + Table.IsDir.getOrdinal(), (fileDbModel.isDir() ? TRUE : FALSE));

        byte[] data = fileDbModel.getData();
        if (null != data){
            stmt.bindBlob(1 + Table.Data.getOrdinal(), data);
        }

        return false;
    }
}
