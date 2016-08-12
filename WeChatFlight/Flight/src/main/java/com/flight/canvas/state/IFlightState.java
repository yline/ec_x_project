package com.flight.canvas.state;

public interface IFlightState {
	
	/**
	 * 是否有下一个资源
	 * @return
	 */
	boolean hasNext();
	
	/**
	 * 返回当前信息
	 * 移动到下一个资源
	 */
	int next();
}
