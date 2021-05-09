package com.flight.activity

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import com.flight.canvas.map.FlightMapComponent
import com.flight.canvas.variable.FlightVariableComponent
import com.flight.canvas.common.BaseComponent
import com.flight.canvas.common.FlightData
import com.flight.canvas.enemy.EnemyController
import com.flight.canvas.hero.FlightHero
import com.flight.canvas.supply.SupplyController
import com.yline.log.LogUtil

class MainController(private val mResources: Resources, // 背景
                     private val mBgRect: Rect, private val mBgPaint: Paint) : BaseComponent() {
    private lateinit var mMapRect: Rect  // 资源文件

    // 转换关系,backGroud 和 背景资源文件
    private var mScaleX = 0f
    private var mScaleY = 0f
    private lateinit var mMatrix: Matrix
    private lateinit var mScorePaint: Paint

    // controller
    private lateinit var mFlightHero: FlightHero
    private lateinit var mSupplyController: SupplyController
    private lateinit var mEnemyController: EnemyController

    private val mapComponent = FlightMapComponent()
    private var variableComponent = FlightVariableComponent()

    private val componentList: List<BaseComponent> = arrayListOf(
            mapComponent,
            variableComponent
    )

    override fun onMainInit(context: Context) {
        for (component in componentList) {
            // 首个
            if (component is FlightMapComponent) {
                component.onMainInit(context)

                val flightData = FlightData()
                flightData.mapWidth = mapComponent.getMapWidth()
                flightData.mapHeight = mapComponent.getMapHeight()
                provide(flightData)
                continue
            }

            component.onMainInit(context)
        }

        mScorePaint = Paint()
        mScorePaint.color = Color.rgb(60, 60, 60) // 颜色
        val font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC) // 字体
        mScorePaint.typeface = font
        mScorePaint.textSize = 30f

        mMapRect = Rect(0, 0, mapComponent.getMapWidth(), mapComponent.getMapHeight())
        mFlightHero = FlightHero(mResources, mMapRect)

        mSupplyController = SupplyController(mResources, mMapRect)
        mEnemyController = EnemyController(mResources, mMapRect)

        mMatrix = Matrix()
        mScaleX = mBgRect.width() * 1.0f / mMapRect.width()
        mScaleY = mBgRect.height() * 1.0f / mMapRect.height()
        mMatrix.setScale(mScaleX, mScaleY)
    }

    override fun onThreadMeasure(diffHeight: Float) {
        variableComponent.setBigBombNumber(mFlightHero.bigBombNumber)
        variableComponent.setTotalScore(mFlightHero.score)

//        mFlightHero.caculateFlightHero(durateTime, -12 * mFlightMap.velocity)
//        // handleSupplyAttack	// 需要条件触发
//        // handleEnemyAttack	// 需要条件触发
//        // handleBulletAttack	// 需要条件触发
//        mSupplyController.caculateSupply(durateTime, 2 * mFlightMap.velocity, 10f, 5f)
//        // handleHeroAttack	// 需要条件触发
//        mEnemyController.caculateEnemy(durateTime, 2 * mFlightMap.velocity, 10f, 10f, 10f)
        // handleHeroAttack	// 需要条件触发
        // handleBulletAttack	// 需要条件触发

        // 撞击操作,这是公共的部分,就公共的操作
        // 爆炸状态不算;only 正常状态
        // 1,hero + supply遍历,supply消失、hero加属性
//        for (iSupply in mSupplyController.supplyList) {
//            if (iSupply.isNormal && mFlightHero.isNormal) { // 正常状态
//                if (Rect.intersects(mFlightHero.heroRect, iSupply.rect)) {
//                    mFlightHero.handleSupplyAttack(iSupply)
//                    mSupplyController.handleHeroAttack(iSupply)
//                }
//            }
//        }
//        for (iEnemy in mEnemyController.enemyList) {
//            // 2,hero + enemy遍历,enemy状态处于爆炸状态、hero处于爆炸状态
//            if (mFlightHero.isNormal && iEnemy.isRunning) { // 正常状态
//                if (Rect.intersects(mFlightHero.heroRect, iEnemy.rect)) {
//                    mFlightHero.handleEnemyAttack() // 拿分
//                    mFlightHero.addScore(mEnemyController.handleHeroAttack(iEnemy))
//                    isGameOver = true
//                }
//            }
//
//            // 3,子弹 + enemy遍历,enemy自身判断、bullet消失处理
//            for (iBullet in mFlightHero.bulletList) {
//                if (iEnemy.isRunning && iBullet.isRunning) {
//                    if (Rect.intersects(iBullet.rect, iEnemy.rect)) {
//                        mFlightHero.handleBulletAttack(iBullet) // 拿分
//                        mFlightHero.addScore(mEnemyController.handleBulletAttack(iEnemy, iBullet.atk))
//                    }
//                }
//            }
//        }
//        if (isGameOver) {
//            if (gameOverDelay < 0) {
//                isGameOver = false
//                // 游戏结束	MainActivity 中直接操作了
//                MainFlight.instance.setGameOverCallback(mFlightHero.score)
//            } else {
//                gameOverDelay -= durateTime
//            }
//        }

        for (component in componentList) {
            component.onThreadMeasure(diffHeight)
        }
    }

    override fun onThreadDraw(canvas: Canvas) {
        canvas.save() // 配套使用
        canvas.concat(mMatrix)
        mFlightHero.drawHero(canvas, mBgPaint) //	带子弹
        mSupplyController.drawSupplies(canvas, mBgPaint)
        mEnemyController.drawEnemies(canvas, mBgPaint)
        variableComponent.drawVariable(canvas, mScorePaint, isPause)
        canvas.restore() // 配套使用

        for (component in componentList) {
            component.onThreadDraw(canvas)
        }
    }

    /**
     * 更新数据
     *
     * @param durateTime 间隔时间
     */
    fun updateFrame(durateTime: Float) {
    }

    private var isGameOver = false
    private var gameOverDelay = 2.0f // 延迟 1s 暂停

    /**
     * 更新界面
     *
     * @param canvas 绘制的画布
     */
    fun renderFrame(canvas: Canvas) {
    }

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
        isControllHero = mFlightHero.heroRect.contains(mBgX.toInt(), mBgY.toInt())
    }

    fun onTouchMove(x: Float, y: Float) {
        setBgXY(x, y)
        if (!isPause && isControllHero) {
            mFlightHero.moveTo(mBgX, mBgY)
        }
    }

    fun onTouchUp(x: Float, y: Float) {
        setBgXY(x, y)
        if (Math.abs(x - downX) < 10 && Math.abs(y - downY) < 10) { // 移动范围小
            if (!isPause && isClickBigBomb) { // 在范围内,并且不是 暂停
                if (mFlightHero.bigBombNumber > 0) {
                    mFlightHero.reduceBigBombNumber()
                    mFlightHero.addScore(mEnemyController.handleBigBombing())
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