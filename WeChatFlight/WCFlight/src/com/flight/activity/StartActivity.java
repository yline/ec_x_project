package com.flight.activity;

import com.flight.application.MainApplication;
import com.yline.base.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import f21.wechat.flight.R;

/**
 * 经验教训几条:
 * 第一:时间类非常重要,一定要封装一个到Activity下面去,单独开一个线程
 * 第二:计数器,就是Counter
 * 第三:准备好一些可以使用的场景,例如dialog,选择情形的demo
 *
 * 第四:再一次体会到了解耦的重要性
 * 
 * 接下来,解耦做一些demo吧
 * 
 * @author yline
 * @date 2016-4-20
 */
public class StartActivity extends BaseActivity
{
    
    private Button mBtnIn, mBtnOut, mBtnHistoryScore, mBtnOther;
    
    private final int request_code = 0;
    
    private final static int result_code = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        initView();
        
        mBtnIn.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(StartActivity.this, MainActivity.class), request_code);
            }
        });
        
        mBtnOut.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        
        mBtnHistoryScore.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                toast("功能未开通 HistoryScore");
            }
        });
        
        mBtnOther.setOnClickListener(new View.OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                toast("功能未开通 Other");
            }
        });
    }
    
    private void initView()
    {
        mBtnIn = (Button)findViewById(R.id.btn_in);
        mBtnOut = (Button)findViewById(R.id.btn_out);
        mBtnHistoryScore = (Button)findViewById(R.id.btn_history_score);
        mBtnOther = (Button)findViewById(R.id.btn_other);
    }
    
    private String lastContent = "";
    
    public void toast(String content)
    {
        if (lastContent.equals(content))
        {
            // do nothing
        }
        else
        {
            lastContent = content;
            MainApplication.toast(content);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (null != data)
        {
            if (requestCode == request_code)
            {
                if (resultCode == result_code)
                {
                    MainApplication.toast("分数为" + data.getLongExtra("game", 0));
                }
            }
        }
    }
    
    public static void setResult(Activity activity, long score)
    {
        activity.setResult(result_code, new Intent().putExtra("game", score));
    }
}
