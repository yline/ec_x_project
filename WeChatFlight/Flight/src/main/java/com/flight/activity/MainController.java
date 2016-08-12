package com.flight.activity;

import com.flight.canvas.FlightMap;
import com.flight.canvas.FlightVariable;
import com.flight.canvas.enemy.EnemyController;
import com.flight.canvas.enemy.IEnemy;
import com.flight.canvas.hero.FlightHero;
import com.flight.canvas.hero.IBullet;
import com.flight.canvas.supply.ISupply;
import com.flight.canvas.supply.SupplyController;
import com.yline.utils.LogUtil;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class MainController
{
    private Resources mResources;
    
    private Rect mBgRect; // 背景
    
    private Rect mMapRect; // 资源文件
    
    private Paint mBgPaint;
    
    // 转换关系,backGroud 和 背景资源文件
    private float mScaleX, mScaleY;
    
    private Matrix mMatrix;
    
    private Paint mScorePaint;
    
    // controller
    private FlightMap mFlightMap;
    
    private FlightVariable mFlightVariable;
    
    private FlightHero mFlightHero;
    
    private SupplyController mSupplyController;
    
    private EnemyController mEnemyController;
    
    public MainController(Resources resources, Rect rect, Paint paint)
    {
        this.mResources = resources;
        this.mBgRect = rect;
        this.mBgPaint = paint;
    }
    
    /** 初始化一些数据 */
    public void init()
    {
        this.mScorePaint = new Paint();
        this.mScorePaint.setColor(Color.rgb(60, 60, 60)); // 颜色
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC); // 字体
        this.mScorePaint.setTypeface(font);
        this.mScorePaint.setTextSize(30);
        
        mFlightMap = new FlightMap();
        mFlightMap.initResource(mResources);
        mMapRect = new Rect(0, 0, mFlightMap.getMapWidth(), mFlightMap.getMapHeight());
        
        mFlightVariable = new FlightVariable();
        mFlightVariable.initResource(mResources, mMapRect.width(), mMapRect.height());
        
        mFlightHero = new FlightHero();
        mFlightHero.initResource(mResources, mMapRect);
        
        mEnemyController = new EnemyController();
        mEnemyController.initResource(mResources, mMapRect);
        
        mSupplyController = new SupplyController();
        mSupplyController.initResource(mResources, mMapRect);
        
        this.mMatrix = new Matrix();
        mScaleX = mBgRect.width() * 1.0f / mMapRect.width();
        mScaleY = mBgRect.height() * 1.0f / mMapRect.height();
        this.mMatrix.setScale(mScaleX, mScaleY);
    }
    
    /**
     * 更新数据
     * @param durateTime	间隔时间
     */
    public void updateFrame(float durateTime)
    {
        mFlightMap.caculateMap();
        mFlightMap.setVelocity(durateTime, 20);
        
        mFlightVariable.setBigBombNumber(mFlightHero.getBigBombNumber());
        mFlightVariable.setTotalScore(mFlightHero.getScore());
        
        mFlightHero.caculateFlightHero(durateTime, -12 * mFlightMap.getVelocity());
        // handleSupplyAttack	// 需要条件触发	
        // handleEnemyAttack	// 需要条件触发	
        // handleBulletAttack	// 需要条件触发	
        
        mSupplyController.caculateSupply(durateTime, 2 * mFlightMap.getVelocity(), 10, 5);
        // handleHeroAttack	// 需要条件触发
        
        mEnemyController.caculateEnemy(durateTime, 2 * mFlightMap.getVelocity(), 10, 10, 10);
        // handleHeroAttack	// 需要条件触发
        // handleBulletAttack	// 需要条件触发
        
        // 撞击操作,这是公共的部分,就公共的操作
        // 爆炸状态不算;only 正常状态
        // 1,hero + supply遍历,supply消失、hero加属性
        for (ISupply iSupply : mSupplyController.getSupplyList())
        {
            if (iSupply.isNormal() && mFlightHero.isNormal())
            { // 正常状态
                if (Rect.intersects(mFlightHero.getHeroRect(), iSupply.getRect()))
                {
                    mFlightHero.handleSupplyAttack(iSupply);
                    mSupplyController.handleHeroAttack(iSupply);
                }
            }
        }
        
        for (IEnemy iEnemy : mEnemyController.getEnemyList())
        {
            // 2,hero + enemy遍历,enemy状态处于爆炸状态、hero处于爆炸状态
            if (mFlightHero.isNormal() && iEnemy.isRunning())
            { // 正常状态
                if (Rect.intersects(mFlightHero.getHeroRect(), iEnemy.getRect()))
                {
                    mFlightHero.handleEnemyAttack(); // 拿分
                    mFlightHero.addScore(mEnemyController.handleHeroAttack(iEnemy));
                    
                    isGameOver = true;
                }
            }
            
            // 3,子弹 + enemy遍历,enemy自身判断、bullet消失处理
            for (IBullet iBullet : mFlightHero.getBulletList())
            {
                if (iEnemy.isRunning() && iBullet.isRunning())
                {
                    if (Rect.intersects(iBullet.getRect(), iEnemy.getRect()))
                    {
                        mFlightHero.handleBulletAttack(iBullet); // 拿分
                        mFlightHero.addScore(mEnemyController.handleBulletAttack(iEnemy, iBullet.getATK()));
                    }
                }
            }
        }
        
        if (isGameOver)
        {
            if (gameOverDelay < 0)
            {
                isGameOver = false;
                // 游戏结束	MainActivity 中直接操作了
                MainFlight.getInstance().setGameOverCallback(mFlightHero.getScore());
            }
            else
            {
                gameOverDelay -= durateTime;
            }
        }
    }
    
    private boolean isGameOver = false;
    
    private float gameOverDelay = 2.0f; // 延迟 1s 暂停
    
    /**
     * 是否为暂停状态
     * @return
     */
    public boolean isPause()
    {
        return isPause;
    }
    
    /**
     * 更新界面
     * @param canvas	绘制的画布
     */
    public void renderFrame(Canvas canvas)
    {
        canvas.save(); // 配套使用
        canvas.concat(mMatrix);
        
        mFlightMap.drawMap(canvas, mBgPaint);
        mFlightHero.drawHero(canvas, mBgPaint); //	带子弹
        mSupplyController.drawSupplies(canvas, mBgPaint);
        mEnemyController.drawEnemies(canvas, mBgPaint);
        mFlightVariable.drawVariable(canvas, mScorePaint, isPause);
        
        canvas.restore(); // 配套使用
    }
    
    // 暂停按钮点击
    private boolean isControllHero = false;
    
    // 大炸弹 点击
    private boolean isClickBigBomb = false;
    
    // 暂停按钮 点击
    private boolean isClickPause = false;
    
    // 是否为暂停状态
    private boolean isPause = false;
    
    private float downX, downY;
    
    private float mBgX, mBgY;
    
    public void onTouchDown(float x, float y)
    {
        LogUtil.v("mBgX = " + mBgX + ",mBgY = " + mBgY);
        this.downX = x;
        this.downY = y;
        setBgXY(x, y);
        isClickBigBomb = mFlightVariable.getBigBombRect().contains((int)mBgX, (int)mBgY);
        isClickPause = mFlightVariable.getPauseRect().contains((int)mBgX, (int)mBgY);
        isControllHero = mFlightHero.getHeroRect().contains((int)mBgX, (int)mBgY);
    }
    
    public void onTouchMove(float x, float y)
    {
        setBgXY(x, y);
        if (!isPause && isControllHero)
        {
            mFlightHero.moveTo(mBgX, mBgY);
        }
    }
    
    public void onTouchUp(float x, float y)
    {
        setBgXY(x, y);
        if (Math.abs(x - downX) < 10 && Math.abs(y - downY) < 10)
        { // 移动范围小
            if (!isPause && isClickBigBomb)
            { // 在范围内,并且不是 暂停
                if (mFlightHero.getBigBombNumber() > 0)
                {
                    mFlightHero.reduceBigBombNumber();
                    mFlightHero.addScore(mEnemyController.handleBigBombing());
                }
            }
            if (isClickPause)
            {
                isPause = !isPause;
            }
        }
        isClickBigBomb = false;
        isControllHero = false;
    }
    
    /**
     * 百分比适配需要
     * @param x
     * @param y
     */
    private void setBgXY(float x, float y)
    {
        this.mBgX = x / mScaleX;
        this.mBgY = y / mScaleY;
    }
}
