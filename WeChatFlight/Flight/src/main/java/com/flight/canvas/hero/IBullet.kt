package com.flight.canvas.hero

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

interface IBullet {
    /**
     * 开火
     * @param x    开始位置X		中心
     * @param y    开始位置y		中心
     */
    fun fireBullet(x: Int, y: Int, durateTime: Float, frizTime: Float): Boolean

    /**
     * 负责自身的移动速度 + 出界处理
     * @param dx    刷新一次移动的x
     * @param dy    刷新一次移动的高度
     */
    fun caculateHeroBullet(dx: Float, dy: Float)

    fun drawBullet(canvas: Canvas, paint: Paint?)

    val isEnd: Boolean

    /** 例如撞击前必须要 判断 normal  */
    val isRunning: Boolean

    fun disppear()

    val rect: Rect?

    /**
     * @return    伤害度
     */
    var atk: Int
}