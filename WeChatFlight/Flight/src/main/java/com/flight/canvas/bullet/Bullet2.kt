package com.flight.canvas.bullet

import com.flight.canvas.common.ContextData
import com.project.wechatflight.R

class Bullet2(contextData: ContextData) : IBullet(contextData) {

    override fun clone(contextData: ContextData): IBullet {
        return Bullet2(contextData)
    }

    override fun getSourceId(): Int {
        return R.drawable.bullet2
    }

    override fun getATK(): Int {
        return 2
    }
}