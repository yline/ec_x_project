package com.flight.canvas.state;

public class StateConstant {
	public enum EnemyState{
		/** 
		 * 未开始状态
		 */
		unStart,
		/**
		 * 正常状态
		 */
		normal,
		/**
		 * 被打击时,未爆炸时的状态
		 */
		hitting,
		/**
		 * 爆炸状态
		 */
		bombing,
		/**
		 * 结束,出界
		 */
		end;
	}
	
	public enum BulletState{
		/**
		 * 未fire状态
		 */
		unStart,
		/**
		 * 正常状态
		 */
		normal,
		/**
		 * 出界状态
		 */
		end;
	}
	
	public enum HeroState{
		/**
		 * 正常游戏状态
		 */
		normal,
		/**
		 * 自身处于爆炸状态
		 */
		bombing,
		/**
		 * 结束,mFlightState = null
		 */
		end;
	}
}





















