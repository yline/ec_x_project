package com.yline.file.module.file.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import com.yline.file.common.FileType;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.sqlite.SQLiteIOUtils;
import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.common.Column;
import com.yline.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
        private final static Column FileType = new Column(2, Long.class, "FileType", false, "FileType");
        private final static Column Data = new Column(3, Object.class, "Data", false, "Data");
    }

    public FileDbModelDao(SQLiteDatabase db) {
        super(db, TABLE_NAME, new Column[]{Table.AbsolutePath, Table.IsDir, Table.FileType, Table.Data}, new Column[]{Table.AbsolutePath});
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "if not exists" : "";
        String sql = String.format("create table %s %s(%s text primary key, %s boolean, %s long, %s blob);",
                constraint, TABLE_NAME, Table.AbsolutePath.getColumnName(), Table.IsDir.getColumnName(), Table.FileType.getColumnName(), Table.Data.getColumnName());
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
        int fileType = cursor.isNull(Table.FileType.getOrdinal()) ? FileType.UNKNOW.getFid() : cursor.getInt(Table.FileType.getOrdinal());
        long isDir = cursor.isNull(Table.IsDir.getOrdinal()) ? FALSE : cursor.getLong(Table.IsDir.getOrdinal());
        byte[] data = cursor.isNull(Table.Data.getOrdinal()) ? null : cursor.getBlob(Table.Data.getOrdinal());
        return new FileDbModel(absolutePath, fileType, (isDir == TRUE), data);
    }

    @Override
    protected boolean bindValues(SQLiteStatement stmt, FileDbModel fileDbModel) {
        String absolutePath = fileDbModel.getAbsolutePath();
        if (null != absolutePath) {
            stmt.bindString(1 + Table.AbsolutePath.getOrdinal(), absolutePath);
        }

        stmt.bindLong(1 + Table.FileType.getOrdinal(), fileDbModel.getFileType());
        stmt.bindLong(1 + Table.IsDir.getOrdinal(), (fileDbModel.isDir() ? TRUE : FALSE));

        byte[] data = fileDbModel.getData();
        if (null != data) {
            stmt.bindBlob(1 + Table.Data.getOrdinal(), data);
        }

        return false;
    }

    /*** ------------------------------ 自定义功能 -------------------------------- **/
    /**
     * 计算某一种类型，有多少数据
     *
     * @return select * from MsgTable where Age=? and (Name like ?  or Name like ?)
     */
    public long countFileType(FileType fileType) {
        String sql = String.format(Locale.CHINA, "select * from %s where %s=?", TABLE_NAME, Table.FileType.getColumnName());
        String[] fileTypeArray = new String[]{String.valueOf(fileType.getFid())};
        LogUtil.v("countFileType sql = " + sql + ", array = " + Arrays.toString(fileTypeArray));

        return rawQuery(sql, fileTypeArray, new OnCursorCallback<Long>() {
            @Override
            public Long onRawQuery(Cursor cursor) {
                if (null != cursor) {
                    return (long) cursor.getCount();
                }
                return 0L;
            }
        });
    }

    @NonNull
    public List<FileInfoModel> loadFileType(FileType fileType) {
        String sql = String.format(Locale.CHINA, "select * from %s where %s=?", TABLE_NAME, Table.FileType.getColumnName());
        String[] fileTypeArray = new String[]{String.valueOf(fileType.getFid())};
        LogUtil.v("loadFileType sql = " + sql + ", array = " + Arrays.toString(fileTypeArray));

        return rawQuery(sql, fileTypeArray, new OnCursorCallback<List<FileInfoModel>>() {

            @Override
            public List<FileInfoModel> onRawQuery(Cursor cursor) {
                List<FileInfoModel> fileInfoList = new ArrayList<>();
                if (null != cursor) {
                    while (cursor.moveToNext()) {
                        FileDbModel fileDbModel = readModel(cursor);
                        if (null != fileDbModel && null != fileDbModel.getData()) {
                            Object obj = SQLiteIOUtils.byte2Object(fileDbModel.getData());
                            if (null != obj && obj instanceof FileInfoModel) {
                                fileInfoList.add((FileInfoModel) obj);
                            }
                        }
                    }
                }
                return fileInfoList;
            }
        });
    }

    @Override
    public <Result> Result rawQuery(String sql, String[] selectionArgs, OnCursorCallback<Result> callback) {
        return super.rawQuery(sql, selectionArgs, callback);
    }
}
