package com.flight.canvas.bullet

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.flight.canvas.common.*
import com.yline.log.LogUtil
import java.util.*

class BulletComponent : BaseComponent() {
    companion object {
        const val styleNormal = 0 // 杀伤力 1
        const val styleDouble = 1 // 杀伤力 2
    }

    // bullet 类型
    private var mBulletStyle = 0
    private var restNormalBulletTime = 0f

    // bullet 对象
    private lateinit var mBullet1: IBullet
    private lateinit var mBullet2: IBullet

    // bullet 对象集合
    private val mBulletList = ArrayList<IBullet>()

    private var nextBulletTime = 0.0f

    private val paint = Paint()

    override fun onMainInit(contextData: ContextData) {
        // bullet 资源初始化
        mBullet1 = Bullet1(contextData)
        mBullet2 = Bullet2(contextData)

        // 子弹 类型
        mBulletStyle = styleNormal
    }

    private fun newBullet(spaceTime: Float, heroRect: RectF): IBullet? {
        // 还没到 供给 时间
        if (nextBulletTime > 0) {
            nextBulletTime -= spaceTime
            return null
        }

        // 重新设值
        nextBulletTime = 0.15f

        // 供给 时间 到了
        if (mBulletStyle == styleNormal) {
            return mBullet1.clone().init(heroRect)
        } else {
            return mBullet2.clone().init(heroRect)
        }
    }

    override fun onThreadMeasure(contextData: ContextData, toData: MeasureToData) {
        // 新的 子弹 出现
        newBullet(contextData.spaceTime, toData.hero.getRectF())?.let {
            mBulletList.add(it)
        }

        // 中等 子弹 自动倒计时 准备结束
        if (restNormalBulletTime < 0) {
            mBulletStyle = styleNormal
        } else {
            restNormalBulletTime -= contextData.spaceTime
        }

        // 子弹类型 升级
        if (contextData.supply2Num > 0) {
            contextData.supply2Num = 0

            mBulletStyle = styleDouble
            restNormalBulletTime = 10f
        }

        val height = -12 * contextData.spaceHeight

        // 当前 子弹 运行
        for (iBullet in mBulletList) {
            iBullet.move(0.0f, height)
        }

        toData.bulletList = mBulletList
    }

    override fun onThreadDraw(canvas: Canvas, contextData: ContextData) {
        // 敌机 销毁 移除
        mBulletList.removeAll { it.isDestroy() }

        if (mBulletList.size > 20) {
            LogUtil.e("bulletSize = ${mBulletList.size}")
        }

        // 子弹	遍历 + drawBullet
        for (bullet in mBulletList) {
            bullet.draw(canvas, paint)
        }
    }
}