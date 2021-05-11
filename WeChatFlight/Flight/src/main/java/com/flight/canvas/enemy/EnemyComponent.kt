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
    private lateinit var mMapRect: Rect

    private lateinit var mResources: Resources

    private lateinit var mEnemy1: IEnemy
    private var mEnemyNumber1: Int = 0
    private lateinit var mEnemy2: IEnemy
    private var mEnemyNumber2: Int = 0
    private lateinit var mEnemy3: IEnemy
    private var mEnemyNumber3: Int = 0

    private val mEnemyListTotal: MutableList<IEnemy> = ArrayList()

    private var nextEnemyTime = 0.0f

    override fun onMainInit(context: Context, initData: InitData) {
        mResources = context.resources

        mMapRect = Rect(0, 0, initData.mapWidth, initData.mapHeight)

        mEnemy1 = Enemy1(mResources, mRandom, initData).init()
        mEnemy2 = Enemy2(mResources, mRandom, initData).init()
        mEnemy3 = Enemy3(mResources, mRandom, initData).init()
    }

    private fun newEnemy(spaceTime: Float): IEnemy? {
        // 还没到 供给 时间
        if (nextEnemyTime > 0) {
            nextEnemyTime -= spaceTime
            return null
        }

        // 重新设值
        nextEnemyTime = 1.0f + mRandom.nextInt(3)

        // 供给 时间 到了
        val nextInt = mRandom.nextInt(3)
        if (nextInt >= 2) {
            return mEnemy1.clone()
        } else if (nextInt == 1) {
            return mEnemy2.clone()
        } else {
            return mEnemy3.clone()
        }
    }

    override fun onThreadMeasure(fromData: MeasureFromData, toData: MeasureToData) {
        // 新的 敌机 出现
        newEnemy(fromData.spaceTime)?.let {
            mEnemyListTotal.add(it)
        }

        // 敌机 销毁 移除
        mEnemyListTotal.removeAll { it.isDestroy() }

        val height = 2 * fromData.spaceHeight

        // 当前 敌机 运行
        for (iEnemy in mEnemyListTotal) {
            iEnemy.move(0.0f, height)
        }
    }

    override fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {
    }

    private val paint = Paint()

    override fun onThreadDraw(canvas: Canvas, attackData: AttackData) {
        for (iEnemy in mEnemyListTotal) {
            iEnemy.draw(canvas, paint)
        }
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

//    /**
//     * 大炸弹 的操作
//     * @return
//     */
//    fun handleBigBombing(): Int {
//        var score = 0
//        for (iEnemy in mEnemyListTotal) {
//            score += iEnemy.hitted(Int.MAX_VALUE)
//        }
//        return score
//    }

//    /**
//     * hero撞击后 操作
//     */
//    fun handleHeroAttack(iEnemy: IEnemy): Int {
//        iEnemy.blowUp()
//        return iEnemy.score
//    }

//    /**
//     * 子弹撞击后操作
//     */
//    fun handleBulletAttack(iEnemy: IEnemy, atk: Int): Int {
//        return iEnemy.hitted(atk)
//    }

    val enemyList: List<IEnemy>
        get() = mEnemyListTotal

}