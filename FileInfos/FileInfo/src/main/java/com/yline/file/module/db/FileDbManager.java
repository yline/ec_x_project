package com.yline.file.module.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yline.file.module.file.model.FileModel;
import com.yline.sqlite.SQLiteIOUtils;
import com.yline.sqlite.SqliteManager;
import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.dao.DaoManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文件 db帮助类
 *
 * @author yline 2018/1/29 -- 9:48
 * @version 1.0.0
 */
public class FileDbManager {
    public static void init(Context context) {
        SqliteManager.init(context, new DaoManager.OnSQLiteLifeCallback() {
            @Override
            public void onCreate(SQLiteDatabase db) {
                FileDbModelDao.createTable(db, true);
            }

            @Override
            public void onDaoCreate(HashMap<String, AbstractSafelyDao> daoHashMap, SQLiteDatabase db) {
                FileDbModelDao.attachSession(daoHashMap, db);
            }

            @Override
            public void onUpdate(SQLiteDatabase db) {

            }
        });
    }

    public static void insertOrReplaceInTx(List<FileModel> list) {
        List<FileDbModel> fileDbModelList = new ArrayList<>();
        for (FileModel fileModel : list) {
            byte[] value = SQLiteIOUtils.object2Byte(fileModel);
            fileDbModelList.add(new FileDbModel(fileModel.getAbsolutePath(), fileModel.isDirectory(), value));
        }

        FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
        if (null != dbModelDao) {
            dbModelDao.insertOrReplaceInTx(fileDbModelList);
        }
    }

    public static FileModel loadFileModel(String path) {
        FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
        if (null != dbModelDao) {
            FileDbModel dbModel = dbModelDao.load(path);
            if (null != dbModel) {
                Object object = SQLiteIOUtils.byte2Object(dbModel.getData());
                if (object instanceof FileModel) {
                    return (FileModel) object;
                }
            }
        }
        return null;
    }

    public static void insertOrReplace(FileModel fileModel) {
        FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
        if (null != dbModelDao) {
            dbModelDao.insertOrReplace(new FileDbModel(fileModel.getAbsolutePath(), fileModel.isDirectory(), SQLiteIOUtils.object2Byte(fileModel)));
        }
    }
}
