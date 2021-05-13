package com.flight.canvas.map

import android.graphics.*
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.*
import com.project.wechatflight.R

/**
 * 背景资源图片
 *
 * @author yline
 * @date 2016-4-3
 */
class MapComponent : BaseComponent() {
    // 资源文件
    private var mMapWidth: Int = 0
    private var mMapHeight: Int = 0
    private lateinit var mMapBitmap: Bitmap
    private val mMapPaint = Paint()

    override fun onMainInit(contextData: ContextData) {
        // 背景图
        mMapBitmap = BitmapManager.newBitmap(contextData.resources, R.drawable.background_1)
        mMapWidth = mMapBitmap.width
        mMapHeight = mMapBitmap.height

        mMapPaint.color = Color.BLACK
        mMapPaint.isAntiAlias = true

        // 赋值
        contextData.mapWidth = mMapWidth
        contextData.mapHeight = mMapHeight
    }

    // 历史 偏移量
    private var mMapOffsetY = 0f

    override fun onThreadMeasure(contextData: ContextData, toData: MeasureToData) {
        mMapOffsetY = (mMapOffsetY + contextData.spaceHeight) % mMapHeight
    }

    override fun onThreadDraw(canvas: Canvas, contextData: ContextData) {
        // 由于未变化之前,因此这里的高度为图片本身的高度而不是窗口的高度
        canvas.drawBitmap(mMapBitmap, 0f, mMapOffsetY - mMapHeight, mMapPaint)
        canvas.drawBitmap(mMapBitmap, 0f, mMapOffsetY, mMapPaint)
    }
}