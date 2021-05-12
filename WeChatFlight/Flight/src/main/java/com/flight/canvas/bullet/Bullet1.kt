package com.flight.canvas.bullet

import com.flight.canvas.common.ContextData
import com.project.wechatflight.R

class Bullet1(contextData: ContextData) : IBullet(contextData) {

    override fun clone(contextData: ContextData): IBullet {
        return Bullet1(contextData)
    }

    override fun getSourceId(): Int {
        return R.drawable.bullet1
    }

    override fun getATK(): Int {
        return 1
    }
}