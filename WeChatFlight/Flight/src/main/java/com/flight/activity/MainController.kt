package com.flight.activity

import android.content.Context
import android.graphics.*
import com.flight.canvas.common.*
import com.flight.canvas.map.MapComponent
import com.flight.canvas.enemy.EnemyComponent
import com.flight.canvas.bullet.BulletComponent
import com.flight.canvas.hero.HeroComponent
import com.flight.canvas.supply.Supply1
import com.flight.canvas.supply.SupplyComponent
import kotlin.math.min

class MainController : BaseComponent() {
    private val mapComponent = MapComponent()
    private var bulletComponent = BulletComponent()
    private var heroComponent = HeroComponent()
    private var supplyComponent = SupplyComponent()
    private var enemyComponent = EnemyComponent()

    private val componentList: List<BaseComponent> = arrayListOf(
            mapComponent, enemyComponent, supplyComponent,
            heroComponent, bulletComponent
    )

    override fun onMainInit(context: Context, initData: InitData) {
        for (component in componentList) {
            component.onMainInit(context, initData)
        }
    }

    override fun onThreadMeasure(fromData: MeasureFromData, toData: MeasureToData) {
        for (component in componentList) {
            component.onThreadMeasure(fromData, toData)
        }
    }

    fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {
        // hero + supply; supply消失、hero加属性
        for (iSupply in toData.supplyList) {
            if (iSupply.isAttacked) continue

            val iHero = toData.hero
            if (RectF.intersects(iSupply.getRectF(), iHero.getRectF())) {
                // 更新 炸弹状态
                if (iSupply is Supply1) {
                    attackData.supply1Num += 1
                    attackData.supply1Num = min(3, attackData.supply1Num)   // 上限为 3
                } else {
                    attackData.supply2Num += 1
                }

                iSupply.isAttacked = true
            }
        }

        // bullet + enemy; enemy自身判断、bullet消失处理
        for (iEnemy in toData.enemyList) {
            if (iEnemy.isHPEmpty()) continue

            for (iBullet in toData.bulletList) {
                if (iBullet.isAttacked) continue

                // 表示 击中 了
                if (RectF.intersects(iBullet.getRectF(), iEnemy.getRectF())) {
                    iEnemy.changeHP(-iBullet.getATK())
                    iBullet.isAttacked = true

                    if (iEnemy.isHPEmpty()) {
                        attackData.totalScore += iEnemy.getScore()
                    }
                }
            }
        }

        // hero + enemy 遍历;
        for (iEnemy in toData.enemyList) {
            if (iEnemy.isHPEmpty()) continue

            // 表示 撞到了
            val iHero = toData.hero
            if (RectF.intersects(iEnemy.getRectF(), iHero.getRectF())) {
                iEnemy.changeHP(-iHero.getHP())
                // iHero.changeHP(-iEnemy.getHP())

                if (iEnemy.isHPEmpty()) {
                    attackData.totalScore += iEnemy.getScore()
                }

                if (iHero.isHPEmpty()){
                    // todo 结束游戏
                }
            }
        }

        if (attackData.isHeroDestroy) {
            if (attackData.heroDestroyTime < System.currentTimeMillis()) {
                attackData.isHeroDestroy = false

                // 游戏结束	MainActivity 中直接操作了
                MainFlight.instance.setGameOverCallback(attackData.totalScore)
            }
            return
        }
    }

    override fun onThreadDraw(canvas: Canvas, attackData: AttackData) {
        for (component in componentList) {
            component.onThreadDraw(canvas, attackData)
        }
    }

    fun controlHero(x: Float, y: Float) {
        heroComponent.moveTo(x, y)
    }
}