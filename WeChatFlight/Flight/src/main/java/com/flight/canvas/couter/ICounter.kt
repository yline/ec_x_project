package com.flight.canvas.couter

interface ICounter {
    fun next(spaceTime: Float, totalTime: Float): Int?
}