package com.yline.file.module.file.helper;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yline.file.IApplication;
import com.yline.file.common.FileThreadPool;
import com.yline.file.common.FileType;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;
import com.yline.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 这里负责加载数据
 * 缓存结束:则读取缓存
 * 未结束:读取现场
 *
 * @author yline 2017/1/28 --> 11:47
 * @version 1.0.0
 */
public class FileInfoDbLoader {
    public static void getFileList(final String path, final OnLoadListener listener) {
        FileThreadPool.fixedThreadExecutor(new Runnable() {
            @Override
            public void run() {
                LogUtil.v("path = " + path);

                final List<FileInfoModel> resultList = loadFileList(path);
                IApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != listener) {
                            listener.onLoadFinish(resultList);
                        }
                    }
                });
            }
        });
    }

    private static List<FileInfoModel> loadFileList(String path) {
        List<FileInfoModel> resultList = new ArrayList<>();
        if (!TextUtils.isEmpty(path)) {
            final File pathDir = new File(path);
            final File[] dirs = pathDir.listFiles(FileUtil.getsDirFilter());
            if (null != dirs) {
                Arrays.sort(dirs, FileUtil.getsComparator());

                for (File dirFile : dirs) {
                    FileInfoModel model = FileDbManager.loadFileModel(dirFile.getAbsolutePath() + File.separator);
                    // 更新数据
                    if (null == model) {
                        model = new FileInfoModel(dirFile.getName(), dirFile.getAbsolutePath(),
                                dirFile.listFiles(FileUtil.getsDirFilter()).length,
                                dirFile.listFiles(FileUtil.getsFileFilter()).length, FileSizeUtil.getErrorSize());
                    }
                    resultList.add(model);
                }
            }

            final File[] files = pathDir.listFiles(FileUtil.getsFileFilter());
            if (null != files) {
                Arrays.sort(files, FileUtil.getsComparator());

                for (File file : files) {
                    FileInfoModel model = FileDbManager.loadFileModel(file.getAbsolutePath());
                    // 更新数据
                    if (null == model) {
                        model = new FileInfoModel(file.getName(), file.getAbsolutePath(), FileSizeUtil.getFileOrDirAutoSize(file), FileType.UNKNOW.getFid());
                        FileDbManager.insertOrReplace(model);
                    }
                    resultList.add(model);
                }
            }
        }

        return resultList;
    }

    public interface OnLoadListener {
        /**
         * 加载结束
         *
         * @param fileModelList 数据结果
         */
        void onLoadFinish(@NonNull List<FileInfoModel> fileModelList);
    }
}
