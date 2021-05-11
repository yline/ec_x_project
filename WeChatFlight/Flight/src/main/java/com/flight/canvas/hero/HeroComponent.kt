package com.flight.canvas.hero

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.flight.canvas.common.*
import com.flight.canvas.state.Counter
import com.flight.canvas.state.CycleState
import com.flight.canvas.state.IFlightState
import com.flight.canvas.state.LineState
import com.flight.canvas.state.StateConstant.HeroState
import com.flight.canvas.supply.ISupply
import com.flight.canvas.supply.Supply1
import com.flight.canvas.supply.Supply2
import com.project.wechatflight.R
import java.util.*

/**
 * hero
 *
 * @author yline
 * @date 2016-4-3
 */
class HeroComponent() : BaseComponent() {

    // 引用系统资源
    private lateinit var mResources: Resources

    // 系统 背景
    private lateinit var mMapRect: Rect

    // hero	图片状态
    private lateinit var mFlightState: IFlightState

    // hero 图片资源
    private lateinit var mBitmapHero: Bitmap

    // hero 当前矩形
    lateinit var heroRect: RectF

    // hero 当前位置,配合矩形一起使用
    private var positionX = 0.0f
    private var positionY = 0.0f

    // hero 状态
    private lateinit var mState: HeroState

    // hero 大招 个数
    var bigBombNumber = 0
        private set
    private val MaxBigBombNumber = 3

    // hero 分数
    var score: Long = 0
        private set

    // 倒计时,每隔几个出现一个true
    private lateinit var mCounter: Counter

    override fun onMainInit(context: Context, initData: InitData) {
        // 赋值
        mResources = context.resources

        mMapRect = Rect(0, 0, initData.mapWidth, initData.mapHeight)

        // 辅助 资源初始化
        mCounter = Counter()

        // hero 资源初始化
        mFlightState = NormalState()
        mState = HeroState.normal
        mBitmapHero = BitmapFactory.decodeResource(mResources, mFlightState.next())
        heroRect = RectF(mMapRect.right.toFloat() / 2 - mBitmapHero.getWidth() / 2, mMapRect.bottom.toFloat() - mBitmapHero.getHeight(), mMapRect.right.toFloat() / 2 + mBitmapHero.getWidth() / 2, mMapRect.bottom.toFloat()) // 如果输入满屏的话,就居中底部(近乎)显示
        positionX = heroRect.left
        positionY = heroRect.top
    }

    override fun onThreadMeasure(fromData: MeasureFromData, toData: MeasureToData) {
        if (HeroState.normal == mState) {        // 正常状态
            // hero 切换图片资源
            if (mCounter.caculate("caculate_change", fromData.spaceTime, 0.15f)) {
                mBitmapHero = BitmapFactory.decodeResource(mResources, mFlightState.next())
            }
//
//            // hero 隔段时间 发送 bullet
//            if (mBullet.fireBullet(heroRect.centerX(), heroRect.top, fromData.spaceTime, 0.15f)) {
//                if (mBulletStyle == styleNormal) {
//                    mBullet = Bullet1(mBitmapBullet1, mMapRect.top, mMapRect.bottom, 0)
//                    mBulletList.add(mBullet)
//                } else {    // 其它 都算 double
//                    mBullet = Bullet2(mBitmapBullet2, mMapRect.top, mMapRect.bottom, 0)
//                    mBulletList.add(mBullet)
//
//                    // 自动调回去
//                    if (mCounter.caculate("caculate_autoback", 1f, 30f)) {
//                        mBulletStyle = styleNormal
//                    }
//                }
//            }
        }

        // 爆炸 阶段
        if (HeroState.bombing == mState) {
            if (mCounter.caculate("caculate_bombing", fromData.spaceTime, 0.1f)) {
                if (mFlightState.hasNext()) {
                    mBitmapHero = BitmapFactory.decodeResource(mResources, mFlightState.next())
                } else {
                    mState = HeroState.end
                }
            }
        }
//        for (iBullet in mBulletList) {
//            iBullet.caculateHeroBullet(0f, -12 * fromData.spaceHeight)
//            if (iBullet.isEnd) {
//                mBulletList.remove(iBullet)
//                break // 跳出整个循环,这是因为在该循环的时候,iterator的原因导致的错误
//            }
//        }

        toData.heroRect = heroRect
    }

    override fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {
    }

    private val paint = Paint()

    override fun onThreadDraw(canvas: Canvas, attackData: AttackData) {
        if (HeroState.normal == mState || HeroState.bombing == mState) {
            canvas.drawBitmap(mBitmapHero, heroRect.left.toFloat(), heroRect.top.toFloat(), paint)
        }
    }

    /**
     * 移动,控制是在中心位置
     *
     * @param x 手所在的x轴位置
     * @param y 手所在的Y轴位置
     */
    fun moveTo(x: Float, y: Float) {
        if (isInBridge(x, y) && mState == HeroState.normal) {
            positionX = x - mBitmapHero.width / 2
            positionY = y - mBitmapHero.height / 2
        }
        heroRect.offsetTo(positionX - 1, positionY - 1)
    }

    private fun isInBridge(x: Float, y: Float): Boolean {
        return x > mMapRect.left + mBitmapHero.width / 2 && x < mMapRect.right - mBitmapHero.width / 2 && y > mMapRect.top + mBitmapHero.height / 2 && y < mMapRect.bottom - mBitmapHero.height / 2
    }

    /**
     * hero加属性,bullet
     */
//    fun handleSupplyAttack(iSupply: ISupply?) {
//        if (iSupply is Supply1) {    // big炸弹
//            if (bigBombNumber < MaxBigBombNumber) {
//                bigBombNumber += 1
//            }
//        } else if (iSupply is Supply2) {    // 多发子弹
//            mBulletStyle = styleDouble
//            mCounter.clearCaculate("caculate_autoback")
//        }
//    }

    /**
     * hero转变为爆炸状态
     */
    fun handleEnemyAttack() {
        mState = HeroState.bombing
        mFlightState = BombingState()
    }

//    /**
//     * 撞击到的时候的处理
//     *
//     * @param iBullet 有交集时的子弹对象
//     * @return 伤害值
//     */
//    fun handleBulletAttack(iBullet: IBullet): Int {
//        if (iBullet.isRunning) {
//            iBullet.disppear()
//            return iBullet.atk
//        }
//        return 0
//    }

    fun addScore(score: Int) {
        this.score += score.toLong()
    }

    val isNormal: Boolean
        get() = if (mState == HeroState.normal) true else false

    val isEnd: Boolean
        get() = if (mState == HeroState.end) true else false

//    val bulletList: List<IBullet>
//        get() = mBulletList

    fun reduceBigBombNumber() {
        bigBombNumber -= 1
    }

    class NormalState : CycleState() {
        init {
            list.add(R.drawable.hero_fly_1)
            list.add(R.drawable.hero_fly_2)
        }
    }

    class BombingState : LineState() {
        init {
            list.add(R.drawable.hero_blowup_1)
            list.add(R.drawable.hero_blowup_2)
            list.add(R.drawable.hero_blowup_3)
            list.add(R.drawable.hero_blowup_4)
        }
    }

}