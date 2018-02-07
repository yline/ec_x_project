package com.yline.file.module.fileclassify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.file.R;

/**
 * 视频类型
 *
 * @author yline 2018/2/7 -- 19:41
 * @version 1.0.0
 */
public class VideoTypeActivity extends BaseAppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, VideoTypeActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_type);

        initView();
        initData();
    }

    private void initView() {
        initViewClick();
    }

    private void initViewClick() {

    }

    private void initData() {

    }
}
