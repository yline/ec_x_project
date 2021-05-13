package com.flight.canvas.enemy

import android.content.res.Resources
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData
import com.project.wechatflight.R

/**
 * 注意状态改变之后,哪些发生了改变(当前状态\图片集\当前图片)
 *
 * @author yline
 * @date 2016-4-13
 */
class Enemy3(contextData: ContextData) : IEnemy(contextData) {

    override fun getSourceArray(state: Int): IntArray? {
        return when (getEnemyState()) {
            STATE_NORMAL -> intArrayOf(R.drawable.enemy3_fly_1)
            STATE_HIT -> intArrayOf(R.drawable.enemy3_hit_1)
            STATE_BLOW_UP -> intArrayOf(R.drawable.enemy3_blowup_1, R.drawable.enemy3_blowup_2,
                    R.drawable.enemy3_blowup_3, R.drawable.enemy3_blowup_4)
            else -> null
        }
    }

    override fun getSourceRect(resources: Resources): RectF {
        val bitmap = BitmapManager.newBitmap(resources, R.drawable.enemy3_fly_1)
        return RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
    }

    override fun getEnemyState(): Int {
        if (isOuter()) return STATE_END

        if (mHP > 8) return STATE_NORMAL

        if (mHP > 0) return STATE_HIT

        if (mIsBlowUp) return STATE_BLOW_UP

        return STATE_END
    }

    override fun clone(contextData: ContextData): IEnemy {
        return Enemy3(contextData)
    }

    override fun getInitScore(): Int {
        return 5
    }

    override fun getInitHP(): Int {
        return 10
    }
}