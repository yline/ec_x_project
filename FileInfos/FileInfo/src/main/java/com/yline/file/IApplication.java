package com.yline.file;

import com.yline.application.BaseApplication;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.file.module.file.helper.FileInfoLoadService;

/**
 * 程序入口
 *
 * @author yline 2018/1/25 -- 13:41
 * @version 1.0.0
 */
public class IApplication extends BaseApplication {
    public static final String TAG = "FileInfos";

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化数据库
//        DbFileBeanManager.getInstance().init(this);

        FileDbManager.init(this);

        // 开启Service服务,准备缓存文件
        FileInfoLoadService.launcher(this, true);
    }
}