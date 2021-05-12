package com.flight.canvas.supply

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData

abstract class ISupply(private val contextData: ContextData) {
    // 保证 精准度
    private val mSupplyRect = RectF()

    var isAttacked = false

    fun init(): ISupply {
        val bitmap = BitmapManager.newBitmap(contextData.resources, getSourceId())

        val left = contextData.random.nextInt(contextData.mapWidth - bitmap.width).toFloat()
        val top = -bitmap.height.toFloat()

        mSupplyRect.set(left, top, left + bitmap.width.toFloat(), top + bitmap.height.toFloat())
        return this
    }

    fun move(dx: Float, dy: Float) {
        mSupplyRect.offset(dx, dy)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        val bitmap = BitmapManager.newBitmap(contextData.resources, getSourceId())
        canvas.drawBitmap(bitmap, mSupplyRect.left, mSupplyRect.top, paint)
    }

    fun clone(): ISupply {
        return clone(contextData)
    }

    protected abstract fun clone(contextData: ContextData): ISupply

    protected abstract fun getSourceId(): Int

    /**
     * 被销毁
     * 1: 供给越界
     * 2: 被 战机吃掉 了
     */
    fun isDestroy(): Boolean {
        return (mSupplyRect.top > contextData.mapHeight) || isAttacked
    }

    fun getRectF() = mSupplyRect
}