package com.flight.canvas.variable

import android.content.Context
import android.graphics.*
import com.flight.canvas.common.*
import com.project.wechatflight.R
import kotlin.properties.Delegates

/**
 * 就一些界面上固定位置的东西
 *
 * @author yline
 * @date 2016-4-3
 */
class VariableComponent : BaseComponent() {    // resources: Resources, width: Int, height: Int
    private var mBigBombNumber = 0
    private var mTotalScore: Long = 0

    private var mapWidth by Delegates.notNull<Int>()
    private var mapHeight by Delegates.notNull<Int>()

    private lateinit var mBitmapBigBomb: Bitmap
    lateinit var bigBombRect: Rect

    private lateinit var mBitmapPause: Bitmap
    private lateinit var mBitmapStart: Bitmap
    lateinit var pauseRect: Rect

    override fun onMainInit(context: Context, initData: InitData) {
        mapWidth = initData.mapWidth
        mapHeight = initData.mapHeight

        // 大炸弹
        mBitmapBigBomb = BitmapFactory.decodeResource(context.resources, R.drawable.bomb)
        bigBombRect = Rect(20, mapHeight - 20 - mBitmapBigBomb.getHeight(), 20 + mBitmapBigBomb.getWidth(), mapHeight - 20)

        // 暂停
        mBitmapPause = BitmapFactory.decodeResource(context.resources, R.drawable.game_pause)
        mBitmapStart = BitmapFactory.decodeResource(context.resources, R.drawable.game_start)
        pauseRect = Rect(mapWidth - 20 - mBitmapPause.getWidth(), mapHeight - 20 - mBitmapPause.getHeight(), mapWidth - 20, mapHeight - 20)
    }

    override fun onThreadMeasure(fromData: MeasureFromData, toData: MeasureToData) {
    }

    override fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {
    }

    override fun onThreadDraw(canvas: Canvas, attackData: AttackData) {
    }

    fun setBigBombNumber(number: Int) {
        mBigBombNumber = number
    }

    fun setTotalScore(score: Long) {
        mTotalScore = score
    }

    fun drawVariable(canvas: Canvas, scorePaint: Paint?, isPause: Boolean) {
        canvas.drawText("分数:$mTotalScore", 20f, 30f, scorePaint!!)

        canvas.drawBitmap(mBitmapBigBomb, 20f, mapHeight - 20 - mBitmapBigBomb.height.toFloat(), scorePaint)
        canvas.drawText("×$mBigBombNumber", 20 + mBitmapBigBomb.width.toFloat(), mapHeight - 30.toFloat(), scorePaint)
        if (!isPause) {
            canvas.drawBitmap(mBitmapPause, pauseRect.left.toFloat(), pauseRect.top.toFloat(), scorePaint)
        } else {
            canvas.drawBitmap(mBitmapStart, pauseRect.left.toFloat(), pauseRect.top.toFloat(), scorePaint)
        }
    }

}