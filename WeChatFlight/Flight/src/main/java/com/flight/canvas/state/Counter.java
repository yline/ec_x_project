package com.flight.canvas.state;

import java.util.HashMap;

/**
 * 注意int 和 float 不能相同的tag.
 * 不同的计算,不要使用相同的tag,不然会有计算错误.
 * 使用案例:
 * isTrue = counter.caculate(0.15f, 2.3f);
	if (isTrue) {
		System.out.println("i = " + i + ",tag1" + isTrue);
	}

 * @author yline
 * @date 2016-4-6
 */
public class Counter {
	private final static String DEFAULT_TAG = "DEFAULT";
	
	private HashMap<String, Object> map;
	public boolean judge;	// 给外界用的

	public Counter() {
		map = new HashMap<String, Object>();
	}

	public boolean caculate(float durate, float friz){
		return caculate(DEFAULT_TAG, durate, friz);
	}

	/**
	 * 倒计时,只要一直被执行,那么每过friz/durate次就会返回一次true
	 * @param tag	标签
	 * @param durate	每次刷新间隔
	 * @param friz		通知间隔
	 * @return
	 */
	public boolean caculate(String tag, float durate, float friz){
		if (!map.containsKey(tag)) {
			map.put(tag, 0.0f);
		}else {
			if ((Float)map.get(tag) > friz) {
				map.put(tag, 0.0f);
				return true;
			}else {
				map.put(tag, (Float)map.get(tag) + durate);
			}
		}
		return false;
	}
	
	/**
	 * 倒计时,清零,默认的倒计时器
	 */
	public void clearCaculate(){
		if (map.containsKey(DEFAULT_TAG)) {
			map.put(DEFAULT_TAG, 0.0f);
		}
	}

	/**
	 * 倒计时,清零
	 * @param tag	标记
	 */
	public void clearCaculate(String tag){
		if (map.containsKey(tag)) {
			map.put(tag, 0.0f);
		}
	}
}



















