package com.yline.file.module.file.helper;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yline.file.IApplication;
import com.yline.file.module.db.DbFileBeanManager;
import com.yline.file.module.db.SpManager;
import com.yline.file.module.file.model.FileModel;
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
public class FileLoadService extends IntentService {
    /**
     * 是否需要手动请求权限
     */
    public static void launcher(Context context, boolean isCheckPermission) {
        boolean isRequest = isCheckPermission && PermissionUtil.check(context, Manifest.permission.WRITE_EXTERNAL_STORAGE); // 判断是否需要请求权限
        if (!isRequest) {
            context.startService(new Intent(context, FileLoadService.class));
        } else {
            LogUtil.v("launcher, should request permission first");
        }
    }

    private List<FileModel> mFileModelList;

    public FileLoadService() {
        super("FileLoadService");

        mFileModelList = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.v("FileInfoLoadService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtil.v("FileInfoLoadService onDestroy");
    }

    private List<FileModel> mResultBean = new ArrayList<>();

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

            LogUtil.v("onHandleIntent, readTime = " + (System.currentTimeMillis() - startTime) + ", count = " + mResultBean.size());
            startTime = System.currentTimeMillis();

            DbFileBeanManager.getInstance().insertAtSameMoment(mResultBean);
            LogUtil.v("onHandleIntent, writeTime = " + (System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();

            SpManager.setLastLoadTime(startTime);
            SpManager.setIsFileInfoLoading(false);
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
                    mFileModelList.add(new FileModel(childFile.getName(), childFile.getAbsolutePath(), tempSize));
                }
            }

            mFileModelList.add(new FileModel(topFile.getName(), topFile.getAbsolutePath(),
                    topFile.listFiles(FileUtil.getsDirFilter()).length, topFile.listFiles(FileUtil.getsFileFilter()).length, totalSize));
            return totalSize;
        }
        return FileSizeUtil.getErrorSize();
    }
}
