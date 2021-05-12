package com.flight.canvas.hero

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.InitData

abstract class IHero(private val resources: Resources, private val initData: InitData) {
    companion object {
        const val STATE_NORMAL = 1  // 正常运行
        const val STATE_BLOW_UP = 2 // 爆炸
    }

    private var mHeroWidth: Float = 0f
    private var mHeroHeight: Float = 0f

    private lateinit var mHeroCenterRectF: RectF

    private val mHeroRectF = RectF()

    private var mHP: Int = 0

    fun init(): IHero {
        val bitmap = BitmapManager.newBitmap(resources, getSourceArray(STATE_NORMAL)[0])

        mHeroWidth = bitmap.width.toFloat()
        mHeroHeight = bitmap.height.toFloat()

        val left = (initData.mapWidth - bitmap.width) / 2.0f
        val top = initData.mapHeight - bitmap.height.toFloat()

        mHeroRectF.set(left, top, left + bitmap.width, top + bitmap.height)

        mHeroCenterRectF = RectF(mHeroWidth / 2, mHeroHeight / 2,
                initData.mapWidth - mHeroWidth / 2, initData.mapHeight - mHeroHeight / 2)

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
        if (isHPEmpty()) {
            return true
        }
        return false
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

    fun draw(canvas: Canvas, paint: Paint) {
        val state = if (isHPEmpty()) STATE_BLOW_UP else STATE_NORMAL

        val bitmap = BitmapManager.newBitmap(resources, getSourceArray(state)[0])
        canvas.drawBitmap(bitmap, mHeroRectF.left, mHeroRectF.top, paint)
    }

    fun getRectF() = mHeroRectF
}