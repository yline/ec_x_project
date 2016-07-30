package com.flight.canvas.state;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以自动消失的时候,使用
 * @author f21
 * @date 2016-3-19
 */
public class LineState implements IFlightState{
	private int position;
	protected List<Integer> list;
	
	public LineState() {
		this.position = 0;
		this.list = new ArrayList<Integer>();
	}
	
	@Override
	public boolean hasNext(){
		return !(position > list.size() - 1 || list.get(position) == null);
	}

	@Override
	public int next() {
		int res = list.get(position);
		++position;
		return res;
	}
}













