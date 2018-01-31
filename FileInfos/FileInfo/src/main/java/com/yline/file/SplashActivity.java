package com.yline.file;

import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.file.module.main.MainActivity;

/**
 * 程序入口，后续加上 AD
 *
 * @author yline 2018/1/31 -- 16:37
 * @version 1.0.0
 */
public class SplashActivity extends BaseAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.launcher(SplashActivity.this);
        finish();
    }
}
