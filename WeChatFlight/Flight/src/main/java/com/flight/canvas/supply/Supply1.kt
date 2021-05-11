package com.flight.canvas.supply

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.flight.canvas.common.InitData
import com.project.wechatflight.R
import java.util.*

class Supply1(resources: Resources, random: Random, initData: InitData)
    : ISupply(resources, random, initData) {

    override fun clone(resources: Resources, random: Random, initData: InitData): ISupply {
        return Supply1(resources, random, initData)
    }

    override fun getSourceId(): Int {
        return R.drawable.supply1
    }
}