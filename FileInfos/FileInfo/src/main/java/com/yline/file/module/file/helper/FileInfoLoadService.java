package com.yline.file.module.file.helper;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yline.file.IApplication;
import com.yline.file.common.IntentUtils;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.file.common.SpManager;
import com.yline.file.module.file.model.FileInfoModel;
import com.yline.log.LogFileUtil;
import com.yline.utils.FileSizeUtil;
import com.yline.utils.FileUtil;
import com.yline.utils.LogUtil;
import com.yline.utils.PermissionUtil;

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
public class FileInfoLoadService extends IntentService {
    /**
     * 是否需要手动请求权限
     */
    public static void launcher(Context context, boolean isCheckPermission) {
        boolean isRequest = isCheckPermission && PermissionUtil.check(context, Manifest.permission.WRITE_EXTERNAL_STORAGE); // 判断是否需要请求权限
        if (!isRequest) {
            context.startService(new Intent(context, FileInfoLoadService.class));
        } else {
            LogUtil.v("launcher, should request permission first");
        }
    }

    private List<FileInfoModel> mFileModelList;

    public FileInfoLoadService() {
        super("FileInfoLoadService");

        mFileModelList = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.v("FileInfoLoadService onCreate");
    }

    @Override
    public void onDestroy() {
        SpManager.setIsFileInfoLoading(false); // 防止意外发生
        LogUtil.v("FileInfoLoadService onDestroy");

        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // 判断是否进行 加载文件
        boolean isStartLoad = SpManager.isStartLoad();
        LogFileUtil.v("isStartLoad = " + isStartLoad);
        if (isStartLoad && !SpManager.isFileInfoLoading()) {
            SpManager.setIsFileInfoLoading(true);

            long startTime = System.currentTimeMillis();
            String topPath = FileUtil.getPathTop();
            LogUtil.v("onHandleIntent, topPath = " + topPath + ", startTime = " + startTime);

            if (!TextUtils.isEmpty(topPath)) {
                final File topFile = new File(topPath);
                long totalSize = getFileSize(topFile);

                LogUtil.v("onHandleIntent, totalSize = " + totalSize);
                if (totalSize == FileSizeUtil.getErrorSize()) {
                    IApplication.toast("读取内部存储失败");
                    SpManager.setIsFileInfoLoading(false);
                    return;
                }
            } else {
                IApplication.toast("内部存储不存在");
                SpManager.setIsFileInfoLoading(false);
                return;
            }

            LogUtil.v("onHandleIntent, readTime = " + (System.currentTimeMillis() - startTime) + ", count = " + mFileModelList.size());
            startTime = System.currentTimeMillis();

            FileDbManager.deleteAll();
            LogUtil.v("onHandleIntent, deleteTime = " + (System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();

            FileDbManager.insertOrReplaceInTx(mFileModelList);
            LogUtil.v("onHandleIntent, writeTime = " + (System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();

            SpManager.setLastLoadTime(startTime);
            SpManager.setIsFileInfoLoading(false);

            FileInfoLoadReceiver.sendReceiver();
        }
    }

    /**
     * 读取文件大小
     *
     * @param topFile 顶层目录
     * @return 返回数据
     */
    private long getFileSize(File topFile) {
        long tempSize, totalSize = 1;

        String absolutePath;
        File[] childFileArray = (null != topFile && topFile.isDirectory()) ? topFile.listFiles() : null;
        if (null != childFileArray) {
            for (File childFile : childFileArray) {
                if (childFile.isDirectory()) {
                    tempSize = getFileSize(childFile);
                    tempSize = (tempSize == FileSizeUtil.getErrorSize() ? 1 : tempSize);

                    totalSize += tempSize;
                } else {
                    tempSize = FileSizeUtil.getFileSize(childFile);
                    tempSize = (tempSize == FileSizeUtil.getErrorSize() ? 1 : tempSize);

                    totalSize += tempSize;
                    absolutePath = childFile.getAbsolutePath();
                    mFileModelList.add(new FileInfoModel(childFile.getName(), absolutePath, tempSize, IntentUtils.getFileType(absolutePath).getFid()));
                }
            }

            mFileModelList.add(new FileInfoModel(topFile.getName(), topFile.getAbsolutePath(),
                    getChildDirCount(topFile), getChildFileCount(topFile), totalSize));
            return totalSize;
        }
        return FileSizeUtil.getErrorSize();
    }

    private int getChildDirCount(@NonNull File topFile) {
        File[] fileArray = topFile.listFiles(FileUtil.getsDirFilter());
        return (null == fileArray ? 0 : fileArray.length);
    }

    private int getChildFileCount(@NonNull File topFile) {
        File[] fileArray = topFile.listFiles(FileUtil.getsFileFilter());
        return (null == fileArray ? 0 : fileArray.length);
    }
}
