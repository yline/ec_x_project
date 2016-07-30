package com.flight.canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import f21.wechat.flight.R;

/**
 * 背景资源图片
 * @author yline
 * @date 2016-4-3
 */
public class FlightMap {
	// 偏移量
	private float mMapOffsetY;
	// 每次运行的高度 
	private float mHeightVelocity;
	// 资源文件
	private Bitmap mBitmapMap;
	// 资源高度
	private int mBitmapHeight;
	
	public FlightMap() {
		this.mMapOffsetY = 0;
		this.mHeightVelocity = 4;
	}

	public void initResource(Resources resources){
		this.mBitmapMap = BitmapFactory.decodeResource(resources, R.drawable.background_1);
		this.mBitmapHeight = mBitmapMap.getHeight();
	}

	/**
	 * 设置背景速度,每次运行的高度 
	 * @param durateTime
	 * @param fullTime	运行完一个屏幕的时间
	 */
	public void setVelocity(float durateTime, float fullTime){
		this.mHeightVelocity = durateTime * mBitmapHeight / fullTime;
	}
	
	/**
	 * 获取当前的运行速度
	 * @return
	 */
	public float getVelocity(){
		return this.mHeightVelocity;
	}

	public void caculateMap(){
		mMapOffsetY = (mMapOffsetY + mHeightVelocity) % mBitmapHeight;
	}

	public void drawMap(Canvas canvas, Paint paint){
		// 由于未变化之前,因此这里的高度为图片本身的高度而不是窗口的高度
		canvas.drawBitmap(mBitmapMap, 0, mMapOffsetY - mBitmapHeight, paint);	
		canvas.drawBitmap(mBitmapMap, 0, mMapOffsetY, paint);
	}
	
	public int getMapWidth(){
		return this.mBitmapMap.getWidth();
	}
	
	public int getMapHeight(){
		return this.mBitmapMap.getHeight();
	}
}





















