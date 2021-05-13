package com.flight.canvas.map

import android.graphics.*
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.BaseComponent
import com.flight.canvas.common.ContextData
import com.flight.canvas.common.MeasureToData
import com.project.wechatflight.R

class InfoComponent : BaseComponent() {
    // FPS
    private var mCFPSMaker = CFPSMaker()
    private var mFpsPaint = Paint()

    private var scorePaint = Paint()

    private lateinit var mBitmapBigBomb: Bitmap
    private lateinit var bigBombRect: RectF

    private lateinit var mBitmapPause: Bitmap
    private lateinit var mBitmapStart: Bitmap
    private lateinit var pauseRect: RectF

    override fun onMainInit(contextData: ContextData) {
        // FPS
        mFpsPaint.color = Color.rgb(60, 60, 60) // 颜色
        val font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC) // 字体
        mFpsPaint.typeface = font
        mFpsPaint.textSize = 30f

        // score
        scorePaint.color = Color.rgb(60, 60, 60) // 颜色
        scorePaint.typeface = font
        scorePaint.textSize = 30f

        // 大炸弹
        mBitmapBigBomb = BitmapManager.newBitmap(contextData.resources, R.drawable.bomb)
        bigBombRect = RectF(0f, 0f, mBitmapBigBomb.width.toFloat(), mBitmapBigBomb.height.toFloat())
        bigBombRect.offsetTo(20.0f, contextData.mapHeight - 20.0f - mBitmapBigBomb.height)

        // 暂停
        mBitmapPause = BitmapManager.newBitmap(contextData.resources, R.drawable.game_pause)
        mBitmapStart = BitmapManager.newBitmap(contextData.resources, R.drawable.game_start)
        pauseRect = RectF(0.0f, 0.0f, mBitmapPause.width.toFloat(), mBitmapPause.height.toFloat())
        pauseRect.offsetTo(contextData.mapWidth - 20.0f - mBitmapPause.width, contextData.mapHeight - 20.0f - mBitmapBigBomb.height)

        contextData.bigBombRect = bigBombRect
        contextData.pauseRect = pauseRect
    }

    override fun onThreadMeasure(contextData: ContextData, toData: MeasureToData) {
        // FPS
        mCFPSMaker.measure(contextData)
    }

    override fun onThreadDraw(canvas: Canvas, contextData: ContextData) {
        // FPS
        canvas.drawText(contextData.showFPS, contextData.mapWidth - 155.toFloat(), 30f, mFpsPaint)
        canvas.drawText("分数:${contextData.totalScore}", 20f, 30f, scorePaint)

        canvas.drawBitmap(mBitmapBigBomb, 20f, contextData.mapHeight - 20 - mBitmapBigBomb.height.toFloat(), scorePaint)
        canvas.drawText("×${contextData.supply1Num}", 20 + mBitmapBigBomb.width.toFloat(), contextData.mapHeight - 30.toFloat(), scorePaint)

        if (!contextData.isPause) {
            canvas.drawBitmap(mBitmapPause, pauseRect.left, pauseRect.top, scorePaint)
        } else {
            canvas.drawBitmap(mBitmapStart, pauseRect.left, pauseRect.top, scorePaint)
        }
    }
}