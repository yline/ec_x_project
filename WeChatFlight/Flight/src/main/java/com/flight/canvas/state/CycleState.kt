package com.flight.canvas.state

import java.util.*

/**
 * 不玩自动消失的时候使用
 * @author f21
 * @date 2016-3-19
 */
open class CycleState() : IFlightState {
    private var position = 0

    var list: ArrayList<Int> = ArrayList()


    override fun hasNext(): Boolean {
        return true
    }

    override fun next(): Int {
        val res = list[position]
        position = (position + 1) % list.size
        return res
    }

}