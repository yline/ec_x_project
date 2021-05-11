package com.flight.canvas.enemy

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.InitData
import java.util.*

abstract class IEnemy(private val resources: Resources, private val random: Random, private val initData: InitData) {
    private val mEnemyRectF = RectF()

    private var mHP: Int = 0

    fun init(): IEnemy {
        val bitmap = BitmapManager.newBitmap(resources, getSourceArray()[0])

        val left = random.nextInt(initData.mapWidth - bitmap.width).toFloat()
        val top = -bitmap.height.toFloat()

        mEnemyRectF.set(left, top, left + bitmap.width, top + bitmap.height)

        mHP = getHP()

        return this
    }

    fun isHPEmpty(): Boolean {
        return mHP <= 0
    }

    fun changeHP(dHP: Int) {
        mHP += dHP
    }

    fun isDestroy(): Boolean {
        // 越界了
        if (mEnemyRectF.top > initData.mapHeight) {
            return true
        }

        if (isHPEmpty()) {
            return true
        }

        return false
    }

    fun move(dx: Float, dy: Float) {
        mEnemyRectF.offset(dx, dy)
    }

    abstract fun getSourceArray(): IntArray

    abstract fun getScore(): Int

    abstract fun getHP(): Int

    fun clone(): IEnemy {
        return clone(resources, random, initData).init()
    }

    fun draw(canvas: Canvas, paint: Paint) {
        val bitmap = BitmapManager.newBitmap(resources, getSourceArray()[0])
        canvas.drawBitmap(bitmap, mEnemyRectF.left, mEnemyRectF.top, paint)
    }

    protected abstract fun clone(resources: Resources, random: Random, initData: InitData): IEnemy

    fun getRectF() = mEnemyRectF

//
//    abstract fun start(durateTime: Float, frizTime: Float): Boolean
//
//    abstract fun caculate(durateTime: Float, dx: Float, dy: Float)
//
//    /** 绘制有图片时候的状态  */
//    abstract fun draw(canvas: Canvas, paint: Paint?)
//
//    /**
//     * 被撞击
//     * @param atk    杀伤力
//     * @return    获取的分数
//     */
//    abstract fun hitted(atk: Int): Int
//
//    /**
//     * 开始自爆状态
//     */
//    abstract fun blowUp()
//
//    /**
//     * 是否结束,就是状态为end
//     * @return
//     */
//    abstract val isEnd: Boolean
//
//    /**
//     * 是否在运行状态,正常和被撞击(为销毁时)
//     * @return
//     */
//    abstract val isRunning: Boolean
//
//    abstract val rect: Rect?

//    abstract val score: Int
}