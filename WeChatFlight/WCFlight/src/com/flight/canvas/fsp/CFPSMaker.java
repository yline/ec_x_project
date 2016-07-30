package com.flight.canvas.fsp;
import java.text.DecimalFormat;

/**
 * 在游戏循环中计算和显示当前游戏帧速
 * @author f21
 * @date 2016-3-16
 */
public class CFPSMaker {
	
    public static final int FPS = 8;  
 
    public static final long PERIOD = (long) (1.0 / FPS * 1000000000);  // 1s

    /** 更新的频率,现在是1s */
    public static long FPS_MAX_INTERVAL = 1000000000L; // 1s
    
    /** 现在这一秒的 帧数, 即1s钟执行的次数 */
    private double nowFPS = 0.0;
    
    // 中间变量
    private long interval = 0L;
    private long time;
    private long frameCount = 0;
    
    private DecimalFormat df = new DecimalFormat("0.0"); 
    
    public void makeFPS(){
        frameCount++;	
        interval += PERIOD;
        if (interval >= FPS_MAX_INTERVAL) {	// 总数超过 1s了
            long timeNow = System.nanoTime();
            long realTime = timeNow - time; 
            nowFPS = ((double) frameCount / realTime) * FPS_MAX_INTERVAL;
            
            frameCount = 0L;
            interval = 0L;
            time = timeNow;
        }
    }
    
    public double getNowFPS() {
        return nowFPS;
    }
    
    public String getFPS() {
        return df.format(nowFPS);
    }
    
}








