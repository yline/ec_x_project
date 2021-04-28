package com.flight.canvas.supply

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

interface ISupply {
    /**
     * 开始补给
     * @param durateTime    每次刷新的时间
     * @param frizTime        每次出现的间隔
     * @return
     */
    fun start(durateTime: Float, frizTime: Float): Boolean

    /**
     * 计算
     * @param dx    刷新一次移动的x
     * @param dy    刷新一次移动的y
     */
    fun caculate(dx: Float, dy: Float)

    /**
     * 绘图
     * @param canvas
     * @param paint
     */
    fun draw(canvas: Canvas, paint: Paint?)
    val isOut: Boolean
    val isNormal: Boolean
    val rect: Rect?

    /**
     * 这个是设置成透明,而不是消失
     */
    fun disppear()
}