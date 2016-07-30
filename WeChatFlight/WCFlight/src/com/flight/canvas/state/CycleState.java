package com.flight.canvas.state;

import java.util.ArrayList;
import java.util.List;

/**
 * 不玩自动消失的时候使用
 * @author f21
 * @date 2016-3-19
 */
public class CycleState implements IFlightState{
	private int position;
	protected List<Integer> list;
	
	public CycleState() {
		this.position = 0;
		this.list = new ArrayList<Integer>();
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public int next() {
		int res = list.get(position);
		position = (position + 1) % list.size();
		return res;
	}
}


















