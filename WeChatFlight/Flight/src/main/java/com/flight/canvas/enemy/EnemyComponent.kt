package com.flight.canvas.enemy

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.flight.canvas.common.*
import com.yline.log.LogFileUtil.v
import java.util.*

class EnemyComponent() : BaseComponent() {
    private val mRandom: Random = Random()
    private val isEnd = false // hero 是否 死了
    private lateinit var mMapRect: Rect

    private lateinit var mResources: Resources

    private lateinit var mEnemy1: IEnemy
    private var mEnemyNumber1: Int = 0
    private lateinit var mEnemy2: IEnemy
    private var mEnemyNumber2: Int = 0
    private lateinit var mEnemy3: IEnemy
    private var mEnemyNumber3: Int = 0

    private val mEnemyListTotal: MutableList<IEnemy> = ArrayList()

    override fun onMainInit(context: Context, initData: InitData) {
        mResources = context.resources

        mMapRect = Rect(0, 0, initData.mapWidth, initData.mapHeight)

        mEnemy1 = Enemy1(mResources, mRandom, mMapRect, 0)
        mEnemy2 = Enemy2(mResources, mRandom, mMapRect, 0)
        mEnemy3 = Enemy3(mResources, mRandom, mMapRect, 0)
    }

    override fun onThreadMeasure(fromData: MeasureFromData, toData: MeasureToData) {
        val durateTime = fromData.spaceTime
        val height = 2 * fromData.spaceHeight
        val frizTime1 = 10f
        val frizTime2 = 10f
        val frizTime3 = 10f

        if (!isEnd) {
            // 敌机产生 时间间隔
            isStart = mEnemy1.start(durateTime, frizTime1)
            if (isStart) {
                mEnemyListTotal.add(mEnemy1)
                mEnemyNumber1 += 1
                mEnemy1 = Enemy1(mResources, mRandom, mMapRect, 0)
            }
            isStart = mEnemy2.start(durateTime, frizTime2)
            if (isStart) {
                mEnemyListTotal.add(mEnemy2)
                mEnemyNumber2 += 1
                mEnemy2 = Enemy2(mResources, mRandom, mMapRect, 0)
            }
            isStart = mEnemy3.start(durateTime, frizTime3)
            if (isStart) {
                mEnemyListTotal.add(mEnemy3)
                mEnemyNumber3 += 1
                mEnemy3 = Enemy3(mResources, mRandom, mMapRect, 0)
            }

            // 移动, + 出界处理
            for (iEnemy in mEnemyListTotal) {
                iEnemy.caculate(durateTime, 0f, height)
                if (iEnemy.isEnd) {
                    mEnemyListTotal.remove(iEnemy)
                    reduceEnemyNumber(iEnemy)
                    break
                }
            }
        }
    }

    override fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {
    }

    override fun onThreadDraw(canvas: Canvas, attackData: AttackData) {
    }

    private var isStart = false

    /**
     * 出界就减少个数
     * @param iEnemy
     */
    private fun reduceEnemyNumber(iEnemy: IEnemy?) {
        if (iEnemy is Enemy1) {
            mEnemyNumber1 -= 1
        } else if (iEnemy is Enemy2) {
            mEnemyNumber2 -= 1
        } else if (iEnemy is Enemy3) {
            mEnemyNumber3 -= 1
        }
        v("number1 = $mEnemyNumber1,number2 = $mEnemyNumber2,number3 = $mEnemyNumber3")
    }

    fun drawEnemies(canvas: Canvas, paint: Paint?) {
        for (iEnemy in mEnemyListTotal) {
            iEnemy.draw(canvas, paint)
        }
    }

    /**
     * 大炸弹 的操作
     * @return
     */
    fun handleBigBombing(): Int {
        var score = 0
        for (iEnemy in mEnemyListTotal) {
            score += iEnemy.hitted(Int.MAX_VALUE)
        }
        return score
    }

    /**
     * hero撞击后 操作
     */
    fun handleHeroAttack(iEnemy: IEnemy): Int {
        iEnemy.blowUp()
        return iEnemy.score
    }

    /**
     * 子弹撞击后操作
     */
    fun handleBulletAttack(iEnemy: IEnemy, atk: Int): Int {
        return iEnemy.hitted(atk)
    }

    val enemyList: List<IEnemy>
        get() = mEnemyListTotal

}