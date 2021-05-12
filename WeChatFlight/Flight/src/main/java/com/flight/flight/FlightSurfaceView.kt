package com.flight.flight

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.flight.activity.MainController
import com.flight.canvas.common.ContextData
import com.flight.canvas.common.MeasureToData
import com.flight.utils.ThreadPoolUtil
import com.yline.log.LogUtil

/**
 * 确立一些背景参数
 * 然后传参给Controller控制
 * @author yline
 * @date 2016-4-2
 */
class FlightSurfaceView constructor(context: Context, attrs: AttributeSet? = null) : SurfaceView(context, attrs), SurfaceHolder.Callback, Runnable {
    private val mSurfaceHolder: SurfaceHolder

    private var isDrawing = false

    // 背景大参数
    private var mBgPaint: Paint = Paint()

    // 其它参数
    private var mMainController: MainController? = null

    // 交互数据
//    private val initData = InitData()
//    private val measureFromData = MeasureFromData()
    private val measureToData = MeasureToData()
    private val contextData = ContextData(context)

    private var mScaleX = 0f
    private var mScaleY = 0f
    private val mMatrix = Matrix()

    private var mBgX = 0f
    private var mBgY = 0f

    init {
        this.keepScreenOn = true

        mSurfaceHolder = this.holder
        // 继承SurfaceView类并实现SurfaceHolder.Callback接口就可以实现一个自定义的SurfaceView
        mSurfaceHolder.addCallback(this)

        // 设置成这个,对于界面的按钮就是可点击的了
        this.isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // 只有在这里才能拿到具体的值,而不是零
        val bgWidth = this.width
        val bgHeight = this.height

        mBgPaint.color = Color.BLACK
        mBgPaint.isAntiAlias = true

        mMainController = MainController()
        mMainController?.onMainInit(contextData)

        LogUtil.v("mBgWidth = $bgWidth,mBgHeight = $bgHeight")
        isDrawing = true

        mScaleX = bgWidth * 1.0f / contextData.mapWidth
        mScaleY = bgHeight * 1.0f / contextData.mapHeight
        mMatrix.setScale(mScaleX, mScaleY)

        ThreadPoolUtil.execute(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isDrawing = false
    }

    override fun run() {
        var startTime = System.nanoTime() // 毫微秒
        while (isDrawing) {
            // 确定刷新时间间隔的参数
            val spaceTime = (System.nanoTime() - startTime) / 1000_000_000.0f // 秒 单位
            startTime = System.nanoTime() // 重新赋值

            if (!contextData.isPause) {
                contextData.spaceTime = spaceTime
                contextData.spaceHeight = spaceTime * contextData.mapHeight / 20f  // 20s 运行完一个 bitmap

                try {
                    mMainController?.onThreadMeasure(contextData, measureToData)
                    mMainController?.onThreadAttack(measureToData, contextData)

                    synchronized(mSurfaceHolder) {
                        val canvas = mSurfaceHolder.lockCanvas()
                        canvas?.let {
                            it.save() // 配套使用
                            it.concat(mMatrix)
                            mMainController?.onThreadDraw(it, contextData)
                            it.restore() // 配套使用
                        }
                        mSurfaceHolder.unlockCanvasAndPost(canvas)
                    }
                } catch (ex: Throwable) {
                    LogUtil.e("exception", ex)
                }
            }
        }

        Thread.sleep(1000)
    }

    private var downX = 0f
    private var downY = 0f

    private var isHeroTouched = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        setBgXY(event.x, event.y)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                LogUtil.v("mBgX = $mBgX, mBgY = $mBgY")
                downX = x
                downY = y

                isHeroTouched = measureToData.hero.getRectF().contains(mBgX, mBgY)
            }
            MotionEvent.ACTION_MOVE -> {
                // 非 暂停 状态下，飞机移动
                if (!contextData.isPause && isHeroTouched) {
                    mMainController?.controlHero(mBgX, mBgY)
                }
            }
            MotionEvent.ACTION_UP -> {
                // 确定是点击事件
                if (Math.abs(x - downX) < 10 && Math.abs(y - downY) < 10) {
                    // 点击 大炸弹使用
                    val isClickBigBomb = contextData.bigBombRect.contains(mBgX, mBgY)
                    if (!contextData.isPause && isClickBigBomb && contextData.supply1Num > 0) {
                        contextData.supply1Num -= 1

                        // todo 炸掉 界面所有的 敌机
                    }

                    // 点击 暂停 或 开始
                    val isClickPause = contextData.pauseRect.contains(mBgX, mBgY)
                    if (isClickPause) {
                        contextData.isPause = !contextData.isPause
                    }
                }

                isHeroTouched = false
            }
        }
        return true
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

    fun gameStop() {
        isDrawing = false
    }
}