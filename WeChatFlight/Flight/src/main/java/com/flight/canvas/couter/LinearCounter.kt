package com.flight.canvas.couter

class LinearCounter(private val intArray: IntArray,
                    private var selectIndex: Int = 0) : ICounter {

    private var spaceTotalTime = 0f

    override fun next(spaceTime: Float, totalTime: Float): Int? {
        if (selectIndex >= intArray.size) return null

        spaceTotalTime += spaceTime
        if (spaceTotalTime >= totalTime) {
            selectIndex += 1

            if (selectIndex >= intArray.size) {
                return null
            } else {
                return intArray[selectIndex]
            }
        } else {
            return intArray[selectIndex]
        }
    }
}