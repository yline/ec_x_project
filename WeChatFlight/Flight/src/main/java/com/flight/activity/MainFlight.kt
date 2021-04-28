package com.flight.activity

class MainFlight private constructor() {
    // 静态内部类
    private object MainFlightHolder {
        val sInstance = MainFlight()
    }

    private var gameOverCallback: OnGameOverCallback? = null
    fun setOnGameOverCallback(callback: OnGameOverCallback?) {
        gameOverCallback = callback
    }

    fun setGameOverCallback(score: Long) {
        if (null != gameOverCallback) {
            gameOverCallback!!.result(score)
        }
    }

    interface OnGameOverCallback {
        fun result(score: Long)
    }

    companion object {
        val instance: MainFlight
            get() = MainFlightHolder.sInstance
    }
}