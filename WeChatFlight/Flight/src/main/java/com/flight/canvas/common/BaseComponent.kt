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
     * 1: hero 和 supply 的交互. hero 提供数据，supply 处理
     * 2: bullet 和 enemy 的交互,
     * 3: hero 和 enemy 的交互,
     *
     * @param toData 每次测量 结果
     * @param attackData 每次 组件间 交互的结果
     */
    abstract fun onThreadAttack(toData: MeasureToData, attackData: AttackData)

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