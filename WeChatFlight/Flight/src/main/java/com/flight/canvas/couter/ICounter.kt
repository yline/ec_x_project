package com.flight.canvas.couter

interface ICounter {
    fun default(): Int

    fun next(spaceTime: Float, totalTime: Float): Int?
}