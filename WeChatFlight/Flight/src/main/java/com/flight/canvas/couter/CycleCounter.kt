package com.flight.canvas.couter

class CycleCounter(private val intArray: IntArray,
                   private var selectIndex: Int = 0) : ICounter {

    private var spaceTotalTime = 0f

    override fun next(spaceTime: Float, totalTime: Float): Int? {
        if (selectIndex >= intArray.size) return null

        spaceTotalTime += spaceTime
        if (spaceTotalTime >= totalTime) {
            selectIndex += 1
            selectIndex %= intArray.size

            return intArray[selectIndex]
        } else {
            return intArray[selectIndex]
        }
    }
}