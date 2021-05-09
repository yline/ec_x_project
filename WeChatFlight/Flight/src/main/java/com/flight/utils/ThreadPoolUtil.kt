package com.flight.utils

import java.util.concurrent.Executors

object ThreadPoolUtil {
    private val singleExecutors = Executors.newSingleThreadExecutor()

    fun execute(task: Runnable) {
        singleExecutors.submit(task)
    }

    fun shutdown() {
        singleExecutors.shutdown()
    }
}