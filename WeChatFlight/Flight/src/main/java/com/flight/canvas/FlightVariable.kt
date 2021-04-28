package com.flight.canvas

import android.content.res.Resources
import android.graphics.*
import com.flight.canvas.fsp.CFPSMaker
import com.project.wechatflight.R

/**
 * 就一些界面上固定位置的东西
 *
 * @author yline
 * @date 2016-4-3
 */
class FlightVariable(resources: Resources, width: Int, height: Int) {
    private var mBigBombNumber = 0
    private var mTotalScore: Long = 0
    private var mapWidth = 0
    private var mapHeight = 0
    private var mBitmapBigBomb: Bitmap
    var bigBombRect: Rect

    private var mCfpsMaker: CFPSMaker
    private var mBitmapPause: Bitmap
    private var mBitmapStart: Bitmap
    var pauseRect: Rect

    init {
        mapWidth = width
        mapHeight = height

        // 大炸弹
        mBitmapBigBomb = BitmapFactory.decodeResource(resources, R.drawable.bomb)
        bigBombRect = Rect(20, mapHeight - 20 - mBitmapBigBomb.getHeight(), 20 + mBitmapBigBomb.getWidth(), mapHeight - 20)
        mCfpsMaker = CFPSMaker()

        // 暂停
        mBitmapPause = BitmapFactory.decodeResource(resources, R.drawable.game_pause)
        mBitmapStart = BitmapFactory.decodeResource(resources, R.drawable.game_start)
        pauseRect = Rect(mapWidth - 20 - mBitmapPause.getWidth(), mapHeight - 20 - mBitmapPause.getHeight(), mapWidth - 20, mapHeight - 20)
    }

    fun setBigBombNumber(number: Int) {
        mBigBombNumber = number
    }

    fun setTotalScore(score: Long) {
        mTotalScore = score
    }

    fun drawVariable(canvas: Canvas, scorePaint: Paint?, isPause: Boolean) {
        canvas.drawText("分数:$mTotalScore", 20f, 30f, scorePaint!!)
        mCfpsMaker.makeFPS()
        canvas.drawText(mCfpsMaker.fPS + " FPS", mapWidth - 155.toFloat(), 30f, scorePaint)
        canvas.drawBitmap(mBitmapBigBomb, 20f, mapHeight - 20 - mBitmapBigBomb.height.toFloat(), scorePaint)
        canvas.drawText("×$mBigBombNumber", 20 + mBitmapBigBomb.width.toFloat(), mapHeight - 30.toFloat(), scorePaint)
        if (!isPause) {
            canvas.drawBitmap(mBitmapPause, pauseRect.left.toFloat(), pauseRect.top.toFloat(), scorePaint)
        } else {
            canvas.drawBitmap(mBitmapStart, pauseRect.left.toFloat(), pauseRect.top.toFloat(), scorePaint)
        }
    }

}