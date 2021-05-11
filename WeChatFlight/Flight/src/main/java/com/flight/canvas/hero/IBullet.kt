package com.flight.canvas.hero

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.InitData
import java.util.*

abstract class IBullet(private val resources: Resources, private val random: Random, private val initData: InitData) {
    private val mBulletRect = RectF()
    private var isAttacked = false

    fun init(heroRect: RectF): IBullet {
        val bitmap = BitmapManager.newBitmap(resources, getSourceId())

        val left = heroRect.centerX() - (bitmap.width.toFloat() / 2)
        val top = heroRect.top - (bitmap.height.toFloat() / 2)

        mBulletRect.set(left, top, left + bitmap.width, top + bitmap.height)
        return this
    }

    fun move(dx: Float, dy: Float) {
        mBulletRect.offset(dx, dy)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        val bitmap = BitmapManager.newBitmap(resources, getSourceId())
        canvas.drawBitmap(bitmap, mBulletRect.left, mBulletRect.top, paint)
    }

    fun isAttack(heroRect: RectF): Boolean {
        if (isAttacked) {
            return false
        }

        if (RectF.intersects(heroRect, mBulletRect)) {
            isAttacked = true
            return true
        }
        return false
    }

    fun clone(): IBullet {
        return clone(resources, random, initData)
    }

    protected abstract fun clone(resources: Resources, random: Random, initData: InitData): IBullet

    protected abstract fun getSourceId(): Int

    /**
     * 被销毁
     * 1: 供给越界
     * 2: 被 战机吃掉 了
     */
    fun isDestroy(): Boolean {
        return (mBulletRect.top > initData.mapHeight) || isAttacked
    }

    fun getRectF() = mBulletRect

//
//    /**
//     * 开火
//     * @param x    开始位置X		中心
//     * @param y    开始位置y		中心
//     */
//    fun fireBullet(x: Int, y: Int, durateTime: Float, frizTime: Float): Boolean
//
//    /**
//     * 负责自身的移动速度 + 出界处理
//     * @param dx    刷新一次移动的x
//     * @param dy    刷新一次移动的高度
//     */
//    fun caculateHeroBullet(dx: Float, dy: Float)
//
//    fun drawBullet(canvas: Canvas, paint: Paint?)
//
//    val isEnd: Boolean
//
//    /** 例如撞击前必须要 判断 normal  */
//    val isRunning: Boolean
//
//    fun disppear()
//
//    val rect: Rect?
//
//    /**
//     * @return    伤害度
//     */
//    var atk: Int
}