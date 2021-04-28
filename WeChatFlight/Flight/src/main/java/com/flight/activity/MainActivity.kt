package com.flight.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.flight.activity.MainDialogFragment.AlterInputListener
import com.flight.activity.MainFlight.OnGameOverCallback
import com.yline.base.BaseFragmentActivity

/**
 * 学习blog系列：
 * himi:
 * http://blog.csdn.net/xiaominghimi/article/category/762640/2
 * @author yline
 * @date 2016-4-2
 */
class MainActivity : BaseFragmentActivity(), OnGameOverCallback, AlterInputListener {
    private var mMainSurfaceView: MainSurfaceView? = null
    private var mAlertDialog: DialogFragment? = null
    private var mScore: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 隐去电池等图标和一切修饰部分（状态栏部分） 
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mMainSurfaceView = MainSurfaceView(this)
        setContentView(mMainSurfaceView)

        MainFlight.instance.setOnGameOverCallback(this)
        mAlertDialog = MainDialogFragment()
    }

    override fun result(score: Long) {
        mMainSurfaceView!!.gameStop()
        mAlertDialog!!.show(supportFragmentManager, "tag")
        mAlertDialog!!.isCancelable = false
        mScore = score
    }

    override fun onAlertInputComplete(isRestart: Boolean) {
        if (isRestart) {
            // 重新开始游戏	简单点,就退出游戏好了
            StartActivity.Companion.setResult(this, mScore)
            finish()
        } else {
            StartActivity.Companion.setResult(this, mScore) // 退出游戏
            finish()
        }
    }
}