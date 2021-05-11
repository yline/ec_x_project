package com.flight.canvas.common

import android.graphics.RectF
import com.flight.canvas.bullet.IBullet
import com.flight.canvas.enemy.IEnemy
import com.flight.canvas.supply.ISupply
import java.util.ArrayList

/**
 * 测量 的结果
 * @author yline 5/10/21 -- 10:57 AM
 */
class MeasureToData {
    // 战机 的 位置
    var heroRect = RectF()

    // 敌机 的 集合
    var enemyList = ArrayList<IEnemy>()

    // 子弹 的 集合
    var bulletList = ArrayList<IBullet>()

    // 供给 的 集合
    var supplyList = ArrayList<ISupply>()
}