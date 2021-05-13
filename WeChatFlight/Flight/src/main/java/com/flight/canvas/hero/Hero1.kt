package com.flight.canvas.hero

import com.flight.canvas.common.ContextData
import com.flight.canvas.couter.CycleCounter
import com.flight.canvas.couter.ICounter
import com.flight.canvas.couter.LinearCounter
import com.project.wechatflight.R

class Hero1(contextData: ContextData) : IHero(contextData) {
    private val cycleCounter = CycleCounter(intArrayOf(R.drawable.hero_fly_1, R.drawable.hero_fly_2))
    private val linearCounter = LinearCounter(intArrayOf(R.drawable.hero_blowup_1, R.drawable.hero_blowup_2, R.drawable.hero_blowup_3, R.drawable.hero_blowup_4))

    override fun getSourceArray(state: Int): ICounter {
        if (state == STATE_NORMAL) {
            return cycleCounter
        } else {
            return linearCounter
        }
    }

    override fun getInitHP(): Int {
        return DEFAULT_HP
    }

}