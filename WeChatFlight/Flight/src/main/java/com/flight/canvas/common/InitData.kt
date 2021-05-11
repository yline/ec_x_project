package com.flight.canvas.common

import android.graphics.RectF

class InitData {
    var mapWidth: Int = 0   // 背景图片 宽度
    var mapHeight: Int = 0  // 背景图片 高度

    var bigBombRect = RectF()   // 炸弹 可点击区域
    var pauseRect = RectF()     // 暂停 可点击区域
}