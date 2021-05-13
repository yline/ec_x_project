package com.flight.canvas

import android.graphics.*
import com.flight.canvas.common.*
import com.flight.canvas.map.MapComponent
import com.flight.canvas.enemy.EnemyComponent
import com.flight.canvas.bullet.BulletComponent
import com.flight.canvas.enemy.IEnemy
import com.flight.canvas.hero.HeroComponent
import com.flight.canvas.map.InfoComponent
import com.flight.canvas.supply.Supply1
import com.flight.canvas.supply.SupplyComponent
import kotlin.math.min

class MainController : BaseComponent() {
    private val mapComponent = MapComponent()
    private var bulletComponent = BulletComponent()
    private var heroComponent = HeroComponent()
    private var supplyComponent = SupplyComponent()
    private var enemyComponent = EnemyComponent()
    private var infoComponent = InfoComponent() // 展示 边角信息

    private val componentList: List<BaseComponent> = arrayListOf(
            mapComponent, enemyComponent, supplyComponent,
            heroComponent, bulletComponent, infoComponent
    )

    override fun onMainInit(contextData: ContextData) {
        for (component in componentList) {
            component.onMainInit(contextData)
        }
    }

    override fun onThreadMeasure(contextData: ContextData, toData: MeasureToData) {
        for (component in componentList) {
            component.onThreadMeasure(contextData, toData)
        }
    }

    fun onThreadAttackBigBomb(toData: MeasureToData, contextData: ContextData) {
        // big bomb + enemy;
        for (iEnemy in toData.enemyList) {
            val enemyState = iEnemy.getEnemyState()
            if (enemyState == IEnemy.STATE_BLOW_UP || enemyState == IEnemy.STATE_END) continue

            iEnemy.changeHP(-iEnemy.getCurrentHP())    // 扣光
            val enemyState2 = iEnemy.getEnemyState()
            if (enemyState2 == IEnemy.STATE_BLOW_UP || enemyState2 == IEnemy.STATE_END) {
                contextData.totalScore += iEnemy.getInitScore()
            }
        }
    }

    // 处理 屏幕 内 正常情况下的交互
    fun onThreadAttack(toData: MeasureToData, contextData: ContextData) {
        // hero + supply; supply消失、hero加属性
        for (iSupply in toData.supplyList) {
            if (iSupply.isAttacked) continue

            val iHero = toData.hero
            if (RectF.intersects(iSupply.getRectF(), iHero.getRectF())) {
                // 更新 炸弹状态
                if (iSupply is Supply1) {
                    contextData.supply1Num += 1
                    contextData.supply1Num = min(3, contextData.supply1Num)   // 上限为 3
                } else {
                    contextData.supply2Num += 1
                }

                iSupply.isAttacked = true
            }
        }

        // bullet + enemy; enemy自身判断、bullet消失处理
        for (iEnemy in toData.enemyList) {
            val enemyState = iEnemy.getEnemyState()
            if (enemyState == IEnemy.STATE_BLOW_UP || enemyState == IEnemy.STATE_END) continue

            for (iBullet in toData.bulletList) {
                if (iBullet.isAttacked) continue

                // 表示 击中 了
                if (RectF.intersects(iBullet.getRectF(), iEnemy.getRectF())) {
                    iEnemy.changeHP(-iBullet.getATK())
                    iBullet.isAttacked = true

                    val enemyState2 = iEnemy.getEnemyState()
                    if (enemyState2 == IEnemy.STATE_BLOW_UP || enemyState2 == IEnemy.STATE_END) {
                        contextData.totalScore += iEnemy.getInitScore()
                    }
                }
            }
        }

        // hero + enemy 遍历;
        for (iEnemy in toData.enemyList) {
            val enemyState = iEnemy.getEnemyState()
            if (enemyState == IEnemy.STATE_BLOW_UP || enemyState == IEnemy.STATE_END) continue

            // 表示 撞到了
            val iHero = toData.hero
            if (RectF.intersects(iEnemy.getRectF(), iHero.getRectF())) {
                iHero.changeHP(-iEnemy.getCurrentHP())
                iEnemy.changeHP(-iEnemy.getCurrentHP())    // 扣光

                val enemyState2 = iEnemy.getEnemyState()
                if (enemyState2 == IEnemy.STATE_BLOW_UP || enemyState2 == IEnemy.STATE_END) {
                    contextData.totalScore += iEnemy.getInitScore()
                }

                // 血量 赋值
                contextData.heroHP = iHero.getCurrentHP()
            }
        }
    }

    override fun onThreadDraw(canvas: Canvas, contextData: ContextData) {
        for (component in componentList) {
            component.onThreadDraw(canvas, contextData)
        }
    }

    fun controlHero(x: Float, y: Float) {
        heroComponent.moveTo(x, y)
    }
}