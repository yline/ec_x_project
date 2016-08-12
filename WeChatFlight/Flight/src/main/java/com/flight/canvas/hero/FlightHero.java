package com.flight.canvas.hero;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.flight.canvas.state.Counter;
import com.flight.canvas.state.CycleState;
import com.flight.canvas.state.IFlightState;
import com.flight.canvas.state.LineState;
import com.flight.canvas.state.StateConstant.HeroState;
import com.flight.canvas.supply.ISupply;
import com.flight.canvas.supply.Supply1;
import com.flight.canvas.supply.Supply2;
import com.project.wechatflight.R;

import java.util.ArrayList;
import java.util.List;

/**
 * hero
 *
 * @author yline
 * @date 2016-4-3
 */
public class FlightHero
{
	// 引用系统资源
	private Resources mResources;

	// 系统 背景
	private Rect mMapRect;

	// hero	图片状态
	private IFlightState mFlightState;

	// hero 图片资源
	private Bitmap mBitmapHero;

	// hero 当前矩形
	private Rect mRect;

	// hero 当前位置,配合矩形一起使用
	private int positionX, positionY;

	// hero 状态
	private HeroState mState;

	// hero 大招 个数
	private int mBigBombNumber;

	private final int MaxBigBombNumber = 3;

	// hero 分数
	private long mScore;

	// bullet 资源
	private Bitmap mBitmapBullet1, mBitmapBullet2;

	// bullet 对象
	private IBullet mBullet;

	// bullet 对象集合
	private List<IBullet> mBulletList;

	// bullet 类型
	private int mBulletStyle;

	public static final int styleNormal = 0;    // 杀伤力 1

	public static final int styleDouble = 1;    // 杀伤力 2

	// 倒计时,每隔几个出现一个true
	private Counter mCounter;

	public FlightHero()
	{
		
	}

	public void initResource(Resources resources, Rect rect)
	{
		// 赋值
		this.mResources = resources;
		this.mMapRect = rect;
		
		// 辅助 资源初始化
		this.mCounter = new Counter();
		
		// hero 资源初始化
		this.mFlightState = new NormalState();
		this.mState = HeroState.normal;
		this.mBitmapHero = BitmapFactory.decodeResource(mResources, mFlightState.next());
		this.mRect = new Rect(mMapRect.right / 2 - mBitmapHero.getWidth() / 2, mMapRect.bottom - mBitmapHero.getHeight(), mMapRect.right / 2 + mBitmapHero.getWidth() / 2, mMapRect.bottom);    // 如果输入满屏的话,就居中底部(近乎)显示
		this.positionX = mRect.left;
		this.positionY = mRect.top;
		
		// bullet 资源初始化
		this.mBitmapBullet1 = BitmapFactory.decodeResource(resources, Bullet1.bulletRes);
		this.mBitmapBullet2 = BitmapFactory.decodeResource(resources, Bullet2.bulletRes);
		this.mBullet = new Bullet1(mBitmapBullet1, mMapRect.top, mMapRect.bottom);
		this.mBulletList = new ArrayList<IBullet>();
		this.mBulletStyle = FlightHero.styleNormal;
	}

	/**
	 * 负责子弹移动、飞机移动、子弹产生
	 *
	 * @param durateTime 刷新频率
	 * @param height     子弹速度
	 */
	public void caculateFlightHero(float durateTime, float height)
	{
		if (HeroState.normal == mState)
		{        // 正常状态
			// hero 切换图片资源
			if (mCounter.caculate("caculate_change", durateTime, 0.15f))
			{
				this.mBitmapHero = BitmapFactory.decodeResource(mResources, mFlightState.next());
			}

			// hero 隔段时间 发送 bullet
			if (mBullet.fireBullet(mRect.centerX(), mRect.top, durateTime, 0.15f))
			{
				if (mBulletStyle == FlightHero.styleNormal)
				{
					this.mBullet = new Bullet1(mBitmapBullet1, mMapRect.top, mMapRect.bottom);
					mBulletList.add(mBullet);
				}
				else
				{    // 其它 都算 double
					this.mBullet = new Bullet2(mBitmapBullet2, mMapRect.top, mMapRect.bottom);
					mBulletList.add(mBullet);
					
					// 自动调回去
					if (mCounter.caculate("caculate_autoback", 1, 30))
					{
						this.mBulletStyle = FlightHero.styleNormal;
					}
				}
			}
		}

		// 爆炸 阶段
		if (HeroState.bombing == mState)
		{
			if (mCounter.caculate("caculate_bombing", durateTime, 0.1f))
			{
				if (mFlightState.hasNext())
				{
					mBitmapHero = BitmapFactory.decodeResource(mResources, mFlightState.next());
				}
				else
				{
					this.mState = HeroState.end;
				}
			}
		}
		
		for (IBullet iBullet : mBulletList)
		{
			iBullet.caculateHeroBullet(0, height);
			if (iBullet.isEnd())
			{
				mBulletList.remove(iBullet);
				break;    // 跳出整个循环,这是因为在该循环的时候,iterator的原因导致的错误
			}
		}
	}

