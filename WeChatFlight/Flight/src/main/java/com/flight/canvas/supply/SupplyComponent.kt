package com.flight.canvas.supply

import android.content.Context
import android.graphics.*
import com.flight.canvas.common.*
import com.yline.log.LogUtil
import java.util.*

class SupplyComponent : BaseComponent() {
    private val random: Random = Random()

    private lateinit var supply1: ISupply
    private lateinit var supply2: ISupply

    private var nextSupplyTime = 0.0f

    private var supplyList = ArrayList<ISupply>()

    private val paint = Paint()

    override fun onMainInit(contextData: ContextData) {
        supply1 = Supply1(contextData)
        supply2 = Supply2(contextData)

        nextSupplyTime = 1.0f + random.nextInt(3)
    }

    override fun onThreadMeasure(contextData: ContextData, toData: MeasureToData) {
        // 新的 供给 派发
        newSupply(contextData.spaceTime)?.let {
            supplyList.add(it)
        }

        // 当前 供给 运行
        val height = 2 * contextData.spaceHeight
        for (iSupply in supplyList) {
            iSupply.move(0.0f, height)
        }

        // 数据
        toData.supplyList = supplyList
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
            return supply1.clone().init()
        } else {
            return supply2.clone().init()
        }
    }

    override fun onThreadDraw(canvas: Canvas, contextData: ContextData) {
        // 越界 供给 移除
        supplyList.removeAll { it.isDestroy() }

        if (supplyList.size > 10) {
            LogUtil.e("supplySize = ${supplyList.size}")
        }

        for (iSupply in this.supplyList) {
            iSupply.draw(canvas, paint)
        }
    }
}