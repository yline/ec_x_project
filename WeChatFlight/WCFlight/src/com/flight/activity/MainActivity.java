package com.flight.activity;

import com.yline.base.BaseFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * 学习blog系列：
 * himi:
 * http://blog.csdn.net/xiaominghimi/article/category/762640/2
 * @author yline
 * @date 2016-4-2
 */
public class MainActivity extends BaseFragmentActivity
    implements MainFlight.OnGameOverCallback, MainDialogFragment.AlterInputListener
{
    
    private MainSurfaceView mMainSurfaceView;
    
    private DialogFragment mAlertDialog;
    
    private long mScore;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // 隐去电池等图标和一切修饰部分（状态栏部分） 
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mMainSurfaceView = new MainSurfaceView(this);
        setContentView(mMainSurfaceView);
        
        MainFlight.getInstance().setOnGameOverCallback(this);
        mAlertDialog = new MainDialogFragment();
    }
    
    @Override
    public void result(long score)
    {
        mMainSurfaceView.gameStop();
        
        mAlertDialog.show(getSupportFragmentManager(), "tag");
        mAlertDialog.setCancelable(false);
        
        mScore = score;
    }
    
    @Override
    public void onAlertInputComplete(boolean isRestart)
    {
        if (isRestart)
        {
            // 重新开始游戏	简单点,就退出游戏好了
            StartActivity.setResult(this, mScore);
            finish();
        }
        else
        {
            StartActivity.setResult(this, mScore); // 退出游戏
            finish();
        }
    }
}
