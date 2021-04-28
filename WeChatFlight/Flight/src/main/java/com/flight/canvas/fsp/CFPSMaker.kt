package com.flight.canvas.fsp

import java.text.DecimalFormat

/**
 * 在游戏循环中计算和显示当前游戏帧速
 *
 * @author YLine 2016/8/12 --> 18:43
 * @version 1.0.0
 */
class CFPSMaker {
    /** 现在这一秒的 帧数, 即1s钟执行的次数  */
    var nowFPS = 0.0

    // 中间变量
    private var interval = 0L
    private var time: Long = 0
    private var frameCount: Long = 0
    private val df = DecimalFormat("0.0")
    fun makeFPS() {
        frameCount++
        interval += PERIOD
        if (interval >= FPS_MAX_INTERVAL) {    // 总数超过 1s了
            val timeNow = System.nanoTime()
            val realTime = timeNow - time
            nowFPS = frameCount.toDouble() / realTime * FPS_MAX_INTERVAL
            frameCount = 0L
            interval = 0L
            time = timeNow
        }
    }

    var fPS: String = df.format(nowFPS)

    companion object {
        const val FPS = 8
        const val PERIOD = (1.0 / FPS * 1000000000).toLong() // 1s

        /** 更新的频率,现在是1s  */
        var FPS_MAX_INTERVAL = 1000000000L // 1s
    }
}