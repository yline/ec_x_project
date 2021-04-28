package com.flight.canvas.state

import java.util.*

/**
 * 可以自动消失的时候,使用
 * @author f21
 * @date 2016-3-19
 */
open class LineState : IFlightState {
    private var position = 0

    protected var list: ArrayList<Int>

    override fun hasNext(): Boolean {
        return !(position > list.size - 1 || list[position] == null)
    }

    override fun next(): Int {
        val res = list[position]
        ++position
        return res
    }

    init {
        list = ArrayList()
    }
}