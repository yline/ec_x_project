package com.calendar.countdown

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.calendar.R
import com.calendar.countdown.view.BigCountDownView
import com.calendar.util.LunarCalendar
import com.yline.base.BaseActivity

/**
 * 倒计时
 * @author yline 2018/5/23 -- 9:07
 * @version 1.0.0
 */
class CountDownActivity : BaseActivity() {
    companion object {
        fun launcher(context: Context) {
            val intent = Intent()
            intent.setClass(context, CountDownActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mCountDownView: BigCountDownView

    private val mHandler = @SuppressLint("HandlerLeak") object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if (msg.what == 0) {
                initData()
                sendEmptyMessageDelayed(0, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)

        initView()
    }

    private fun initView() {
        mCountDownView = findViewById<BigCountDownView>(R.id.count_down_big_view)

        mHandler.sendEmptyMessage(0)
    }

    private fun initData() {
        // 计算 闰月 日期
        val days = LunarCalendar.lunarToSolar(2020, 4, 7, true)
        mCountDownView.setData(days[0], days[1], days[2], 8, 0, 0)
    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}