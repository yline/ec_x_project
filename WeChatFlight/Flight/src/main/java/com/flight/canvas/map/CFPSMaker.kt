package com.flight.canvas.map

import com.flight.canvas.common.ContextData
import java.text.DecimalFormat

class CFPSMaker {
    companion object {
        private val DF = DecimalFormat("0.0")
    }

    private var speedTime = 0.001f
    private var speedCount = 0

    fun measure(contextData: ContextData) {
        if (speedTime < 1) {
            speedTime += contextData.spaceTime
            speedCount += 1
        } else {
            val fpsValue = speedCount / speedTime
            contextData.showFPS = "FPS: ${DF.format(fpsValue)}"

            speedCount = 0
            speedTime = 0.001f
        }
    }
}