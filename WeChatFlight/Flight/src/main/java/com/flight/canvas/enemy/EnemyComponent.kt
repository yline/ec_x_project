package com.flight.canvas.enemy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.flight.canvas.common.*
import com.yline.log.LogUtil
import java.util.*

class EnemyComponent() : BaseComponent() {
    private val mRandom: Random = Random()

    private lateinit var mEnemy1: IEnemy
    private lateinit var mEnemy2: IEnemy
    private lateinit var mEnemy3: IEnemy

    private val mEnemyListTotal: MutableList<IEnemy> = ArrayList()

    private var nextEnemyTime = 0.0f

    private val paint = Paint()

    override fun onMainInit(context: Context, initData: InitData) {
        val resources = context.resources

        mEnemy1 = Enemy1(resources, mRandom, initData).init()
        mEnemy2 = Enemy2(resources, mRandom, initData).init()
        mEnemy3 = Enemy3(resources, mRandom, initData).init()
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

        val height = 2 * fromData.spaceHeight

        // 当前 敌机 运行
        for (iEnemy in mEnemyListTotal) {
            iEnemy.move(0.0f, height)
        }
    }

    override fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {

    }

    override fun onThreadDraw(canvas: Canvas, attackData: AttackData) {
        // 敌机 销毁 移除
        mEnemyListTotal.removeAll { it.isDestroy() }

        if (mEnemyListTotal.size > 10) {
            LogUtil.e("enemySize = ${mEnemyListTotal.size}")
        }

        for (iEnemy in mEnemyListTotal) {
            iEnemy.draw(canvas, paint)
        }
    }

//    private var isStart = false

//    /**
//     * 出界就减少个数
//     * @param iEnemy
//     */
//    private fun reduceEnemyNumber(iEnemy: IEnemy?) {
//        if (iEnemy is Enemy1) {
//            mEnemyNumber1 -= 1
//        } else if (iEnemy is Enemy2) {
//            mEnemyNumber2 -= 1
//        } else if (iEnemy is Enemy3) {
//            mEnemyNumber3 -= 1
//        }
//        v("number1 = $mEnemyNumber1,number2 = $mEnemyNumber2,number3 = $mEnemyNumber3")
//    }

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
//
//    val enemyList: List<IEnemy>
//        get() = mEnemyListTotal

}