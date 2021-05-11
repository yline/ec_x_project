package com.flight.start

import android.content.Intent
import android.os.Bundle
import com.flight.flight.FlightActivity
import com.project.wechatflight.R
import com.yline.application.SDKManager
import com.yline.base.BaseActivity
import com.yline.log.LogUtil
import kotlinx.android.synthetic.main.activity_start.*
import java.lang.Exception

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
    companion object {
        private const val request_code = 0
        private const val result_code = 0

        fun setResult(activity: FlightActivity, score: Long) {
            activity.setResult(result_code, Intent().putExtra("game", score))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btn_in.setOnClickListener {
            FlightActivity.launchForResult(this, request_code)
        }
        btn_out.setOnClickListener {
             finish()
        }

        btn_history_score.setOnClickListener {
            SDKManager.toast("功能未开通 HistoryScore")
        }
        btn_other.setOnClickListener {
            SDKManager.toast("功能未开通 Other")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            if (requestCode == request_code) {
                if (resultCode == result_code) {
                    SDKManager.toast("分数为" + data.getLongExtra("game", 0))
                }
            }
        }
    }
}


















