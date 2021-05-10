package com.flight.canvas.supply

import android.content.Context
import android.graphics.*
import com.flight.canvas.common.*
import com.project.wechatflight.R
import java.util.*
import kotlin.math.min

class SupplyComponent() : BaseComponent() {
    private val random: Random = Random()

    private lateinit var supply1: ISupply
    private lateinit var supply2: ISupply

    private var nextSupplyTime = 0.0f

    private var supplyList: MutableList<ISupply> = ArrayList()

    private val paint = Paint()

    override fun onMainInit(context: Context, initData: InitData) {
        val resources = context.resources

        val bitmapSupply1 = BitmapFactory.decodeResource(resources, R.drawable.supply1) // 炸弹
        val bitmapSupply2 = BitmapFactory.decodeResource(resources, R.drawable.supply2) // 双子弹

        supply1 = Supply1(bitmapSupply1, random, initData)
        supply2 = Supply2(bitmapSupply2, random, initData)

        nextSupplyTime = 1.0f + random.nextInt(3)
    }

    override fun onThreadMeasure(fromData: MeasureFromData, toData: MeasureToData) {
        // 新的 供给 派发
        newSupply(fromData.spaceTime)?.let {
            supplyList.add(it)
        }

        // 越界 供给 移除
        supplyList.removeAll { it.isDestroy() }

        // 当前 供给 运行
        val height = 2 * fromData.spaceHeight
        for (iSupply in supplyList) {
            iSupply.move(0.0f, height)
        }
    }

    private fun newSupply(spaceTime: Float): ISupply? {
        // 还没到 供给 时间
        if (nextSupplyTime > 0) {
            nextSupplyTime -= spaceTime
            return null
        }

        // 重新设值
        nextSupplyTime = 1.0f + random.nextInt(3)

        // 供给 时间 到了
        if (random.nextInt(2) >= 1) {
            return supply1.clone()
        } else {
            return supply2.clone()
        }
    }

    override fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {
        for (iSupply in this.supplyList) {
            if (iSupply.isAttack(toData.heroRect)) {
                if (iSupply is Supply1) {
                    attackData.supply1Num += 1
                    attackData.supply1Num = min(3, attackData.supply1Num)   // 上限为 3
                } else {
                    attackData.supply2Num += 1
                }
            }
        }
    }

    override fun onThreadDraw(canvas: Canvas, attackData: AttackData) {
        for (iSupply in this.supplyList) {
            iSupply.draw(canvas, paint)
        }
    }

    /**
     * 负责 补给撞击后操作
     */
    fun handleHeroAttack(iSupply: ISupply) {
//        iSupply.disppear()
    }
}