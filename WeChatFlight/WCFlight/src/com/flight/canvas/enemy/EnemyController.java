package com.flight.canvas.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.flight.application.MainApplication;
import com.yline.log.LogFileUtil;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class EnemyController
{
    private Random mRandom;
    
    private boolean isEnd = false; // hero 是否 死了
    
    private Rect mMapRect;
    
    private Resources mResources;
    
    private IEnemy mEnemy1;
    
    private int mEnemyNumber1;
    
    private IEnemy mEnemy2;
    
    private int mEnemyNumber2;
    
    private IEnemy mEnemy3;
    
    private int mEnemyNumber3;
    
    private List<IEnemy> mEnemyListTotal;
    
    public EnemyController()
    {
        this.mRandom = new Random();
        this.mEnemyListTotal = new ArrayList<IEnemy>();
        this.mEnemyNumber1 = 0;
        this.mEnemyNumber2 = 0;
        this.mEnemyNumber3 = 0;
    }
    
    public void initResource(Resources resources, Rect rect)
    {
        this.mResources = resources;
        this.mMapRect = rect;
        
        mEnemy1 = new Enemy1(mResources, mRandom, mMapRect);
        mEnemy2 = new Enemy2(mResources, mRandom, mMapRect);
        mEnemy3 = new Enemy3(mResources, mRandom, mMapRect);
    }
    
    private boolean isStart;
    
    public void caculateEnemy(float durateTime, float height, float frizTime1, float frizTime2, float frizTime3)
    {
        if (!isEnd)
        {
            // 敌机产生 时间间隔
            isStart = mEnemy1.start(durateTime, frizTime1);
            if (isStart)
            {
                mEnemyListTotal.add(mEnemy1);
                mEnemyNumber1 += 1;
                mEnemy1 = new Enemy1(mResources, mRandom, mMapRect);
            }
            
            isStart = mEnemy2.start(durateTime, frizTime2);
            if (isStart)
            {
                mEnemyListTotal.add(mEnemy2);
                mEnemyNumber2 += 1;
                mEnemy2 = new Enemy2(mResources, mRandom, mMapRect);
            }
            
            isStart = mEnemy3.start(durateTime, frizTime3);
            if (isStart)
            {
                mEnemyListTotal.add(mEnemy3);
                mEnemyNumber3 += 1;
                mEnemy3 = new Enemy3(mResources, mRandom, mMapRect);
            }
            
            // 移动, + 出界处理
            for (IEnemy iEnemy : mEnemyListTotal)
            {
                iEnemy.caculate(durateTime, 0, height);
                if (iEnemy.isEnd())
                {
                    mEnemyListTotal.remove(iEnemy);
                    reduceEnemyNumber(iEnemy);
                    break;
                }
            }
        }
    }
    
    /**
     * 出界就减少个数
     * @param iEnemy
     */
    private void reduceEnemyNumber(IEnemy iEnemy)
    {
        if (iEnemy instanceof Enemy1)
        {
            mEnemyNumber1 -= 1;
        }
        else if (iEnemy instanceof Enemy2)
        {
            mEnemyNumber2 -= 1;
        }
        else if (iEnemy instanceof Enemy3)
        {
            mEnemyNumber3 -= 1;
        }
        LogFileUtil.v(MainApplication.TAG,
            "number1 = " + mEnemyNumber1 + ",number2 = " + mEnemyNumber2 + ",number3 = " + mEnemyNumber3);
    }
    
    public void drawEnemies(Canvas canvas, Paint paint)
    {
        for (IEnemy iEnemy : mEnemyListTotal)
        {
            iEnemy.draw(canvas, paint);
        }
    }
    
    /**
     * 大炸弹 的操作 
     * @return
     */
    public int handleBigBombing()
    {
        int score = 0;
        for (IEnemy iEnemy : mEnemyListTotal)
        {
            score += iEnemy.hitted(Integer.MAX_VALUE);
        }
        return score;
    }
    
    /**
     * hero撞击后 操作
     */
    public int handleHeroAttack(IEnemy iEnemy)
    {
        iEnemy.blowUp();
        return iEnemy.getScore();
    }
    
    /**
     * 子弹撞击后操作
     */
    public int handleBulletAttack(IEnemy iEnemy, int atk)
    {
        return iEnemy.hitted(atk);
    }
    
    public List<IEnemy> getEnemyList()
    {
        return this.mEnemyListTotal;
    }
}
