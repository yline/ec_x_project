package com.flight.canvas.supply

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.InitData
import java.util.*

abstract class ISupply(private val resources: Resources, private val random: Random, private val initData: InitData) {
    // 保证 精准度
    private val mSupplyRect = RectF()

    private var isAttacked = false

    fun init(): ISupply {
        val bitmap = BitmapManager.newBitmap(resources, getSourceId())

        val left = random.nextInt(initData.mapWidth - bitmap.width).toFloat()
        val top = -bitmap.height.toFloat()

        mSupplyRect.set(left, top, left + bitmap.width.toFloat(), top + bitmap.height.toFloat())
        return this
    }

    fun move(dx: Float, dy: Float) {
        mSupplyRect.offset(dx, dy)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        val bitmap = BitmapManager.newBitmap(resources, getSourceId())
        canvas.drawBitmap(bitmap, mSupplyRect.left, mSupplyRect.top, paint)
    }

    fun isAttack(heroRect: RectF): Boolean {
        if (isAttacked) {
            return false
        }

        if (RectF.intersects(heroRect, mSupplyRect)) {
            isAttacked = true
            return true
        }
        return false
    }

    fun clone(): ISupply {
        return clone(resources, random, initData)
    }

    protected abstract fun clone(resources: Resources, random: Random, initData: InitData): ISupply

    protected abstract fun getSourceId(): Int

    /**
     * 被销毁
     * 1: 供给越界
     * 2: 被 战机吃掉 了
     */
    fun isDestroy(): Boolean {
        return (mSupplyRect.top > initData.mapHeight) || isAttacked
    }

    fun getRectF() = mSupplyRect
}