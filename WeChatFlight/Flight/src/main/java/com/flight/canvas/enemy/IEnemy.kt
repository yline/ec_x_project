package com.flight.canvas.enemy

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData
import com.flight.canvas.couter.ICounter

abstract class IEnemy(private val contextData: ContextData) {
    companion object {
        const val STATE_NORMAL = 1  // 正常
        const val STATE_HIT = 2    // 受伤
        const val STATE_BLOW_UP = 3 // 爆炸
        const val STATE_END = 4     // 结束
    }

    private val mEnemyRectF = RectF()

    private var mHP: Int = 0

    fun init(): IEnemy {
        val bitmap = BitmapManager.newBitmap(contextData.resources, getSourceArray(STATE_NORMAL).default())

        val left = contextData.random.nextInt(contextData.mapWidth - bitmap.width).toFloat()
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
        if (mEnemyRectF.top > contextData.mapHeight) {
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

    abstract fun getSourceArray(state: Int): ICounter

    abstract fun getScore(): Int

    abstract fun getHP(): Int

    abstract fun getEnemyState(): Int

    fun clone(): IEnemy {
        return clone(contextData)
    }

    fun draw(canvas: Canvas, paint: Paint, sourceId: Int) {
        val bitmap = BitmapManager.newBitmap(contextData.resources, sourceId)
        canvas.drawBitmap(bitmap, mEnemyRectF.left, mEnemyRectF.top, paint)
    }

    protected abstract fun clone(contextData: ContextData): IEnemy

    fun getRectF() = mEnemyRectF
}