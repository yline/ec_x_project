package com.flight.canvas.supply

import com.flight.canvas.common.ContextData
import com.project.wechatflight.R

class Supply1(contextData: ContextData) : ISupply(contextData) {

    override fun clone(contextData: ContextData): ISupply {
        return Supply1(contextData)
    }

    override fun getSourceId(): Int {
        return R.drawable.supply1
    }
}