package com.flight.activity

import android.content.Context
import android.graphics.*
import com.flight.canvas.common.*
import com.flight.canvas.map.MapComponent
import com.flight.canvas.enemy.EnemyComponent
import com.flight.canvas.bullet.BulletComponent
import com.flight.canvas.hero.HeroComponent
import com.flight.canvas.supply.SupplyComponent

class MainController : BaseComponent() {
    private val mapComponent = MapComponent()
    private var bulletComponent = BulletComponent()
    private var heroComponent = HeroComponent()
    private var supplyComponent = SupplyComponent()
    private var enemyComponent = EnemyComponent()

    private val componentList: List<BaseComponent> = arrayListOf(
            mapComponent, bulletComponent,
            heroComponent, enemyComponent, supplyComponent
    )

    private var isGameOver = false
    private var gameOverDelay = 2.0f // 延迟 1s 暂停

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

    override fun onThreadAttack(toData: MeasureToData, attackData: AttackData) {
        // 撞击操作,这是公共的部分,就公共的操作
        // 爆炸状态不算;only 正常状态
        // 1,hero + supply遍历,supply消失、hero加属性
//        for (iSupply in supplyComponent.supplyList) {
//            if (iSupply.isNormal && heroComponent.isNormal) { // 正常状态
//                if (Rect.intersects(heroComponent.heroRect, iSupply.rect!!)) {
//                    heroComponent.handleSupplyAttack(iSupply)
//                    supplyComponent.handleHeroAttack(iSupply)
//                }
//            }
//        }
//        for (iEnemy in enemyComponent.enemyList) {
//            // 2,hero + enemy遍历,enemy状态处于爆炸状态、hero处于爆炸状态
//            if (heroComponent.isNormal && iEnemy.isRunning) { // 正常状态
//                if (Rect.intersects(heroComponent.heroRect, iEnemy.rect!!)) {
//                    heroComponent.handleEnemyAttack() // 拿分
//                    heroComponent.addScore(enemyComponent.handleHeroAttack(iEnemy))
//                    isGameOver = true
//                }
//            }
//
//            // 3,子弹 + enemy遍历,enemy自身判断、bullet消失处理
//            for (iBullet in heroComponent.bulletList) {
//                if (iEnemy.isRunning && iBullet.isRunning) {
//                    if (Rect.intersects(iBullet.rect!!, iEnemy.rect!!)) {
//                        heroComponent.handleBulletAttack(iBullet) // 拿分
//                        heroComponent.addScore(enemyComponent.handleBulletAttack(iEnemy, iBullet.atk))
//                    }
//                }
//            }
//        }

        if (attackData.isHeroDestroy) {
            if (attackData.heroDestroyTime < System.currentTimeMillis()) {
                attackData.isHeroDestroy = false

                // 游戏结束	MainActivity 中直接操作了
                MainFlight.instance.setGameOverCallback(attackData.totalScore)
            }
            return
        }

        for (component in componentList) {
            component.onThreadAttack(toData, attackData)
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