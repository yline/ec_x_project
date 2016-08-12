package com.flight.activity;

import com.yline.utils.LogUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 确立一些背景参数
 * 然后传参给Controller控制
 * @author yline
 * @date 2016-4-2
 */
public class MainSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    private SurfaceHolder mSurfaceHolder;
    
    private boolean isDrawing;
    
    private Thread mThread;
    
    // 背景大参数
    private Canvas mCanvas;
    
    private int mBgWidth, mBgHeight;
    
    private Rect mBgRect;
    
    private Paint mBgPaint;
    
    // 其它参数
    private MainController mMainController;
    
    public MainSurfaceView(Context context)
    {
        this(context, null);
    }
    
    public MainSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        
        this.setKeepScreenOn(true);
        
        mSurfaceHolder = this.getHolder();
        // 继承SurfaceView类并实现SurfaceHolder.Callback接口就可以实现一个自定义的SurfaceView
        mSurfaceHolder.addCallback(this);
        
        // 设置成这个,对于界面的按钮就是可点击的了
        this.setFocusable(true);
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // 只有在这里才能拿到具体的值,而不是零
        this.mBgWidth = this.getWidth();
        this.mBgHeight = this.getHeight();
        this.mBgRect = new Rect(0, 0, mBgWidth, mBgHeight);
        
        this.mBgPaint = new Paint();
        this.mBgPaint.setColor(Color.BLACK);
        this.mBgPaint.setAntiAlias(true);
        
        mMainController = new MainController(this.getResources(), mBgRect, mBgPaint);
        mMainController.init();
        
        LogUtil.v("mBgWidth = " + mBgWidth + ",mBgHeight = " + mBgHeight);
        isDrawing = true;
        
        mThread = new Thread(this, "surfaceview");
        mThread.start();
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        isDrawing = false;
    }
    
    @Override
    public void run()
    {
        long startTime = System.nanoTime(); // 毫微秒
        while (isDrawing)
        {
            // 确定刷新时间间隔的参数
            float durateTime = (System.nanoTime() - startTime) / 1000000000.0f; // 秒 单位
            startTime = System.nanoTime(); // 重新赋值
            
            if (!mMainController.isPause())
            {
                mMainController.updateFrame(durateTime);
                
                synchronized (mSurfaceHolder)
                {
                    mCanvas = mSurfaceHolder.lockCanvas();
                    if (null != mCanvas)
                    {
                        mCanvas.drawRect(mBgRect, mBgPaint); // 确定绘制的范围
                        
                        mMainController.renderFrame(mCanvas);
                        
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    }
                }
            }
        }
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mMainController.onTouchDown(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mMainController.onTouchMove(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                mMainController.onTouchUp(event.getX(), event.getY());
                break;
        }
        return true;
    }
    
    public void gameStop()
    {
        isDrawing = false;
    }
}
