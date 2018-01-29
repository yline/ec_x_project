package com.yline.file.module.file.helper;

import com.yline.file.module.file.model.FileModel;
import com.yline.log.LogFileUtil;

import java.util.List;

/**
 * 这里负责加载数据
 * 缓存结束:则读取缓存
 * 未结束:读取现场
 *
 * @author yline 2017/1/28 --> 11:47
 * @version 1.0.0
 */
public class FileHelper {
    public static void getFileList(LoadListener listener, String path) {
        LogFileUtil.v("path = " + path);
        FileDbLoader dbLoader = new FileDbLoader();
        dbLoader.setLoadListener(listener);
        dbLoader.execute(path);
    }

    public interface LoadListener {
        void onLoadFinish(List<FileModel> fileBeanList);
    }
}
