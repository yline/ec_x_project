package com.flight.canvas.map

import android.content.Context
import android.graphics.*
import com.flight.canvas.common.BaseComponent
import com.flight.canvas.common.FlightData
import com.project.wechatflight.R

/**
 * 背景资源图片
 *
 * @author yline
 * @date 2016-4-3
 */
class FlightMapComponent : BaseComponent() {

    // 资源文件
    private lateinit var mMapBitmap: Bitmap

    // 资源高度
    private var mMapHeight: Int = 0

    private val mMapPaint = Paint()

    override fun onMainInit(context: Context) {
        // 背景图
        mMapBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background_1)
        mMapHeight = mMapBitmap.height

        mMapPaint.color = Color.BLACK
        mMapPaint.isAntiAlias = true
    }

    // 历史 偏移量
    private var mMapOffsetY = 0f

    override fun onThreadMeasure(diffHeight: Float) {
        mMapOffsetY = (mMapOffsetY + diffHeight) % mMapHeight
    }

    override fun onThreadDraw(canvas: Canvas) {
        // 由于未变化之前,因此这里的高度为图片本身的高度而不是窗口的高度
        canvas.drawBitmap(mMapBitmap, 0f, mMapOffsetY - mMapHeight, mMapPaint)
        canvas.drawBitmap(mMapBitmap, 0f, mMapOffsetY, mMapPaint)
    }

    fun getMapWidth() = mMapBitmap.width

    fun getMapHeight() = mMapBitmap.height

/*
    var velocity = 4f
        private set

    val mapWidth: Int
        get() = mBitmapMap.width

    val mapHeight: Int
        get() = mBitmapMap.height*/

}