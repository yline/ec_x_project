package com.yline.file;

import android.os.Bundle;
import android.view.View;

import com.yline.file.module.file.helper.FileInfoLoadService;
import com.yline.file.module.main.MainActivity;
import com.yline.test.BaseTestActivity;

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

//                ClassifyManager.loadVideo(IApplication.getApplication());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        FileInfoLoadService.launcher(IApplication.getApplication(), false);
    }
}
