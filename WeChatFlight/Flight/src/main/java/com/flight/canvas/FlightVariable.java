package com.flight.canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.flight.canvas.fsp.CFPSMaker;
import com.project.wechatflight.R;

/**
 * 就一些界面上固定位置的东西
 *
 * @author yline
 * @date 2016-4-3
 */
public class FlightVariable
{
	private int mBigBombNumber;

	private long mTotalScore;

	private int mapWidth, mapHeight;
	
	private Bitmap mBitmapBigBomb;

	private Rect mBigBombRect;

	private CFPSMaker mCfpsMaker;
	
	private Bitmap mBitmapPause;

	private Bitmap mBitmapStart;

	private Rect mPauseRect;
	
	public FlightVariable()
	{
		this.mBigBombNumber = 0;
		this.mTotalScore = 0;
	}
	
	public void initResource(Resources resources, int width, int height)
	{
		this.mapWidth = width;
		this.mapHeight = height;
		
		// 大炸弹
		this.mBitmapBigBomb = BitmapFactory.decodeResource(resources, R.drawable.bomb);
		this.mBigBombRect = new Rect(20, mapHeight - 20 - mBitmapBigBomb.getHeight(), 20 + mBitmapBigBomb.getWidth(), mapHeight - 20);
		this.mCfpsMaker = new CFPSMaker();
		
		// 暂停
		this.mBitmapPause = BitmapFactory.decodeResource(resources, R.drawable.game_pause);
		this.mBitmapStart = BitmapFactory.decodeResource(resources, R.drawable.game_start);
		this.mPauseRect = new Rect(mapWidth - 20 - mBitmapPause.getWidth(), mapHeight - 20 - mBitmapPause.getHeight(), mapWidth - 20, mapHeight - 20);
	}
	
	public void setBigBombNumber(int number)
	{
		this.mBigBombNumber = number;
	}
	
	public void setTotalScore(long score)
	{
		this.mTotalScore = score;
	}
	
	public void drawVariable(Canvas canvas, Paint scorePaint, boolean isPause)
	{
		canvas.drawText("分数:" + mTotalScore, 20, 30, scorePaint);
		
		mCfpsMaker.makeFPS();
		canvas.drawText(mCfpsMaker.getFPS() + " FPS", mapWidth - 155, 30, scorePaint);
		
		canvas.drawBitmap(mBitmapBigBomb, 20, mapHeight - 20 - mBitmapBigBomb.getHeight(), scorePaint);
		canvas.drawText("×" + mBigBombNumber, 20 + mBitmapBigBomb.getWidth(), mapHeight - 30, scorePaint);
		
		if (!isPause)
		{
			canvas.drawBitmap(mBitmapPause, mPauseRect.left, mPauseRect.top, scorePaint);
		}
		else
		{
			canvas.drawBitmap(mBitmapStart, mPauseRect.left, mPauseRect.top, scorePaint);
		}
	}
	
	public Rect getBigBombRect()
	{
		return this.mBigBombRect;
	}
	
	public Rect getPauseRect()
	{
		return this.mPauseRect;
	}
}




























