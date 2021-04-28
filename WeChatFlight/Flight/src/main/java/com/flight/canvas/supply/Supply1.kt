package com.flight.canvas.supply

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.project.wechatflight.R
import java.util.*

class Supply1(private val mBitmap: Bitmap, random: Random, private var mMapRect: Rect) : ISupply {
    /**
     * 0	未开始状态
     * 1	正常状态
     * 2	透明状态
     * 3	出界,结束
     */
    private var mState: Int
    private var mTime: Float
    override var rect: Rect? = null
        private set
    private val mLeft: Int
    private var positionX = 0f
    private var positionY = 0f
    override fun start(durateTime: Float, frizTime: Float): Boolean {
        if (0 == mState) {
            if (mTime > frizTime) {
                mTime = 0f
                mState = 1
                mMapRect = Rect(mLeft, mMapRect!!.top - mBitmap!!.height, mLeft + mBitmap.width, mMapRect.top)
                positionX = mMapRect.left.toFloat()
                positionY = mMapRect.top.toFloat()
                return true
            } else {
                mTime += durateTime
            }
        }
        return false
    }

    override fun caculate(dx: Float, dy: Float) {
        if (1 == mState || 2 == mState) {
            positionX += dx
            positionY += dy
            mMapRect!!.offsetTo(positionX.toInt(), positionY.toInt())
            if (mMapRect.bottom < mMapRect.top || mMapRect.top > mMapRect.bottom) {
                mState = 3
            }
        }
    }

    override fun draw(canvas: Canvas, paint: Paint?) {
        if (1 == mState) {
            canvas.drawBitmap(mBitmap!!, mMapRect!!.left.toFloat(), mMapRect.top.toFloat(), paint)
        }
    }

    override val isOut: Boolean
        get() = if (3 == mState) true else false

    override val isNormal: Boolean
        get() = if (1 == mState) true else false

    override fun disppear() {
        mState = 2
    }

    companion object {
        const val SupplyRes = R.drawable.supply1
    }

    init {
        mLeft = random.nextInt(mMapRect!!.right - mBitmap!!.width)
        mState = 0
        mTime = 0f
    }
}