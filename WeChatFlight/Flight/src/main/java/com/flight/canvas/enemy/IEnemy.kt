package com.flight.canvas.enemy

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.SparseArray
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData
import com.flight.canvas.couter.CycleCounter
import com.flight.canvas.couter.ICounter
import com.flight.canvas.couter.LinearCounter

abstract class IEnemy(private val contextData: ContextData) {
    companion object {
        const val STATE_NORMAL = 1  // 正常
        const val STATE_HIT = 2    // 受伤
        const val STATE_BLOW_UP = 3 // 爆炸
        const val STATE_END = 4     // 结束
    }

    private val mEnemyRectF = RectF()

    protected var mHP: Int = 0 // 用来 表示 存活血量
    protected var mIsBlowUp = false   // 是否 正在处于 爆炸状态

    fun init(): IEnemy {
        val sourceRectF = getSourceRect(contextData.resources)

        val left = contextData.random.nextInt(contextData.mapWidth - sourceRectF.width().toInt()).toFloat()
        val top = -sourceRectF.height()

        mEnemyRectF.set(sourceRectF)
        mEnemyRectF.offsetTo(left, top)

        mHP = getInitHP()

        return this
    }

    protected abstract fun getSourceRect(resources: Resources): RectF

    fun changeHP(dHP: Int) {
        mHP += dHP

        if (mHP <= 0) {
            mIsBlowUp = true
        }
    }

    fun finishBlowUp() {
        mIsBlowUp = false
    }

    protected fun isOuter(): Boolean {
        // 越界
        return mEnemyRectF.top > contextData.mapHeight
    }

    fun move(dx: Float, dy: Float) {
        mEnemyRectF.offset(dx, dy)
    }

    private val counterMap = SparseArray<ICounter>()

    fun getSourceCounter(state: Int): ICounter? {
        val oldCounter = counterMap[state]
        if (null != oldCounter) return oldCounter

        val sourceArray = getSourceArray(state) ?: return null

        val newCounter = when (state) {
            STATE_NORMAL, STATE_HIT -> CycleCounter(sourceArray)
            STATE_BLOW_UP -> LinearCounter(sourceArray)
            else -> null
        }

        // 存数据
        newCounter?.let { counterMap.put(state, it) }
        return newCounter
    }

    protected abstract fun getSourceArray(state: Int): IntArray?

    abstract fun getInitScore(): Int

    fun getCurrentHP(): Int {
        return mHP
    }

    protected abstract fun getInitHP(): Int

    abstract fun getEnemyState(): Int

    fun clone(): IEnemy {
        return clone(contextData)
    }

    protected abstract fun clone(contextData: ContextData): IEnemy

    fun draw(canvas: Canvas, paint: Paint, sourceId: Int) {
        val bitmap = BitmapManager.newBitmap(contextData.resources, sourceId)
        canvas.drawBitmap(bitmap, mEnemyRectF.left, mEnemyRectF.top, paint)
    }


    fun getRectF() = mEnemyRectF
}