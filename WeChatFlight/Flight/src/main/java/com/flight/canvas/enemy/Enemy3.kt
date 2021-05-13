package com.flight.canvas.enemy

import android.content.res.Resources
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData
import com.flight.canvas.couter.CycleCounter
import com.flight.canvas.couter.ICounter
import com.flight.canvas.couter.LinearCounter
import com.project.wechatflight.R

/**
 * 注意状态改变之后,哪些发生了改变(当前状态\图片集\当前图片)
 *
 * @author yline
 * @date 2016-4-13
 */
class Enemy3(contextData: ContextData) : IEnemy(contextData) {

    override fun getSourceArray(state: Int): IntArray? {
        return when (getEnemyState()) {
            STATE_NORMAL -> intArrayOf(R.drawable.enemy3_fly_1)
            STATE_HIT -> intArrayOf(R.drawable.enemy3_hit_1)
            STATE_BLOW_UP -> intArrayOf(R.drawable.enemy3_blowup_1, R.drawable.enemy3_blowup_2,
                    R.drawable.enemy3_blowup_3, R.drawable.enemy3_blowup_4)
            else -> null
        }
    }

    override fun getSourceRect(resources: Resources): RectF {
        val bitmap = BitmapManager.newBitmap(resources, R.drawable.enemy3_fly_1)
        return RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
    }

    override fun getEnemyState(): Int {
        if (isOuter()) return STATE_END

        if (mHP > 8) return STATE_NORMAL

        if (mHP > 0) return STATE_HIT

        if (mIsBlowUp) return STATE_BLOW_UP

        return STATE_END
    }

    override fun clone(contextData: ContextData): IEnemy {
        return Enemy3(contextData)
    }

    override fun getScore(): Int {
        return 5
    }

    override fun getHP(): Int {
        return 10
    }
//
//    // 中间变量
//    private var mHp = 0
//    private var mBitmap: Bitmap? = null
//    override var rect: Rect? = null
//        private set
//
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
//        }
//        if (EnemyState.hitting == mState) {    // 击中时,位置、图片计算
//            positionX += dx
//            positionY += dy
//            mMapRect!!.offsetTo(positionX.toInt(), positionY.toInt())
//
//            // 切换图片
//            mCounter.judge = mCounter.caculate("caculate_hitting", durateTime, 0.1f)
//            if (mCounter.judge) {
//                if (mFlightState!!.hasNext()) {
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
//        mBitmap = BitmapFactory.decodeResource(mResources, mFlightState!!.next())
//    }
//
//    override val isEnd: Boolean
//        get() = if (mState == EnemyState.end) true else false
//
//    override val isRunning: Boolean
//        get() = if (EnemyState.normal == mState || EnemyState.hitting == mState) true else false
//

//
//    companion object {
//        // 常量
//        private const val MAXHP = 10
//        const val score = 5
//    }
//
//    init {
//        mState = EnemyState.unStart
//        mCounter = Counter()
//    }
}