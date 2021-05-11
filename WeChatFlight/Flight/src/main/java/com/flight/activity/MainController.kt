package com.flight.activity

import android.content.Context
import android.graphics.*
import com.flight.canvas.common.*
import com.flight.canvas.map.MapComponent
import com.flight.canvas.variable.VariableComponent
import com.flight.canvas.enemy.EnemyComponent
import com.flight.canvas.hero.HeroComponent
import com.flight.canvas.supply.SupplyComponent
import com.yline.log.LogUtil

class MainController(private val mBgRect: Rect, private val mBgPaint: Paint) : BaseComponent() {
    // 转换关系,backGroud 和 背景资源文件
    private var mScaleX = 0f
    private var mScaleY = 0f
    private val mMatrix = Matrix()

    // controller
    private val mapComponent = MapComponent()
    private var variableComponent = VariableComponent()
    private var heroComponent = HeroComponent()
    private var supplyComponent = SupplyComponent()
    private var enemyComponent = EnemyComponent()

    private val componentList: List<BaseComponent> = arrayListOf(
            mapComponent, variableComponent, heroComponent,
            supplyComponent, enemyComponent
    )

    override fun onMainInit(context: Context, initData: InitData) {
        for (component in componentList) {
            component.onMainInit(context, initData)
        }

        mScaleX = mBgRect.width() * 1.0f / initData.mapWidth
        mScaleY = mBgRect.height() * 1.0f / initData.mapHeight
        mMatrix.setScale(mScaleX, mScaleY)
    }

    override fun onThreadMeasure(fromData: MeasureFromData, toData: MeasureToData) {
        variableComponent.setBigBombNumber(heroComponent.bigBombNumber)
        variableComponent.setTotalScore(heroComponent.score)

        if (isGameOver) {
            if (gameOverDelay < 0) {
                isGameOver = false
                // 游戏结束	MainActivity 中直接操作了
                MainFlight.instance.setGameOverCallback(heroComponent.score)
            } else {
                gameOverDelay -= fromData.spaceTime
            }
        }

        for (component in componentList) {
            component.onThreadMeasure(fromData, toData)
        }
    }

    override fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {
        // 撞击操作,这是公共的部分,就公共的操作
        // 爆炸状态不算;only 正常状态
        // 1,hero + supply遍历,supply消失、hero加属性
//        for (iSupply in supplyComponent.supplyList) {
//            if (iSupply.isNormal && heroComponent.isNormal) { // 正常状态
//                if (Rect.intersects(heroComponent.heroRect, iSupply.rect!!)) {
//                    heroComponent.handleSupplyAttack(iSupply)
//                    supplyComponent.handleHeroAttack(iSupply)
//                }
//            }
//        }
//        for (iEnemy in enemyComponent.enemyList) {
//            // 2,hero + enemy遍历,enemy状态处于爆炸状态、hero处于爆炸状态
//            if (heroComponent.isNormal && iEnemy.isRunning) { // 正常状态
//                if (Rect.intersects(heroComponent.heroRect, iEnemy.rect!!)) {
//                    heroComponent.handleEnemyAttack() // 拿分
//                    heroComponent.addScore(enemyComponent.handleHeroAttack(iEnemy))
//                    isGameOver = true
//                }
//            }
//
//            // 3,子弹 + enemy遍历,enemy自身判断、bullet消失处理
//            for (iBullet in heroComponent.bulletList) {
//                if (iEnemy.isRunning && iBullet.isRunning) {
//                    if (Rect.intersects(iBullet.rect!!, iEnemy.rect!!)) {
//                        heroComponent.handleBulletAttack(iBullet) // 拿分
//                        heroComponent.addScore(enemyComponent.handleBulletAttack(iEnemy, iBullet.atk))
//                    }
//                }
//            }
//        }

        for (component in componentList) {
            component.onThreadAttack(toData, attackData)
        }

        attackData.isPause = isPause
    }

    override fun onThreadDraw(canvas: Canvas, attackData: AttackData) {
        canvas.save() // 配套使用
        canvas.concat(mMatrix)

        for (component in componentList) {
            component.onThreadDraw(canvas, attackData)
        }

        canvas.restore() // 配套使用
    }

    private var isGameOver = false
    private var gameOverDelay = 2.0f // 延迟 1s 暂停

    // 暂停按钮点击
    private var isControllHero = false

    // 大炸弹 点击
    private var isClickBigBomb = false

    // 暂停按钮 点击
    private var isClickPause = false

    /**
     * 是否为暂停状态
     *
     * @return
     */
    // 是否为暂停状态
    var isPause = false
        private set
    private var downX = 0f
    private var downY = 0f
    private var mBgX = 0f
    private var mBgY = 0f

    fun onTouchDown(x: Float, y: Float) {
        LogUtil.v("mBgX = $mBgX,mBgY = $mBgY")
        downX = x
        downY = y
        setBgXY(x, y)
        isClickBigBomb = variableComponent.bigBombRect.contains(mBgX.toInt(), mBgY.toInt())
        isClickPause = variableComponent.pauseRect.contains(mBgX.toInt(), mBgY.toInt())
        isControllHero = heroComponent.heroRect.contains(mBgX.toInt(), mBgY.toInt())
    }

    fun onTouchMove(x: Float, y: Float) {
        setBgXY(x, y)
        if (!isPause && isControllHero) {
            heroComponent.moveTo(mBgX, mBgY)
        }
    }

    fun onTouchUp(x: Float, y: Float) {
        setBgXY(x, y)
        if (Math.abs(x - downX) < 10 && Math.abs(y - downY) < 10) { // 移动范围小
            if (!isPause && isClickBigBomb) { // 在范围内,并且不是 暂停
                if (heroComponent.bigBombNumber > 0) {
                    heroComponent.reduceBigBombNumber()
//                    heroComponent.addScore(enemyComponent.handleBigBombing())
                }
            }
            if (isClickPause) {
                isPause = !isPause
            }
        }
        isClickBigBomb = false
        isControllHero = false
    }

    /**
     * 百分比适配需要
     *
     * @param x
     * @param y
     */
    private fun setBgXY(x: Float, y: Float) {
        mBgX = x / mScaleX
        mBgY = y / mScaleY
    }


}