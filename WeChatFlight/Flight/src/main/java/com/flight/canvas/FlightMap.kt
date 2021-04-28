package com.flight.canvas

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.project.wechatflight.R

/**
 * 背景资源图片
 *
 * @author yline
 * @date 2016-4-3
 */
class FlightMap(resources: Resources) {
    // 偏移量
    private var mMapOffsetY = 0f

    /**
     * 获取当前的运行速度
     *
     * @return
     */
    // 每次运行的高度
    var velocity = 4f
        private set

    // 资源文件
    private var mBitmapMap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.background_1)

    // 资源高度
    private var mBitmapHeight = 0

    init {
        mBitmapHeight = mBitmapMap.getHeight()
    }

    /**
     * 设置背景速度,每次运行的高度
     *
     * @param durateTime
     * @param fullTime   运行完一个屏幕的时间
     */
    fun setVelocity(durateTime: Float, fullTime: Float) {
        velocity = durateTime * mBitmapHeight / fullTime
    }

    fun caculateMap() {
        mMapOffsetY = (mMapOffsetY + velocity) % mBitmapHeight
    }

    fun drawMap(canvas: Canvas, paint: Paint?) {
        // 由于未变化之前,因此这里的高度为图片本身的高度而不是窗口的高度
        canvas.drawBitmap(mBitmapMap, 0f, mMapOffsetY - mBitmapHeight, paint)
        canvas.drawBitmap(mBitmapMap, 0f, mMapOffsetY, paint)
    }

    val mapWidth: Int
        get() = mBitmapMap.width

    val mapHeight: Int
        get() = mBitmapMap.height

}