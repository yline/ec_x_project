package com.flight.canvas.supply;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.project.wechatflight.R;

import java.util.Random;

public class Supply2 implements ISupply
{
	public final static int SupplyRes = R.drawable.supply2;

	/**
	 * 0	未开始状态
	 * 1	正常状态
	 * 2	透明状态
	 * 3	出界,结束
	 */
	private int mState;

	private float mTime;

	private Rect mRect;

	private int mLeft;

	private float positionX, positionY;

	private Bitmap mBitmap;

	private Rect mMapRect;
	
	public Supply2(Bitmap bitmap, Random random, Rect rect)
	{
		this.mBitmap = bitmap;
		this.mMapRect = rect;
		this.mLeft = random.nextInt(mMapRect.right - mBitmap.getWidth());
		
		this.mState = 0;
		this.mTime = 0;
	}

	@Override
	public boolean start(float durateTime, float frizTime)
	{
		if (0 == mState)
		{
			if (mTime > frizTime)
			{
				this.mTime = 0;
				this.mState = 1;
				this.mRect = new Rect(mLeft, mMapRect.top - mBitmap.getHeight(), mLeft + mBitmap.getWidth(), mMapRect.top);
				positionX = mLeft;
				positionY = mMapRect.top - mBitmap.getHeight();
				return true;
			}
			else
			{
				mTime += durateTime;
			}
		}
		return false;
	}

	@Override
	public void caculate(float dx, float dy)
	{
		if (1 == mState || 2 == mState)
		{
			this.positionX += dx;
			this.positionY += dy;
			this.mRect.offsetTo((int) positionX, (int) positionY);
			
			if (mRect.bottom < mMapRect.top || mRect.top > mMapRect.bottom)
			{
				this.mState = 3;
			}
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint)
	{
		if (1 == mState)
		{
			canvas.drawBitmap(mBitmap, mRect.left, mRect.top, paint);
		}
	}

	@Override
	public boolean isOut()
	{
		return 3 == this.mState ? true : false;
	}

	@Override
	public boolean isNormal()
	{
		return 1 == this.mState ? true : false;
	}

	@Override
	public void disppear()
	{
		this.mState = 2;
	}

	@Override
	public Rect getRect()
	{
		return this.mRect;
	}
	
}
