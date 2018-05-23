package com.calendar.countdown.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.calendar.R
import com.yline.view.recycler.holder.ViewHolder
import java.text.DecimalFormat
import java.util.*

/**
 * 较长时间的，倒计时
 * 设置一次数据展示一次
 * @author yline 2018/5/23 -- 9:09
 * @version 1.0.0
 */
class BigCountDownView : RelativeLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    companion object {
        const val TIME_UP = "时间到"
    }

    private lateinit var mViewHolder: ViewHolder
    private var mDiffTime: Long = 0

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_big_count_down, this, true)
        mViewHolder = ViewHolder(view)
    }

    /**
     * 公历的时间
     */
    public fun setData(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) {
        val calendar = Calendar.getInstance(Locale.CHINA)
        calendar.set(year, month, day, hour, minute, second)

        mDiffTime = calendar.timeInMillis - System.currentTimeMillis()
        if (mDiffTime < 0) {
            setTitle(TIME_UP)
        }
        setContainerData(mDiffTime)
    }

    public fun setTitle(title: String) {
        mViewHolder.setText(R.id.big_count_down_title, (if (mDiffTime < 0) TIME_UP else title))
    }

    /**
     * 截止的时间戳，unit: ms
     */
    private fun setContainerData(diffMillis: Long) {
        if (diffMillis > 0) {
            mViewHolder.get<View>(R.id.big_count_down_container).visibility = View.VISIBLE

            val decimalFormat = DecimalFormat(",####")

            val diffSecond = diffMillis / 1000
            mViewHolder.setText(R.id.big_count_down_second_tv, decimalFormat.format(diffSecond))

            val diffMinute = diffSecond / 60
            mViewHolder.setText(R.id.big_count_down_minute_tv, decimalFormat.format(diffMinute))

            val diffHour = diffMinute / 60
            mViewHolder.setText(R.id.big_count_down_hour_tv, decimalFormat.format(diffHour))

            val diffDay = diffHour / 24
            mViewHolder.setText(R.id.big_count_down_day_tv, decimalFormat.format(diffDay))
        } else {
            mViewHolder.get<View>(R.id.big_count_down_container).visibility = View.GONE
        }
    }

    private fun attach(number: Int): String {
        return DecimalFormat(",###").format(number)
    }
}