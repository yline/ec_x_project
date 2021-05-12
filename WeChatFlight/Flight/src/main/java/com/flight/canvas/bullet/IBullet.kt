package com.flight.canvas.bullet

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData

abstract class IBullet(private val contextData: ContextData) {
    private val mBulletRect = RectF()

    var isAttacked = false

    fun init(heroRect: RectF): IBullet {
        val bitmap = BitmapManager.newBitmap(contextData.resources, getSourceId())

        val left = heroRect.centerX() - (bitmap.width.toFloat() / 2)
        val top = heroRect.top - (bitmap.height.toFloat() / 2)

        mBulletRect.set(left, top, left + bitmap.width, top + bitmap.height)
        return this
    }

    fun move(dx: Float, dy: Float) {
        mBulletRect.offset(dx, dy)
    }

    fun draw(canvas: Canvas, paint: Paint) {
        val bitmap = BitmapManager.newBitmap(contextData.resources, getSourceId())
        canvas.drawBitmap(bitmap, mBulletRect.left, mBulletRect.top, paint)
    }

    fun clone(): IBullet {
        return clone(contextData)
    }

    protected abstract fun clone(contextData: ContextData): IBullet

    protected abstract fun getSourceId(): Int

    abstract fun getATK(): Int

    /**
     * 被销毁
     * 1: 供给越界
     * 2: 被 战机吃掉 了
     */
    fun isDestroy(): Boolean {
        if (mBulletRect.bottom < 0) {
            return true
        }

        if (isAttacked) {
            return true
        }

        return false
    }

    fun getRectF() = mBulletRect
}