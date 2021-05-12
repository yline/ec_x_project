package com.flight.canvas.couter

class CycleCounter(private val intArray: IntArray,
                   private val index: Int = 0) : ICounter {

    private var selectIndex = index
    private var spaceTotalTime = 0f

    override fun default(): Int {
        return intArray[index]
    }

    override fun next(spaceTime: Float, totalTime: Float): Int? {
        if (selectIndex >= intArray.size) return null

        spaceTotalTime += spaceTime
        if (spaceTotalTime >= totalTime) {
            selectIndex += 1
            selectIndex %= intArray.size
            spaceTotalTime = 0f

            return intArray[selectIndex]
        } else {
            return intArray[selectIndex]
        }
    }
}