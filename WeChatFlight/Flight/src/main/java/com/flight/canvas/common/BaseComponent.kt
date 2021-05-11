package com.flight.canvas.common

import android.content.Context
import android.graphics.Canvas
import com.yline.utils.provider.Provider

abstract class BaseComponent {
    /**
     * 主线程 初始化
     */
    abstract fun onMainInit(context: Context, initData: InitData)

    /**
     * 子线程
     * @diffHeight 每次移动的高度
     */
    /**
     * @param fromData 每次测量 起源数据
     * @param toData 每次测量 结果
     */
    abstract fun onThreadMeasure(fromData: MeasureFromData, toData: MeasureToData)

    /**
     * 子线程 绘制
     * @param canvas  画布
     * @param attackData 每次 组件间 交互的结果
     */
    abstract fun onThreadDraw(canvas: Canvas, attackData: AttackData)

    fun <T> acquire(clz: Class<T>): T? {
        return Provider.acquire(clz)
    }

    fun provide(any: Any) {
        Provider.provide(any)
    }
}