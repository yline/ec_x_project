package com.flight.canvas.common

import android.content.Context
import android.graphics.Canvas
import com.yline.utils.provider.Provider

abstract class BaseComponent {
    /**
     * 主线程 初始化
     */
    abstract fun onMainInit(context: Context)

    /**
     * 子线程 计算
     * @diffHeight 每次移动的高度
     */
    abstract fun onThreadMeasure(diffHeight: Float)

    /**
     * 子线程 绘制
     * @canvas  画布
     */
    abstract fun onThreadDraw(canvas: Canvas)

    fun <T> acquire(clz: Class<T>): T? {
        return Provider.acquire(clz)
    }

    fun provide(any: Any) {
        Provider.provide(any)
    }
}