package com.flight.canvas.common

import android.graphics.Canvas
import com.yline.utils.provider.Provider

abstract class BaseComponent {
    /**
     * 主线程 初始化
     */
    abstract fun onMainInit(contextData: ContextData)

    /**
     * @param contextData
     * @param toData 每次测量 结果
     */
    abstract fun onThreadMeasure(contextData: ContextData, toData: MeasureToData)

    /**
     * 子线程 绘制
     * @param canvas  画布
     * @param contextData 每次 组件间 交互的结果
     */
    abstract fun onThreadDraw(canvas: Canvas, contextData: ContextData)

    fun <T> acquire(clz: Class<T>): T? {
        return Provider.acquire(clz)
    }

    fun provide(any: Any) {
        Provider.provide(any)
    }
}