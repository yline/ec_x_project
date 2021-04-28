package com.flight.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.project.wechatflight.R
import com.yline.application.SDKManager
import com.yline.base.BaseActivity
import kotlinx.android.synthetic.main.activity_start.*

/**
 * 经验教训几条:
 * 第一:时间类非常重要,一定要封装一个到Activity下面去,单独开一个线程
 * 第二:计数器,就是Counter
 * 第三:准备好一些可以使用的场景,例如dialog,选择情形的demo
 *
 *
 * 第四:再一次体会到了解耦的重要性
 *
 *
 * 接下来,解耦做一些demo吧
 *
 * @author yline
 * @date 2016-4-20
 */
class StartActivity : BaseActivity() {
    private var mBtnIn: Button? = null
    private var mBtnOut: Button? = null
    private var mBtnHistoryScore: Button? = null
    private var mBtnOther: Button? = null
    private val request_code = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        initView()

        btn_in.setOnClickListener {
            startActivityForResult(Intent(this, MainActivity::class.java), request_code)
        }

        /*mBtnIn!!.setOnClickListener {
            startActivityForResult(Intent(this@StartActivity, MainActivity::class.java), request_code)
        }
        mBtnOut!!.setOnClickListener { finish() }
        mBtnHistoryScore!!.setOnClickListener { toast("功能未开通 HistoryScore") }
        mBtnOther!!.setOnClickListener { toast("功能未开通 Other") }*/
    }

    private fun initView() {
        mBtnIn = findViewById<View>(R.id.btn_in) as Button
        mBtnOut = findViewById<View>(R.id.btn_out) as Button
        mBtnHistoryScore = findViewById<View>(R.id.btn_history_score) as Button
        mBtnOther = findViewById<View>(R.id.btn_other) as Button
    }

    private var lastContent = ""
    fun toast(content: String) {
        if (lastContent == content) {
            // do nothing
        } else {
            lastContent = content
            SDKManager.toast(content)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (null != data) {
            if (requestCode == request_code) {
                if (resultCode == result_code) {
                    SDKManager.toast("分数为" + data.getLongExtra("game", 0))
                }
            }
        }
    }

    companion object {
        private const val result_code = 0
        fun setResult(activity: Activity, score: Long) {
            activity.setResult(result_code, Intent().putExtra("game", score))
        }
    }
}