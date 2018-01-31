package com.yline.file;

import android.os.Bundle;
import android.view.View;

import com.yline.file.module.file.FileInfoActivity;
import com.yline.file.module.file.helper.FileInfoLoadService;
import com.yline.test.BaseTestActivity;
import com.yline.utils.FileUtil;

/**
 * 程序入口
 *
 * @author yline 2018/1/25 -- 13:42
 * @version 1.0.0
 */
public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("FileInfoActivity", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topPath = FileUtil.getPathTop();
                FileInfoActivity.launcher(MainActivity.this, topPath);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        FileInfoLoadService.launcher(IApplication.getApplication(), false);
    }
}
