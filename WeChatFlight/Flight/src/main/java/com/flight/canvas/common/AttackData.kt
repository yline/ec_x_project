package com.flight.canvas.common

/**
 * 交互 之后的数据
 * @author yline 5/10/21 -- 10:58 AM
 */
class AttackData {
    companion object {
        const val GAME_OVER_DELAY_TIME = 2_000
    }

    var isPause: Boolean = false

    var isHeroDestroy = false   // 英雄 死亡
    var heroDestroyTime = 0     // 英雄 死亡 停止 时间

    var supply1Num: Int = 0 // 装备1 的数量【炸弹】
    var supply2Num: Int = 0 // 装备2 的数量【双子弹】

    var totalScore: Long = 0 // 总分
}