package com.yline.file.module.file.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.yline.file.IApplication;
import com.yline.file.module.db.DbFileBeanManager;
import com.yline.file.module.file.model.FileModel;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;
import com.yline.utils.SPUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库更新采取两种措施:
 * 1,遍历数据时,如果发现不存在在数据库中的文件夹,则进行小批量更新数据库
 * 2,每间隔一个礼拜,进行扫描一次,并且扫描时,并且采取的是,完全替换表的方式
 *
 * @author yline 2017/1/30 --> 0:54
 * @version 1.0.0
 */
public class FileLoadService extends Service {
    private static final String KEY_FILE_NAME = "FileLoadService";

    /**
     * 记录是否更新完毕,替换表时,读取不到文件大小
     */
    private static final String KEY_IS_CACHE = "FileLoad_Switch";

    /**
     * 记录新的更新时间
     */
    private static final String KEY_LAST_SCAN_TIME = "LastScanTime";

    /**
     * 更新间隔时间
     */
    private static final long UPDATE_DURATION = 86400 * 1000 * 7; // ms

    private boolean isStartCache() {
        long oldTime = (long) SPUtil.get(this, KEY_LAST_SCAN_TIME, 0L, KEY_FILE_NAME);
        if (System.currentTimeMillis() - oldTime > UPDATE_DURATION) {
            return true;
        }
        return false;
    }

    private void startCache() {
        setIsCached(this, false);
    }

    private void endCache() {
        setIsCached(this, true);
        SPUtil.put(this, KEY_LAST_SCAN_TIME, System.currentTimeMillis(), KEY_FILE_NAME);
    }

    private void setIsCached(Context context, boolean isCached) {
        SPUtil.put(context, KEY_IS_CACHE, isCached, KEY_FILE_NAME);
    }

    public static boolean isCached(Context context) {
        return (boolean) SPUtil.get(context, KEY_IS_CACHE, false, KEY_FILE_NAME);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 判断是否进行 加载文件
        boolean isStartCache = isStartCache();
        LogFileUtil.v("isStartCache = " + isStartCache);
        if (isStartCache) {
            new Thread(new FileLoadRunnable()).start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private class FileLoadRunnable implements Runnable {
        private static final String TAG = "FileLoadRunnable";

        private static final int ERROR_SIZE = 0;

        private List<FileModel> resultBean = new ArrayList<>();

        @Override
        public void run() {
            startCache();

            String rootPath = FileUtil.getPathTop();
            long startTime = System.currentTimeMillis();
            LogFileUtil.v("rootPath = " + rootPath + ",startTime = " + startTime);

            if (null != rootPath) {
                final File rootFile = new File(rootPath);
                long size = getDirSize(rootFile);

                LogFileUtil.v(TAG, "DirSize = " + FileSizeUtil.formatFileAutoSize(size));
            } else {
                IApplication.toast("内存空间不存在");
                LogFileUtil.v(TAG, "rootPath is null");
            }

            LogFileUtil.v(TAG, "ReadTime = " + (System.currentTimeMillis() - startTime) + ",fileNumber = " + resultBean.size());
            startTime = System.currentTimeMillis();

            DbFileBeanManager.getInstance().insertAtSameMoment(resultBean);
            LogFileUtil.v(TAG, "WriteTime = " + (System.currentTimeMillis() - startTime));

            endCache();
        }

        /**
         * 一次性,全部读取所有的信息
         */
        private long getDirSize(File file) {
            long tempSize = ERROR_SIZE;
            long totalSize = ERROR_SIZE;

            File[] childFiles = file.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                if (childFiles[i].isDirectory()) {
                    tempSize = getDirSize(childFiles[i]);
                    totalSize += tempSize;
                } else {
                    tempSize = FileSizeUtil.getFileSize(childFiles[i]);
                    tempSize = tempSize == FileSizeUtil.getErrorSize() ? 0 : tempSize;
                    totalSize += tempSize;

                    resultBean.add(new FileModel(childFiles[i].getName(), childFiles[i].getAbsolutePath(), tempSize));
                }
            }

            // 储存目录的 信息
            resultBean.add(new FileModel(file.getName(), file.getAbsolutePath(),
                    file.listFiles(FileUtil.getsDirFilter()).length,
                    file.listFiles(FileUtil.getsFileFilter()).length,
                    totalSize));

            return totalSize;
        }
    }
}
