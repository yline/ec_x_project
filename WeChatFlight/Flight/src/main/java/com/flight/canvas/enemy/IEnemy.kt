package com.flight.canvas.enemy

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

interface IEnemy {
    fun start(durateTime: Float, frizTime: Float): Boolean

    fun caculate(durateTime: Float, dx: Float, dy: Float)

    /** 绘制有图片时候的状态  */
    fun draw(canvas: Canvas, paint: Paint?)

    /**
     * 被撞击
     * @param atk    杀伤力
     * @return    获取的分数
     */
    fun hitted(atk: Int): Int

    /**
     * 开始自爆状态
     */
    fun blowUp()

    /**
     * 是否结束,就是状态为end
     * @return
     */
    val isEnd: Boolean

    /**
     * 是否在运行状态,正常和被撞击(为销毁时)
     * @return
     */
    val isRunning: Boolean

    val rect: Rect?

    val score: Int
}