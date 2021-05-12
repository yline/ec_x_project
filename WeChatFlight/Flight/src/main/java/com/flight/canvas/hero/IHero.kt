package com.flight.canvas.hero

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData

abstract class IHero(private val contextData: ContextData) {
    companion object {
        const val STATE_NORMAL = 1  // 正常运行
        const val STATE_BLOW_UP = 2 // 爆炸中
        const val STATE_END = 3 // 结束
    }

    private var mHeroWidth: Float = 0f
    private var mHeroHeight: Float = 0f

    private lateinit var mHeroCenterRectF: RectF

    private val mHeroRectF = RectF()

    private var mHP: Int = 0

    private var mHeroState = 0

    fun init(): IHero {
        val bitmap = BitmapManager.newBitmap(contextData.resources, getSourceArray(STATE_NORMAL)[0])

        mHeroWidth = bitmap.width.toFloat()
        mHeroHeight = bitmap.height.toFloat()

        val left = (contextData.mapWidth - bitmap.width) / 2.0f
        val top = contextData.mapHeight - bitmap.height.toFloat()

        mHeroRectF.set(left, top, left + bitmap.width, top + bitmap.height)

        mHeroCenterRectF = RectF(mHeroWidth / 2, mHeroHeight / 2,
                contextData.mapWidth - mHeroWidth / 2, contextData.mapHeight - mHeroHeight / 2)

        mHP = getHP()
        mHeroState = STATE_NORMAL

        return this
    }

    fun isHPEmpty(): Boolean {
        return mHP <= 0
    }

    fun changeHP(dHP: Int) {
        mHP += dHP
    }

    fun isDestroy(): Boolean {
        if (isHPEmpty()) {
            return true
        }
        return false
    }

    fun getHeroState(): Int {
        return mHeroState
    }

    fun moveTo(x: Float, y: Float) {
        // 越界了，就不移动了
        if (!mHeroCenterRectF.contains(x, y)) return

        val left = x - mHeroWidth / 2
        val top = y - mHeroHeight / 2
        mHeroRectF.offsetTo(left, top)
    }

    abstract fun getSourceArray(state: Int): IntArray

    abstract fun getHP(): Int

    fun draw(canvas: Canvas, paint: Paint, sourceId: Int) {

    }

    private fun drawInner(canvas: Canvas, paint: Paint, sourceId: Int) {
        val bitmap = BitmapManager.newBitmap(contextData.resources, sourceId)
        canvas.drawBitmap(bitmap, mHeroRectF.left, mHeroRectF.top, paint)
    }

    fun getRectF() = mHeroRectF
}