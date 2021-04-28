package com.flight.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.yline.log.LogUtil

/**
 * 确立一些背景参数
 * 然后传参给Controller控制
 * @author yline
 * @date 2016-4-2
 */
class MainSurfaceView constructor(context: Context?, attrs: AttributeSet? = null) : SurfaceView(context, attrs), SurfaceHolder.Callback, Runnable {
    private val mSurfaceHolder: SurfaceHolder

    private var isDrawing = false

    // 背景大参数
    private var mCanvas: Canvas? = null
    private var mBgWidth = 0
    private var mBgHeight = 0
    private var mBgRect: Rect = Rect()
    private var mBgPaint: Paint = Paint()

    // 其它参数
    private var mMainController: MainController? = null

    init {
        this.keepScreenOn = true

        mSurfaceHolder = this.holder
        // 继承SurfaceView类并实现SurfaceHolder.Callback接口就可以实现一个自定义的SurfaceView
        mSurfaceHolder.addCallback(this)

        // 设置成这个,对于界面的按钮就是可点击的了
        this.isFocusable = true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {
        // 只有在这里才能拿到具体的值,而不是零
        mBgWidth = this.width
        mBgHeight = this.height

        mBgRect.set(0, 0, mBgWidth, mBgHeight)
        mBgPaint.color = Color.BLACK
        mBgPaint.isAntiAlias = true
        mMainController = MainController(this.resources, mBgRect, mBgPaint)
        mMainController?.init()

        LogUtil.v("mBgWidth = $mBgWidth,mBgHeight = $mBgHeight")
        isDrawing = true

        Thread(this, "surfaceview").start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isDrawing = false
    }

    override fun run() {
        var startTime = System.nanoTime() // 毫微秒
        while (isDrawing) {
            // 确定刷新时间间隔的参数
            val durateTime = (System.nanoTime() - startTime) / 1000000000.0f // 秒 单位
            startTime = System.nanoTime() // 重新赋值
            if (mMainController?.isPause != true) {
                mMainController?.updateFrame(durateTime)

                synchronized(mSurfaceHolder) {
                    mCanvas = mSurfaceHolder.lockCanvas()
                    if (null != mCanvas) {
                        mCanvas?.drawRect(mBgRect, mBgPaint) // 确定绘制的范围
                        mMainController?.renderFrame(mCanvas!!)
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas)
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mMainController?.onTouchDown(event.x, event.y)
            MotionEvent.ACTION_MOVE -> mMainController?.onTouchMove(event.x, event.y)
            MotionEvent.ACTION_UP -> mMainController?.onTouchUp(event.x, event.y)
        }
        return true
    }

    fun gameStop() {
        isDrawing = false
    }
}