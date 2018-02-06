package com.yline.file.module.file.helper;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yline.file.common.IntentUtils;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;

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
public class FileInfoDbLoader extends AsyncTask<String, Void, List<FileInfoModel>> {
    public static void getFileList(String path, OnLoadListener listener) {
        LogFileUtil.v("path = " + path);
        FileInfoDbLoader dbLoader = new FileInfoDbLoader();
        dbLoader.setLoadListener(listener);
        dbLoader.execute(path);
    }

    private OnLoadListener loadListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<FileInfoModel> doInBackground(String... params) {
        String path = params[0];
        LogFileUtil.v(path);

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
                        model = new FileInfoModel(file.getName(), file.getAbsolutePath(), FileSizeUtil.getFileOrDirAutoSize(file), IntentUtils.FileType.UNKNOW.getFid());
                        FileDbManager.insertOrReplace(model);
                    }
                    resultList.add(model);
                }
            }
        }

        return resultList;
    }

    @Override
    protected void onPostExecute(List<FileInfoModel> been) {
        super.onPostExecute(been);

        if (null != loadListener) {
            loadListener.onLoadFinish(been);
        }
    }

    public void setLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public interface OnLoadListener {
        void onLoadFinish(@NonNull List<FileInfoModel> fileBeanList);
    }
}
