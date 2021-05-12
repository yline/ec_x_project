package com.flight.canvas.hero

import com.flight.canvas.common.ContextData
import com.project.wechatflight.R

class Hero1(private val contextData: ContextData) : IHero(contextData) {

    override fun getSourceArray(state: Int): IntArray {
        if (state == STATE_NORMAL) {
            return intArrayOf(R.drawable.hero_fly_1, R.drawable.hero_fly_2)
        } else {
            return intArrayOf(R.drawable.hero_blowup_1, R.drawable.hero_blowup_2, R.drawable.hero_blowup_3, R.drawable.hero_blowup_4)
        }
    }

    override fun getHP(): Int {
        return 1
    }

}