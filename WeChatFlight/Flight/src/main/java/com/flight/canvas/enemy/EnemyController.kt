package com.flight.canvas.enemy

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.flight.application.MainApplication
import com.yline.log.LogFileUtil.v
import java.util.*

class EnemyController(resources: Resources, rect: Rect) {
    private val mRandom: Random
    private val isEnd = false // hero 是否 死了
    private var mMapRect: Rect
    private var mResources: Resources
    private var mEnemy1: IEnemy
    private var mEnemyNumber1: Int
    private var mEnemy2: IEnemy
    private var mEnemyNumber2: Int
    private var mEnemy3: IEnemy
    private var mEnemyNumber3: Int
    private val mEnemyListTotal: MutableList<IEnemy>

    init {
        mRandom = Random()
        mEnemyListTotal = ArrayList()
        mEnemyNumber1 = 0
        mEnemyNumber2 = 0
        mEnemyNumber3 = 0
    }

    init {
        mResources = resources
        mMapRect = rect
        mEnemy1 = Enemy1(mResources, mRandom, mMapRect, 0)
        mEnemy2 = Enemy2(mResources, mRandom, mMapRect, 0)
        mEnemy3 = Enemy3(mResources, mRandom, mMapRect, 0)
    }


    private var isStart = false
    fun caculateEnemy(durateTime: Float, height: Float, frizTime1: Float, frizTime2: Float, frizTime3: Float) {
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