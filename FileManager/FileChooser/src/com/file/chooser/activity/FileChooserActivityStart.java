package com.file.chooser.activity;

import com.file.chooser.R;
import com.file.chooser.lib.FileChooserActivity;
import com.file.chooser.lib.FileUtils;
import com.yline.log.LogFileUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FileChooserActivityStart extends Activity
{
    private Button btn_zfilechooser;
    
    private TextView tv_zfilechooser;
    
    private final static int QUESTCODE_BTN_FILECHOOSER = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        tv_zfilechooser = (TextView)this.findViewById(R.id.tv_zfilechooser);
        btn_zfilechooser = (Button)this.findViewById(R.id.btn_zfilechooser);
        btn_zfilechooser.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(FileChooserActivityStart.this, FileChooserActivity.class);
                startActivityForResult(intent, QUESTCODE_BTN_FILECHOOSER);
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == QUESTCODE_BTN_FILECHOOSER)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Uri uri = data.getData();
                LogFileUtil.v(IApplication.TAG, "uri = " + uri.toString());
                
                tv_zfilechooser.setText("path1 = " + FileUtils.getPath(this, uri) + "\n" + "path2 = " + uri.getPath());
            }
            else
            {
                tv_zfilechooser.setText("本地不存在文件");
            }
        }
    }
    
}
