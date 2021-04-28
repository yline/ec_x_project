package com.flight.canvas.state

interface IFlightState {
    /**
     * 是否有下一个资源
     * @return
     */
    operator fun hasNext(): Boolean

    /**
     * 返回当前信息
     * 移动到下一个资源
     */
    operator fun next(): Int
}