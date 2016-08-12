package com.flight.activity;

public class MainFlight {
	
	public static MainFlight getInstance(){
		return MainFlightHolder.sInstance;
	}
	
	// 静态内部类
	private static class MainFlightHolder{
		private static MainFlight sInstance = new MainFlight(); 
	}
	
	private MainFlight(){
		
	}
	
	private OnGameOverCallback gameOverCallback;
	public void setOnGameOverCallback(OnGameOverCallback callback){
		this.gameOverCallback = callback;
	}
	public void setGameOverCallback(long score){
		if (null != gameOverCallback) {
			gameOverCallback.result(score);
		}
	}
	public interface OnGameOverCallback{
		void result(long score);
	}
}
