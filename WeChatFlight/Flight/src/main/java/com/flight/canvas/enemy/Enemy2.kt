package com.flight.canvas.enemy

import android.content.res.Resources
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData
import com.project.wechatflight.R

class Enemy2(contextData: ContextData) : IEnemy(contextData) {

    override fun getSourceArray(state: Int): IntArray? {
        return when (getEnemyState()) {
            STATE_NORMAL -> intArrayOf(R.drawable.enemy2_fly_1, R.drawable.enemy2_fly_2)
            STATE_HIT -> intArrayOf(R.drawable.enemy2_hit_1)
            STATE_BLOW_UP -> intArrayOf(R.drawable.enemy2_blowup_1, R.drawable.enemy2_blowup_2,
                    R.drawable.enemy2_blowup_3, R.drawable.enemy2_blowup_4,
                    R.drawable.enemy2_blowup_5, R.drawable.enemy2_blowup_6)
            else -> null
        }
    }

    override fun getSourceRect(resources: Resources): RectF {
        val bitmap = BitmapManager.newBitmap(resources, R.drawable.enemy2_fly_1)
        return RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
    }

    override fun getEnemyState(): Int {
        if (isOuter()) return STATE_END

        if (mHP > 18) return STATE_NORMAL

        if (mHP > 0) return STATE_HIT

        if (mIsBlowUp) return STATE_BLOW_UP

        return STATE_END
    }

    override fun clone(contextData: ContextData): IEnemy {
        return Enemy2(contextData)
    }

    override fun getScore(): Int {
        return 10
    }

    override fun getHP(): Int {
        return 20
    }
}