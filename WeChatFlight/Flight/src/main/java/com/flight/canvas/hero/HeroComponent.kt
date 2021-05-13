package com.flight.canvas.hero

import android.graphics.*
import com.flight.canvas.common.*
import com.yline.log.LogUtil

class HeroComponent : BaseComponent() {
    private lateinit var iHero: IHero

    private val heroList = ArrayList<IHero>()

    override fun onMainInit(contextData: ContextData) {
        iHero = Hero1(contextData)
        iHero.init()

        heroList.clear()
        heroList.add(iHero)
    }

    override fun onThreadMeasure(contextData: ContextData, toData: MeasureToData) {
        toData.hero = iHero
    }

    override fun onThreadDraw(canvas: Canvas, contextData: ContextData) {
        val heroState = iHero.getHeroState()
        when (heroState) {
            IHero.STATE_NORMAL -> {
                val sourceId = iHero.getSourceArray(heroState).next(contextData.spaceTime, 0.15f)
                if (null != sourceId) {
                    iHero.draw(canvas, contextData.paint, sourceId)
                } else {
                    LogUtil.e("hero state: normal, sourceId is null")
                }
            }
            IHero.STATE_BLOW_UP -> {
                val sourceId = iHero.getSourceArray(heroState).next(contextData.spaceTime, 0.6f)
                if (null != sourceId) {
                    iHero.draw(canvas, contextData.paint, sourceId)
                } else {
                    LogUtil.v("hero state blow up, finished")
                    iHero.finishBlowUp()
                }
            }
        }
    }

    /**
     * 移动,控制是在中心位置
     *
     * @param x 手所在的x轴位置
     * @param y 手所在的Y轴位置
     */
    fun moveTo(x: Float, y: Float) {
        iHero.moveTo(x, y)
    }
}