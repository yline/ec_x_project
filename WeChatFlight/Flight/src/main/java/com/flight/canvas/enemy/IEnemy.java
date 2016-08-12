package com.flight.canvas.enemy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public interface IEnemy {
	
	boolean start(float durateTime, float frizTime);
	
	void caculate(float durateTime, float dx, float dy);
	
	/** 绘制有图片时候的状态 */
	void draw(Canvas canvas, Paint paint);
	
	/**
	 * 被撞击
	 * @param atk	杀伤力
	 * @return	获取的分数
	 */
	int hitted(int atk);
	
	/**
	 * 开始自爆状态
	 */
	void blowUp();

	/**
	 * 是否结束,就是状态为end
	 * @return
	 */
	boolean isEnd();
	
	/**
	 * 是否在运行状态,正常和被撞击(为销毁时)
	 * @return
	 */
	boolean isRunning();
	
	Rect getRect();
	
	int getScore();
}
