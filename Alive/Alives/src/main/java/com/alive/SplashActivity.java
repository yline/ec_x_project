package com.alive;

import android.os.Bundle;
import android.view.View;

import com.alive.sample.BActivity;
import com.yline.test.BaseTestActivity;

/**
 * 程序入口
 *
 * @author YLine 2016/8/21 --> 1:34
 * @version 1.0.0
 */
public class SplashActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("To B", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BActivity.launcher(SplashActivity.this);
            }
        });
    }
}

