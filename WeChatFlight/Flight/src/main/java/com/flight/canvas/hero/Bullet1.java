package com.flight.canvas.hero;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.flight.canvas.state.Counter;
import com.flight.canvas.state.StateConstant.BulletState;
import com.project.wechatflight.R;

public class Bullet1 implements IBullet
{
	private final static int ATK = 1;

	public final static int bulletRes = R.drawable.bullet1;
	
	private Bitmap mBitmap;

	private int mTop, mBottom;
	
	private Rect mRect;

	private BulletState mState;
	
	private Counter mCounter;

	private float positionX, positionY;
	
	public Bullet1(Bitmap bitmap, int bgTop, int bottom)
	{
		this.mBitmap = bitmap;
		this.mTop = bgTop;
		this.mBottom = bottom;
		
		this.mState = BulletState.unStart;
		this.mCounter = new Counter();
		this.positionX = 0;
		this.positionY = 0;
	}

	@Override
	public boolean fireBullet(int x, int y, float durateTime, float frizTime)
	{
		if (BulletState.unStart == mState)
		{
			mCounter.judge = mCounter.caculate("fireBullet", durateTime, frizTime);
			if (mCounter.judge)
			{
				this.mState = BulletState.normal;
				
				this.mRect = new Rect(x - mBitmap.getWidth() / 2, y - mBitmap.getHeight() / 2, x + mBitmap.getWidth() / 2, y + mBitmap.getHeight() / 2);
				this.positionX = mRect.left;
				this.positionY = mRect.top;
				
				return true;
			}
		}
		return false;
	}

	@Override
	public void caculateHeroBullet(float dx, float dy)
	{
		if (BulletState.normal == mState)
		{
			positionX += dx;
			positionY += dy;
			this.mRect.offsetTo((int) positionX, (int) positionY);
			
			if (mRect.bottom < mTop || mRect.top > mBottom)
			{
				this.mState = BulletState.end;
				this.mBitmap = null;
			}
		}
	}

	@Override
	public void drawBullet(Canvas canvas, Paint paint)
	{
		if (BulletState.normal == mState)
		{
			canvas.drawBitmap(mBitmap, mRect.left, mRect.top, paint);
		}
	}
	
	@Override
	public boolean isEnd()
	{
		return BulletState.end == mState ? true : false;
	}
	
	@Override
	public boolean isRunning()
	{
		return BulletState.normal == mState ? true : false;
	}
	
	@Override
	public void disppear()
	{
		this.mState = BulletState.end;
	}

	@Override
	public int getATK()
	{
		return Bullet1.ATK;
	}

	@Override
	public Rect getRect()
	{
		return this.mRect;
	}
}






















