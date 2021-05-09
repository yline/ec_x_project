package com.flight.canvas.supply

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.flight.canvas.common.BaseComponent
import com.flight.canvas.common.FlightData
import java.util.*

class SupplyComponent(context: Context) : BaseComponent() {
    private val mRandom: Random = Random()
    private val isEnd = false // hero 是否 死了

    private lateinit var mBitmapSupply1: Bitmap
    private lateinit var mBitmapSupply2: Bitmap
    private lateinit var mMapRect: Rect
    private lateinit var mSupply1: ISupply
    private lateinit var mSupply2: ISupply

    private var mSupplyList: MutableList<ISupply> = ArrayList()

    override fun onMainInit(context: Context) {
        TODO("Not yet implemented")
    }

    override fun onThreadMeasure(diffHeight: Float) {
        TODO("Not yet implemented")
    }

    override fun onThreadDraw(canvas: Canvas) {
        TODO("Not yet implemented")
    }

    init {
        val resources = context.resources

        val flightData = acquire(FlightData::class.java) as FlightData
        mMapRect = Rect(0, 0, flightData.mapWidth, flightData.mapHeight)

        mBitmapSupply1 = BitmapFactory.decodeResource(resources, Supply1.Companion.SupplyRes)
        mBitmapSupply2 = BitmapFactory.decodeResource(resources, Supply2.Companion.SupplyRes)
        mSupply1 = Supply1(mBitmapSupply1, mRandom, mMapRect)
        mSupply2 = Supply2(mBitmapSupply2, mRandom, mMapRect)
    }

    private var isSupply1Start = false
    private var isSupply2Start = false

    /**
     * 负责 补给移动、补给产生
     */
    fun caculateSupply(durateTime: Float, height: Float, frizTime1: Float, frizTime2: Float) {
        if (!isEnd) {
            // 补给	产生时间间隔
            isSupply1Start = mSupply1.start(durateTime, frizTime1)
            if (isSupply1Start) {
                mSupplyList.add(mSupply1)
                mSupply1 = Supply1(mBitmapSupply1, mRandom, mMapRect)
            }
            isSupply2Start = mSupply2.start(durateTime, frizTime2)
            if (isSupply2Start) {
                mSupplyList.add(mSupply2)
                mSupply2 = Supply2(mBitmapSupply2, mRandom, mMapRect)
            }
            for (iSupply in mSupplyList) {
                iSupply.caculate(0f, height)
                if (iSupply.isOut) {
                    mSupplyList.remove(iSupply)
                    break // 跳出整个循环,这是因为在该循环的时候,iterator的错误
                }
            }
        }
    }

    fun drawSupplies(canvas: Canvas, paint: Paint?) {
        for (iSupply in mSupplyList) {
            iSupply.draw(canvas, paint)
        }
    }

    /**
     * 负责 补给撞击后操作
     */
    fun handleHeroAttack(iSupply: ISupply) {
        iSupply.disppear()
    }

    val supplyList: List<ISupply>
        get() = mSupplyList

}