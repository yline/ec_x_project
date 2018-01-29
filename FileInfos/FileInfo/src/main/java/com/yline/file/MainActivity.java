package com.yline.file;

import android.os.Bundle;
import android.view.View;

import com.yline.file.module.file.FileInfoActivity;
import com.yline.file.module.file.FileListActivity;
import com.yline.file.module.file.helper.FileLoadService;
import com.yline.test.BaseTestActivity;

/**
 * 程序入口
 *
 * @author yline 2018/1/25 -- 13:42
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("FileListActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileListActivity.launcher(MainActivity.this);
            }
        });

        addButton("FileInfoActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInfoActivity.launcher(MainActivity.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        FileLoadService.launcher(IApplication.getApplication(), false);
    }
}
