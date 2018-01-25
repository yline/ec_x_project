package com.yline.file;

import android.os.Bundle;
import android.view.View;

import com.yline.file.module.file.FileListActivity;
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
    }
}