	public void drawHero(Canvas canvas, Paint paint)
	{
		if (HeroState.normal == mState || HeroState.bombing == mState)
		{
			canvas.drawBitmap(mBitmapHero, mRect.left, mRect.top, paint);
		}

		// 子弹	遍历 + drawBullet
		for (IBullet bullet : mBulletList)
		{
			bullet.drawBullet(canvas, paint);
		}
	}

	/**
	 * 移动,控制是在中心位置
	 *
	 * @param x 手所在的x轴位置
	 * @param y 手所在的Y轴位置
	 */
	public void moveTo(float x, float y)
	{
		if (isInBridge(x, y) && mState == HeroState.normal)
		{
			positionX = (int) (x - mBitmapHero.getWidth() / 2);
			positionY = (int) (y - mBitmapHero.getHeight() / 2);
		}
		mRect.offsetTo(positionX - 1, positionY - 1);
	}

	private boolean isInBridge(float x, float y)
	{
		return x > mMapRect.left + mBitmapHero.getWidth() / 2 && x < mMapRect.right - mBitmapHero.getWidth() / 2 && y > mMapRect.top + mBitmapHero.getHeight() / 2 && y < mMapRect.bottom - mBitmapHero.getHeight() / 2;
	}

	/**
	 * hero加属性,bullet
	 */
	public void handleSupplyAttack(ISupply iSupply)
	{
		if (iSupply instanceof Supply1)
		{    // big炸弹
			if (mBigBombNumber < MaxBigBombNumber)
			{
				mBigBombNumber += 1;
			}
		}
		else if (iSupply instanceof Supply2)
		{    // 多发子弹
			mBulletStyle = FlightHero.styleDouble;
			mCounter.clearCaculate("caculate_autoback");
		}
	}

	/**
	 * hero转变为爆炸状态
	 */
	public void handleEnemyAttack()
	{
		this.mState = HeroState.bombing;
		this.mFlightState = new BombingState();
	}

	/**
	 * 撞击到的时候的处理
	 *
	 * @param iBullet 有交集时的子弹对象
	 * @return 伤害值
	 */
	public int handleBulletAttack(IBullet iBullet)
	{
		if (iBullet.isRunning())
		{
			iBullet.disppear();
			return iBullet.getATK();
		}
		return 0;
	}

	public void addScore(int score)
	{
		this.mScore += score;
	}

	public long getScore()
	{
		return this.mScore;
	}

	public Rect getHeroRect()
	{
		return this.mRect;
	}

	public boolean isNormal()
	{
		return this.mState == HeroState.normal ? true : false;
	}
	
	public boolean isEnd()
	{
		return this.mState == HeroState.end ? true : false;
	}

	public List<IBullet> getBulletList()
	{
		return this.mBulletList;
	}

	public int getBigBombNumber()
	{
		return this.mBigBombNumber;
	}
	
	public void reduceBigBombNumber()
	{
		this.mBigBombNumber -= 1;
	}

	class NormalState extends CycleState
	{

		public NormalState()
		{
			list.add(R.drawable.hero_fly_1);
			list.add(R.drawable.hero_fly_2);
		}
	}

	class BombingState extends LineState
	{

		public BombingState()
		{
			list.add(R.drawable.hero_blowup_1);
			list.add(R.drawable.hero_blowup_2);
			list.add(R.drawable.hero_blowup_3);
			list.add(R.drawable.hero_blowup_4);
		}
	}
}























