package com.flight.canvas.hero;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public interface IBullet {
	
	/**
	 * 开火
	 * @param x	开始位置X		中心
	 * @param y	开始位置y		中心
	 */
	boolean fireBullet(int x, int y, float durateTime, float frizTime);
	
	/**
	 * 负责自身的移动速度 + 出界处理
	 * @param dx	刷新一次移动的x
	 * @param dy	刷新一次移动的高度
	 */
	void caculateHeroBullet(float dx, float dy);
	
	void drawBullet(Canvas canvas, Paint paint);
	
	boolean isEnd();
	
	/** 例如撞击前必须要 判断 normal */
	boolean isRunning();
	
	void disppear();
	
	Rect getRect();

	/**
	 * @return	伤害度
	 */
	int getATK();
}
