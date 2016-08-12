package com.flight.canvas.supply;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SupplyController {
	private Random mRandom;
	private boolean isEnd = false;	// hero 是否 死了
	private Bitmap mBitmapSupply1;
	private Bitmap mBitmapSupply2;
	private Rect mMapRect;
	private ISupply mSupply1;
	private ISupply mSupply2;
	
	private List<ISupply> mSupplyList;
	
	public SupplyController() {
		this.isSupply1Start = false;
		this.isSupply2Start = false;
		this.mRandom = new Random();
	}
	
	public void initResource(Resources resources, Rect rect) {
		this.mMapRect = rect;
		mBitmapSupply1 = BitmapFactory.decodeResource(resources, Supply1.SupplyRes);
		mBitmapSupply2 = BitmapFactory.decodeResource(resources, Supply2.SupplyRes);
		
		this.mSupply1 = new Supply1(mBitmapSupply1, mRandom, mMapRect);
		this.mSupply2 = new Supply2(mBitmapSupply2, mRandom, mMapRect);
		this.mSupplyList = new ArrayList<ISupply>();
	}
	
	private boolean isSupply1Start;
	private boolean isSupply2Start;
	/**
	 * 负责 补给移动、补给产生
	 */
	public void caculateSupply(float durateTime, float height, float frizTime1, float frizTime2) {
		if (!isEnd) {
			// 补给	产生时间间隔
			isSupply1Start = mSupply1.start(durateTime, frizTime1);
			if (isSupply1Start) {
				mSupplyList.add(mSupply1);
				mSupply1 = new Supply1(mBitmapSupply1, mRandom, mMapRect);
			}
			
			isSupply2Start = mSupply2.start(durateTime, frizTime2);
			if (isSupply2Start) {
				mSupplyList.add(mSupply2);
				mSupply2 = new Supply2(mBitmapSupply2, mRandom, mMapRect);
			}
			
			for (ISupply iSupply : mSupplyList) {
				iSupply.caculate(0, height);
				if (iSupply.isOut()) {
					mSupplyList.remove(iSupply);
					break;	// 跳出整个循环,这是因为在该循环的时候,iterator的错误
				}
			}
		}
	}
	
	public void drawSupplies(Canvas canvas, Paint paint) {
		for (ISupply iSupply : mSupplyList) {
			iSupply.draw(canvas, paint);
		}
	}
	
	/**
	 * 负责 补给撞击后操作
	 */
	public void handleHeroAttack(ISupply iSupply) {
		iSupply.disppear();
	}
	
	public List<ISupply> getSupplyList(){
		return this.mSupplyList;
	}
}



















