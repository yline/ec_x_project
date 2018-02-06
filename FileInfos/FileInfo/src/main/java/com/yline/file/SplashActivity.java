package com.yline.file;

import android.os.Bundle;
import android.view.View;

import com.yline.file.common.FileType;
import com.yline.file.module.file.db.FileDbManager;
import com.yline.file.module.file.helper.FileInfoLoadService;
import com.yline.file.module.main.MainActivity;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

/**
 * 程序入口，后续加上 AD
 *
 * @author yline 2018/1/31 -- 16:37
 * @version 1.0.0
 */
public class SplashActivity extends BaseTestActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("MainActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.launcher(SplashActivity.this);
                finish();
            }
        });

        addButton("Run", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = System.currentTimeMillis();
                long oldStartTime = startTime;

                long count = FileDbManager.count(FileType.AUDIO);
                LogUtil.v("AUDIO count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.VIDEO);
                LogUtil.v("VIDEO count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.IMAGE);
                LogUtil.v("IMAGE count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.APK);
                LogUtil.v("APK count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.EXCEL);
                LogUtil.v("EXCEL count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.WORD);
                LogUtil.v("WORD count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.PDF);
                LogUtil.v("PDF count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.TEXT);
                LogUtil.v("TEXT count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.HTML);
                LogUtil.v("HTML count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();

                count = FileDbManager.count(FileType.PPT);
                LogUtil.v("PPT count = " + count + ", diffTime = " + (System.currentTimeMillis() - startTime));
                LogUtil.v("PPT count = " + count + ", diffOldTime = " + (System.currentTimeMillis() - oldStartTime));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        FileInfoLoadService.launcher(IApplication.getApplication(), false);
    }
}
