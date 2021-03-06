package com.yline.file.module.file.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yline.file.IApplication;
import com.yline.file.common.FileThreadPool;
import com.yline.file.common.FileType;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.sqlite.SQLiteIOUtils;
import com.yline.sqlite.SqliteManager;
import com.yline.sqlite.async.AsyncHelper;
import com.yline.sqlite.common.AbstractSafelyDao;
import com.yline.sqlite.dao.DaoManager;
import com.yline.utils.FileSizeUtil;

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

    public static long count(FileType fileType) {
        FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
        if (null != dbModelDao) {
            return dbModelDao.countFileType(fileType);
        }
        return 0;
    }

    public static void deleteAll() {
        FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
        if (null != dbModelDao) {
            dbModelDao.deleteAll();
        }
    }

    public static long loadFileModelForSize(String path) {
        FileInfoModel fileModel = loadFileModel(path);
        if (null != fileModel) {
            return fileModel.getFileSize();
        }
        return FileSizeUtil.getErrorSize();
    }

    public static FileInfoModel loadFileModel(String path) {
        FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
        if (null != dbModelDao) {
            FileDbModel dbModel = dbModelDao.load(path);
            if (null != dbModel) {
                Object object = SQLiteIOUtils.byte2Object(dbModel.getData());
                if (object instanceof FileInfoModel) {
                    return (FileInfoModel) object;
                }
            }
        }
        return null;
    }

    public static void loadAllAsync(final FileType fileType, final AsyncHelper.OnResultListener<List<FileInfoModel>> resultListener) {
        FileThreadPool.fixedThreadExecutor(new Runnable() {
            @Override
            public void run() {
                final FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
                if (null != resultListener) {
                    IApplication.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (null == dbModelDao) {
                                resultListener.onAsyncResult(new ArrayList<FileInfoModel>());
                            } else {
                                resultListener.onAsyncResult(dbModelDao.loadFileType(fileType));
                            }
                        }
                    });
                }
            }
        });
    }

    public static void insertOrReplace(FileInfoModel fileModel) {
        FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
        if (null != dbModelDao) {
            dbModelDao.insertOrReplace(new FileDbModel(fileModel.getAbsolutePath(), fileModel.getFileType(), fileModel.isDirectory(), SQLiteIOUtils.object2Byte(fileModel)));
        }
    }

    public static void insertOrReplaceInTx(List<FileInfoModel> list) {
        List<FileDbModel> fileDbModelList = new ArrayList<>();
        for (FileInfoModel fileModel : list) {
            byte[] value = SQLiteIOUtils.object2Byte(fileModel);
            fileDbModelList.add(new FileDbModel(fileModel.getAbsolutePath(), fileModel.getFileType(), fileModel.isDirectory(), value));
        }

        FileDbModelDao dbModelDao = (FileDbModelDao) DaoManager.getInstance().getDaoSession().getModelDao(FileDbModelDao.TABLE_NAME);
        if (null != dbModelDao) {
            dbModelDao.insertOrReplaceInTx(fileDbModelList);
        }
    }
}
