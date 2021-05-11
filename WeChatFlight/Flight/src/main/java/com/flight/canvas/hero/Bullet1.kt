package com.flight.canvas.hero

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.flight.canvas.common.InitData
import com.flight.canvas.state.Counter
import com.flight.canvas.state.StateConstant.BulletState
import com.flight.canvas.supply.ISupply
import com.project.wechatflight.R
import java.util.*

class Bullet1(resources: Resources, random: Random, initData: InitData)
    : IBullet(resources, random, initData) {

    override fun clone(resources: Resources, random: Random, initData: InitData): IBullet {
        return Bullet1(resources, random, initData)
    }

    override fun getSourceId(): Int {
        return R.drawable.bullet1
    }


//    override var rect: Rect? = null
//        private set
//    private var mState: BulletState
//    private val mCounter: Counter
//    private var positionX: Float
//    private var positionY: Float
//    override fun fireBullet(x: Int, y: Int, durateTime: Float, frizTime: Float): Boolean {
//        if (BulletState.unStart == mState) {
//            mCounter.judge = mCounter.caculate("fireBullet", durateTime, frizTime)
//            if (mCounter.judge) {
//                mState = BulletState.normal
//                rect = Rect(x - mBitmap!!.width / 2, y - mBitmap!!.height / 2, x + mBitmap!!.width / 2, y + mBitmap!!.height / 2)
//                positionX = rect!!.left.toFloat()
//                positionY = rect!!.top.toFloat()
//                return true
//            }
//        }
//        return false
//    }
//
//    override fun caculateHeroBullet(dx: Float, dy: Float) {
//        if (BulletState.normal == mState) {
//            positionX += dx
//            positionY += dy
//            rect!!.offsetTo(positionX.toInt(), positionY.toInt())
//            if (rect!!.bottom < mTop || rect!!.top > mBottom) {
//                mState = BulletState.end
//                mBitmap = null
//            }
//        }
//    }
//
//    override fun drawBullet(canvas: Canvas, paint: Paint?) {
//        if (BulletState.normal == mState) {
//            canvas.drawBitmap(mBitmap!!, rect!!.left.toFloat(), rect!!.top.toFloat(), paint)
//        }
//    }
//
//    override val isEnd: Boolean
//        get() = if (BulletState.end == mState) true else false
//
//    override val isRunning: Boolean
//        get() = if (BulletState.normal == mState) true else false
//
//    override fun disppear() {
//        mState = BulletState.end
//    }
//
//    companion object {
//        const val aTK = 1
//        const val bulletRes = R.drawable.bullet1
//    }
//
//    init {
//        mState = BulletState.unStart
//        mCounter = Counter()
//        positionX = 0f
//        positionY = 0f
//    }
}