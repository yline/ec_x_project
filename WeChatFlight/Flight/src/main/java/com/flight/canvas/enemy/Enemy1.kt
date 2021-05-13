package com.flight.canvas.enemy

import android.content.res.Resources
import android.graphics.RectF
import com.flight.canvas.BitmapManager
import com.flight.canvas.common.ContextData
import com.flight.canvas.couter.CycleCounter
import com.flight.canvas.couter.ICounter
import com.flight.canvas.couter.LinearCounter
import com.project.wechatflight.R

class Enemy1(contextData: ContextData) : IEnemy(contextData) {

    override fun getSourceArray(state: Int): IntArray? {
        return when (getEnemyState()) {
            STATE_NORMAL -> intArrayOf(R.drawable.enemy1_fly_1)
            STATE_BLOW_UP -> intArrayOf(R.drawable.enemy1_blowup_1, R.drawable.enemy1_blowup_2,
                    R.drawable.enemy1_blowup_3, R.drawable.enemy1_blowup_4)
            else -> null
        }
    }

    override fun getSourceRect(resources: Resources): RectF {
        val bitmap = BitmapManager.newBitmap(resources, R.drawable.enemy1_fly_1)
        return RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
    }

    override fun getEnemyState(): Int {
        if (isOuter()) return STATE_END

        if (mHP > 0) return STATE_NORMAL

        if (mIsBlowUp) return STATE_BLOW_UP

        return STATE_END
    }

    override fun clone(contextData: ContextData): IEnemy {
        return Enemy1(contextData)
    }

    override fun getScore(): Int {
        return 2
    }

    override fun getHP(): Int {
        return 4
    }

}