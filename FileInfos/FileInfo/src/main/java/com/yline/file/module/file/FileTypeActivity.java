package com.yline.file.module.file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yline.base.BaseAppCompatActivity;
import com.yline.file.R;

/**
 * 查看文件类型信息
 *
 * @author yline 2018/2/6 -- 10:01
 * @version 1.0.0
 */
public class FileTypeActivity extends BaseAppCompatActivity {
    public static void launcher(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, FileTypeActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_type);

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
