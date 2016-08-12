package com.flight.canvas.supply;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public interface ISupply {
	/**
	 * 开始补给
	 * @param durateTime	每次刷新的时间
	 * @param frizTime		每次出现的间隔
	 * @return
	 */
	boolean start(float durateTime, float frizTime);
	
	/**
	 * 计算
	 * @param dx	刷新一次移动的x
	 * @param dy	刷新一次移动的y
	 */
	void caculate(float dx, float dy);
	
	/**
	 * 绘图
	 * @param canvas
	 * @param paint
	 */
	void draw(Canvas canvas, Paint paint);
	
	boolean isOut();
	
	boolean isNormal();
	
	Rect getRect();
	
	/**
	 * 这个是设置成透明,而不是消失
	 */
	void disppear();
}

















