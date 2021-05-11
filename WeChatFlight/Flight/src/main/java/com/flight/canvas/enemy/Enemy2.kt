package com.flight.canvas.enemy

import android.content.res.Resources
import android.graphics.*
import com.flight.canvas.common.InitData
import com.flight.canvas.state.Counter
import com.flight.canvas.state.CycleState
import com.flight.canvas.state.IFlightState
import com.flight.canvas.state.LineState
import com.flight.canvas.state.StateConstant.EnemyState
import com.project.wechatflight.R
import java.util.*

class Enemy2(resources: Resources, random: Random, initData: InitData)
    : IEnemy(resources, random, initData)  {

    override fun clone(resources: Resources, random: Random, initData: InitData): IEnemy {
        return Enemy2(resources, random, initData)
    }

    override fun getSourceArray(): IntArray {
        return intArrayOf(R.drawable.enemy2_fly_1, R.drawable.enemy2_fly_2)
    }

    override fun getScore(): Int {
        return 10
    }

    override fun getHP(): Int {
        return 20
    }
//
//    // 中间变量
//    private var mHp = 0
//    private var mBitmap: Bitmap? = null
//    override var rect: Rect? = null
//        private set
//    private var mFlightState: IFlightState? = null
//    private var mState: EnemyState
//    private val mCounter: Counter
//    private var positionX = 0f
//    private var positionY = 0f
//    override fun start(durateTime: Float, frizTime: Float): Boolean {
//        if (EnemyState.unStart == mState) {
//            mCounter.judge = mCounter.caculate("start", durateTime, frizTime)
//            if (mCounter.judge) {
//                mState = EnemyState.normal
//                mFlightState = NormalState()
//                mHp = MAXHP
//                mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//                val left = mRandom.nextInt(mMapRect!!.right - mBitmap!!.getWidth())
//                mMapRect = Rect(left, mMapRect.top - mBitmap!!.getHeight(), left + mBitmap!!.getWidth(), mMapRect.top)
//                positionX = mMapRect.left.toFloat()
//                positionY = mMapRect.top.toFloat()
//                return true
//            }
//        }
//        return false
//    }
//
//    override fun caculate(durateTime: Float, dx: Float, dy: Float) {
//        if (EnemyState.normal == mState) {    // 正常时,位置、图片计算
//            positionX += dx
//            positionY += dy
//            mMapRect!!.offsetTo(positionX.toInt(), positionY.toInt())
//
//            // 切换图片
//            mCounter.judge = mCounter.caculate("caculate_normal", durateTime, 0.1f)
//            if (mCounter.judge) {
//                mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//            }
//        }
//        if (EnemyState.hitting == mState) {    // 击中时,位置、图片计算
//            positionX += dx
//            positionY += dy
//            mMapRect!!.offsetTo(positionX.toInt(), positionY.toInt())
//
//            // 切换图片
//            mCounter.judge = mCounter.caculate("caculate_hitting", durateTime, 0.1f)
//            if (mCounter.judge) {
//                if (mFlightState?.hasNext() == true) {
//                    mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//                } else {
//                    mState = EnemyState.normal
//                    mFlightState = NormalState()
//                    mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//                }
//            }
//        }
//        if (EnemyState.bombing == mState) {    // 爆炸时,位置、图片计算
//            mCounter.judge = mCounter.caculate("caculate_bombing", durateTime, 0.1f)
//            if (mCounter.judge) {
//                if (mFlightState!!.hasNext()) {
//                    mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//                } else {
//                    mState = EnemyState.end // 爆炸结束,进入透明状态
//                    mFlightState = null
//                }
//            }
//        }
//
//        // 判断是否出界
//        if (mMapRect!!.bottom < mMapRect.top || mMapRect.top > mMapRect.bottom) {
//            mState = EnemyState.end
//            mFlightState = null
//        }
//    }
//
//    override fun draw(canvas: Canvas, paint: Paint?) {
//        if (EnemyState.normal == mState || EnemyState.hitting == mState || EnemyState.bombing == mState) {
//            canvas.drawBitmap(mBitmap!!, mMapRect!!.left.toFloat(), mMapRect.top.toFloat(), paint)
//        }
//    }
//
//    override fun hitted(atk: Int): Int {
//        mHp -= atk
//        if (mHp <= 0) {
//            blowUp()
//            return score
//        } else {
//            mState = EnemyState.hitting
//            mFlightState = HitState()
//            mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//        }
//        return 0
//    }
//
//    override fun blowUp() {
//        mState = EnemyState.bombing
//        mFlightState = BombingState()
//    }
//
//    override val isEnd: Boolean
//        get() = if (mState == EnemyState.end) true else false
//
//    override val isRunning: Boolean
//        get() = if (EnemyState.normal == mState || EnemyState.hitting == mState) true else false
//
//    internal inner class NormalState : CycleState() {
//        init {
//            list.add(R.drawable.enemy2_fly_1)
//            list.add(R.drawable.enemy2_fly_2)
//        }
//    }
//
//    internal inner class HitState : LineState() {
//        init {
//            list.add(R.drawable.enemy2_hit_1)
//        }
//    }
//
//    internal inner class BombingState : LineState() {
//        init {
//            list.add(R.drawable.enemy2_blowup_1)
//            list.add(R.drawable.enemy2_blowup_2)
//            list.add(R.drawable.enemy2_blowup_3)
//            list.add(R.drawable.enemy2_blowup_4)
//            list.add(R.drawable.enemy2_blowup_5)
//            list.add(R.drawable.enemy2_blowup_6)
//            list.add(R.drawable.enemy2_blowup_7)
//        }
//    }
//
//    companion object {
//        // 常量
//        private const val MAXHP = 20
//        const val score = 10
//    }
//
//    init {
//        mState = EnemyState.unStart
//        mCounter = Counter()
//    }
}