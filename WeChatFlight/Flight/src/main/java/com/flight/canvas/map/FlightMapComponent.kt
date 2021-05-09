package com.flight.canvas.map

import android.content.Context
import android.graphics.*
import com.flight.canvas.common.BaseComponent
import com.project.wechatflight.R

/**
 * 背景资源图片
 *
 * @author yline
 * @date 2016-4-3
 */
class FlightMapComponent : BaseComponent() {

    // 资源文件
    private var mMapWidth: Int = 0
    private var mMapHeight: Int = 0
    private lateinit var mMapBitmap: Bitmap
    private val mMapPaint = Paint()

    // FPS
    private lateinit var mCfpsMaker: CFPSMaker
    private lateinit var mFpsPaint: Paint

    override fun onMainInit(context: Context) {
        // 背景图
        mMapBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background_1)
        mMapWidth = mMapBitmap.width
        mMapHeight = mMapBitmap.height

        mMapPaint.color = Color.BLACK
        mMapPaint.isAntiAlias = true

        // FPS
        mCfpsMaker = CFPSMaker()

        mFpsPaint = Paint()
        mFpsPaint.color = Color.rgb(60, 60, 60) // 颜色
        val font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC) // 字体
        mFpsPaint.typeface = font
        mFpsPaint.textSize = 30f
    }

    // 历史 偏移量
    private var mMapOffsetY = 0f

    override fun onThreadMeasure(diffHeight: Float) {
        mMapOffsetY = (mMapOffsetY + diffHeight) % mMapHeight

        // FPS
        mCfpsMaker.makeFPS()
    }

    override fun onThreadDraw(canvas: Canvas) {
        // 由于未变化之前,因此这里的高度为图片本身的高度而不是窗口的高度
        canvas.drawBitmap(mMapBitmap, 0f, mMapOffsetY - mMapHeight, mMapPaint)
        canvas.drawBitmap(mMapBitmap, 0f, mMapOffsetY, mMapPaint)

        // FPS
        canvas.drawText(mCfpsMaker.fPS + " FPS", mMapWidth - 155.toFloat(), 30f, mFpsPaint)
    }

    fun getMapWidth() = mMapWidth

    fun getMapHeight() = mMapHeight

/*
    var velocity = 4f
        private set

    val mapWidth: Int
        get() = mBitmapMap.width

    val mapHeight: Int
        get() = mBitmapMap.height*/

}