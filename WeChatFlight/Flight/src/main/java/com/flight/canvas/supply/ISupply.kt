package com.flight.canvas.supply

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.flight.canvas.common.BitmapManager
import com.flight.canvas.common.InitData
import com.project.wechatflight.R
import java.util.*

abstract class ISupply(private val resources: Resources, private val random: Random, private val initData: InitData) {

    private var mLeft: Float = 0.0f
    private var mTop: Float = 0.0f

    private val mSupplyRect = Rect()
    private var isAttacked = false

    fun init(): ISupply {
        val bitmap = BitmapManager.newBitmap(resources, getSourceId())

        mLeft = random.nextInt(initData.mapWidth - bitmap.width).toFloat()
        mTop = -bitmap.height.toFloat()
        return this
    }

    fun move(dx: Float, dy: Float) {
        mLeft += dx
        mTop += dy
    }

    fun draw(canvas: Canvas, paint: Paint) {
        val bitmap = BitmapManager.newBitmap(resources, getSourceId())
        canvas.drawBitmap(bitmap, mLeft, mTop, paint)
    }

    fun isAttack(heroRect: Rect): Boolean {
        if (isAttacked) {
            return false
        }

        mSupplyRect.offsetTo(mLeft.toInt(), mTop.toInt())
        if (Rect.intersects(heroRect, mSupplyRect)) {
            isAttacked = true
            return true
        }
        return false
    }

    fun clone(): ISupply {
        return clone(resources, random, initData).init()
    }

    protected abstract fun clone(resources: Resources, random: Random, initData: InitData): ISupply

    protected abstract fun getSourceId(): Int

    /**
     * 被销毁
     * 1: 供给越界
     * 2: 被 战机吃掉 了
     */
    fun isDestroy(): Boolean {
        return (mTop > initData.mapHeight) || isAttacked
    }
}