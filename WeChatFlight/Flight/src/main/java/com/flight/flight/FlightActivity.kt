package com.flight.flight

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.flight.activity.MainFlight
import com.flight.activity.MainFlight.OnGameOverCallback
import com.flight.start.StartActivity
import com.yline.base.BaseFragmentActivity

/**
 * 学习blog系列：
 * himi:
 * http://blog.csdn.net/xiaominghimi/article/category/762640/2
 * @author yline
 * @date 2016-4-2
 */
class FlightActivity : BaseFragmentActivity(), OnGameOverCallback {
    companion object {
        fun launchForResult(activity: Activity?, requestCode: Int) {
            if (null != activity) {
                val intent = Intent()
                intent.setClass(activity, FlightActivity::class.java)
                activity.startActivityForResult(intent, requestCode)
            }
        }
    }

    private var mMainSurfaceView: FlightSurfaceView? = null
    private var mScore: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // 隐去电池等图标和一切修饰部分（状态栏部分） 
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mMainSurfaceView = FlightSurfaceView(this)
        setContentView(mMainSurfaceView)

        MainFlight.instance.setOnGameOverCallback(this)
    }

    override fun result(score: Long) {
        mMainSurfaceView!!.gameStop()

        FlightStopDialog.newInstance()
                .registerListener { isRestart ->
                    if (isRestart) {
                        // 重新开始游戏	简单点,就退出游戏好了
                        StartActivity.setResult(this, mScore)
                        finish()
                    } else {
                        StartActivity.setResult(this, mScore) // 退出游戏
                        finish()
                    }
                }
                .show(supportFragmentManager, "dialog")

        mScore = score
    }
}