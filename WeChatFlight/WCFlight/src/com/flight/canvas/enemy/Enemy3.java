package com.flight.canvas.enemy;

import java.util.Random;

import com.flight.canvas.state.Counter;
import com.flight.canvas.state.CycleState;
import com.flight.canvas.state.IFlightState;
import com.flight.canvas.state.LineState;
import com.flight.canvas.state.StateConstant.EnemyState;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import f21.wechat.flight.R;

/**
 * 注意状态改变之后,哪些发生了改变(当前状态\图片集\当前图片)
 * @author yline
 * @date 2016-4-13
 */
public class Enemy3 implements IEnemy{
	// 常量
	private static final int MAXHP = 10;
	private static final int SCORE = 5;

	// 传入参数
	private Resources mResources;
	private Rect mMapRect;
	private Random mRandom;

	// 中间变量
	private int mHp;
	private Bitmap mBitmap;
	private Rect mRect;

	private IFlightState mFlightState;
	private EnemyState mState;
	private Counter mCounter;

	private float positionX, positionY;

	public Enemy3(Resources resources, Random random, Rect rect) {
		this.mResources = resources;
		this.mRandom = random;
		this.mMapRect = rect;

		this.mState = EnemyState.unStart;
		this.mCounter = new Counter();
	}

	@Override
	public boolean start(float durateTime, float frizTime) {
		if (EnemyState.unStart == mState) {
			mCounter.judge = mCounter.caculate("start", durateTime, frizTime);
			if (mCounter.judge) {
				this.mState = EnemyState.normal;
				this.mFlightState = new NormalState();

				this.mHp = Enemy3.MAXHP;
				this.mBitmap = BitmapFactory.decodeResource(mResources, mFlightState.next());
				int left = mRandom.nextInt(mMapRect.right - mBitmap.getWidth());
				this.mRect = new Rect(left, mMapRect.top - mBitmap.getHeight(),
						left + mBitmap.getWidth(), mMapRect.top);

				positionX = mRect.left;
				positionY = mRect.top;
				return true;
			}
		}
		return false;
	}

	@Override
	public void caculate(float durateTime, float dx, float dy) {
		if (EnemyState.normal == mState) {	// 正常时,位置、图片计算
			this.positionX += dx;
			this.positionY += dy;
			this.mRect.offsetTo((int)positionX, (int)positionY);
		}

		if (EnemyState.hitting == mState) {	// 击中时,位置、图片计算
			this.positionX += dx;
			this.positionY += dy;
			this.mRect.offsetTo((int)positionX, (int)positionY);
			
			// 切换图片
			mCounter.judge = mCounter.caculate("caculate_hitting", durateTime, 0.1f);
			if (mCounter.judge) {
				if (mFlightState.hasNext()) {
					mBitmap = BitmapFactory.decodeResource(mResources, mFlightState.next());
				}else {
					this.mState = EnemyState.normal;
					this.mFlightState = new NormalState();
					this.mBitmap = BitmapFactory.decodeResource(mResources, mFlightState.next());
				}
			}
		}

		if (EnemyState.bombing == mState) {	// 爆炸时,位置、图片计算
			mCounter.judge = mCounter.caculate("caculate_bombing", durateTime, 0.1f);
			if (mCounter.judge) {
				if (mFlightState.hasNext()) {
					mBitmap = BitmapFactory.decodeResource(mResources, mFlightState.next());
				}else {
					this.mState = EnemyState.end;	// 爆炸结束,进入透明状态
					this.mFlightState = null;
				}
			}
		}
		
		// 判断是否出界
		if (mRect.bottom < mMapRect.top || mRect.top > mMapRect.bottom) {
			this.mState = EnemyState.end;
			this.mFlightState = null;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		if (EnemyState.normal == mState || EnemyState.hitting == mState || EnemyState.bombing == mState) {
			canvas.drawBitmap(mBitmap, mRect.left, mRect.top, paint);
		}
	}

	@Override
	public int hitted(int atk) {
		mHp -= atk;
		if (mHp <= 0) {
			this.blowUp();
			return getScore();
		}else {
			this.mState = EnemyState.hitting;
			this.mFlightState = new HitState();
			this.mBitmap = BitmapFactory.decodeResource(mResources, mFlightState.next());
		}
		return 0;
	}

	@Override
	public void blowUp() {
		this.mState = EnemyState.bombing;
		this.mFlightState = new BombingState();
		this.mBitmap = BitmapFactory.decodeResource(mResources, mFlightState.next());
	}

	@Override
	public boolean isEnd() {
		return mState == EnemyState.end? true : false;
	}

	@Override
	public boolean isRunning() {
		return EnemyState.normal == mState || EnemyState.hitting == mState ? true : false;
	}

	@Override
	public Rect getRect() {
		return this.mRect;
	}

	@Override
	public int getScore() {
		return Enemy3.SCORE;
	}

	class NormalState extends CycleState{

		public NormalState() {
			list.add(R.drawable.enemy3_fly_1);
		}
	}

	class HitState extends LineState{

		public HitState() {
			list.add(R.drawable.enemy3_hit_1);
		}
	}

	class BombingState extends LineState{

		public BombingState() {
			list.add(R.drawable.enemy3_blowup_1);
			list.add(R.drawable.enemy3_blowup_2);
			list.add(R.drawable.enemy3_blowup_3);
			list.add(R.drawable.enemy3_blowup_4);
		}
	}
}
