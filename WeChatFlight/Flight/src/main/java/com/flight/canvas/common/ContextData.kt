package com.flight.canvas.common

import android.content.Context
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.RectF
import java.util.*

/**
 * 上下文 信息
 *
 * @author yline 5/10/21 -- 10:58 AM
 */
class ContextData(context: Context) {
    /* -------------------- 初始化就确定的信息 ------------------- */
    val paint: Paint = Paint()  // 默认的
    val random: Random = Random()   // 默认的
    val resources: Resources = context.resources

    var mapWidth: Int = 0   // 背景图片 宽度
    var mapHeight: Int = 0  // 背景图片 高度

    var bigBombRect = RectF()   // 炸弹 可点击区域
    var pauseRect = RectF()     // 暂停 可点击区域

    /* -------------------- 单次 计算的数据 ------------------- */
    var spaceTime: Float = 0.0f // 单位 s
    var spaceHeight: Float = 0.0f    // 单位 px

    /* --------------------------------------- */
    var isPause: Boolean = false

    var isHeroDestroy = false   // 英雄 死亡

    var supply1Num: Int = 0 // 装备1 的数量【炸弹】
    var supply2Num: Int = 0 // 装备2 的数量【双子弹】

    var totalScore: Long = 0 // 总分

    fun unInit() {
        // todo 清空一些数据
    }
}