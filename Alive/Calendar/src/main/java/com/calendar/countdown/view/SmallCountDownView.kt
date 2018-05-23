package com.calendar.countdown.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.calendar.R
import com.yline.view.recycler.holder.ViewHolder

/**
 * 短时间的，倒计时
 * 有点鸡肋
 * @author yline 2018/5/23 -- 9:12
 * @version 1.0.0
 */
class SmallCountDownView : RelativeLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private lateinit var mViewHolder: ViewHolder

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_small_count_down, this, true)
        mViewHolder = ViewHolder(view)
    }

    public fun setData() {

    }
}