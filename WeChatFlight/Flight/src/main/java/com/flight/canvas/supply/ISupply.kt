package com.flight.canvas.supply

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.flight.canvas.common.InitData
import java.util.*

abstract class ISupply(private val bitmap: Bitmap, private val random: Random, private val initData: InitData) {

    private var mLeft: Float = random.nextInt(initData.mapWidth - bitmap.width).toFloat()
    private var mTop: Float = -bitmap.height.toFloat()

    private val mSupplyRect = Rect()
    private var isAttacked = false

    fun move(dx: Float, dy: Float) {
        mLeft += dx
        mTop += dy
    }

    fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(bitmap, mLeft, mTop, paint)
    }

    fun isAttack(heroRect: Rect): Boolean {
        if (isAttacked) {
            return false
        }

        mSupplyRect.set(mLeft.toInt(), mTop.toInt(), mLeft.toInt() + bitmap.width, mTop.toInt() + bitmap.height)
        if (Rect.intersects(heroRect, mSupplyRect)) {
            isAttacked = true
            return true
        }
        return false
    }

    fun clone(): ISupply {
        return Supply1(bitmap, random, initData)
    }

    /**
     * 被销毁
     * 1: 供给越界
     * 2: 被 战机吃掉 了
     */
    fun isDestroy(): Boolean {
        return (mTop > initData.mapHeight) || isAttacked
    }
}