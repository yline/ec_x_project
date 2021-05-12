package com.flight.canvas.enemy

import com.flight.canvas.common.ContextData
import com.flight.canvas.couter.CycleCounter
import com.flight.canvas.couter.ICounter
import com.flight.canvas.couter.LinearCounter
import com.project.wechatflight.R

class Enemy1(contextData: ContextData) : IEnemy(contextData) {
    private val normalCounter = CycleCounter(intArrayOf(R.drawable.enemy1_fly_1))
    private val blowUpCounter = LinearCounter(intArrayOf(R.drawable.enemy1_blowup_1, R.drawable.enemy1_blowup_2,
            R.drawable.enemy1_blowup_3, R.drawable.enemy1_blowup_4))

    override fun getSourceArray(state: Int): ICounter {
        val enemyState = getEnemyState()
        return when (enemyState) {
            STATE_BLOW_UP -> blowUpCounter
            else -> normalCounter
        }
    }

    override fun getEnemyState(): Int {
        return STATE_END
    }

    override fun clone(contextData: ContextData): IEnemy {
        return Enemy1(contextData)
    }

    override fun getScore(): Int {
        return 2
    }

    override fun getHP(): Int {
        return 4
    }


//
//    // 中间变量
//    private var mHp = 0
//    private lateinit var mBitmap: Bitmap
//    override lateinit var rect: Rect
//    private var mFlightState: IFlightState? = null
//    private var mState: EnemyState
//    private val mCounter: Counter
//    private var positionX = 0f
//    private var positionY = 0f
//
//    override fun start(durateTime: Float, frizTime: Float): Boolean {
//        if (EnemyState.unStart == mState) {
//            mCounter.judge = mCounter.caculate("start", durateTime, frizTime)
//            if (mCounter.judge) {
//                mState = EnemyState.normal
//                mFlightState = NormalState()
//                mHp = MAXHP
//                mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//                val left = mRandom.nextInt(mMapRect.right - mBitmap.getWidth())
//                mMapRect = Rect(left, mMapRect.top - mBitmap.getHeight(), left + mBitmap.getWidth(), mMapRect.top)
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
//            mMapRect.offsetTo(positionX.toInt(), positionY.toInt())
//        }
//        if (EnemyState.hitting == mState) {    // 击中时,位置、图片计算
//            positionX += dx
//            positionY += dy
//            mMapRect.offsetTo(positionX.toInt(), positionY.toInt())
//        }
//
//        // 内部转换图片,cycle就永远单个状态,line 就可能退出
//        if (EnemyState.bombing == mState) {    // 爆炸时,位置、图片计算
//            mCounter.judge = mCounter.caculate("bombing", durateTime, 0.1f)
//            if (mCounter.judge) {
//                if (mFlightState?.hasNext() == true) {
//                    mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//                } else {
//                    mState = EnemyState.end // 爆炸结束,进入透明状态
//                    mFlightState = null
//                }
//            }
//        }
//
//        // 判断是否出界
//        if (mMapRect.bottom < mMapRect.top || mMapRect.top > mMapRect.bottom) {
//            mState = EnemyState.end
//            mFlightState = null
//        }
//    }
//
//    override fun draw(canvas: Canvas, paint: Paint?) {
//        if (EnemyState.normal == mState || EnemyState.hitting == mState || EnemyState.bombing == mState) {
//            canvas.drawBitmap(mBitmap, mMapRect.left.toFloat(), mMapRect.top.toFloat(), paint)
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
//            // 无图片转换,无状态转换
//        }
//        return 0
//    }
//
//    override fun blowUp() {
//        mState = EnemyState.bombing
//        mFlightState = BombingState()
//        mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
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
//            list.add(R.drawable.enemy1_fly_1)
//        }
//    }
//
//    internal inner class BombingState : LineState() {
//        init {
//            list.add(R.drawable.enemy1_blowup_1)
//            list.add(R.drawable.enemy1_blowup_2)
//            list.add(R.drawable.enemy1_blowup_3)
//            list.add(R.drawable.enemy1_blowup_4)
//        }
//    }
//
//    companion object {
//        // 常量
//        private const val MAXHP = 4
//        const val score = 2
//    }
//
//    init {
//        mState = EnemyState.unStart
//        mCounter = Counter()
//    }
}