package com.mygdx.game

import kotlin.math.pow
import kotlin.math.roundToInt

class GameState(
    var factoryMaxHp: Int = 1000,
    var factoryHp: Int = factoryMaxHp,
    var doorIsOpen: Boolean = true,

    var minionTankFactoryLevel: Int = 1,
    var minionTankCountInside: Float = 0f,
    var minionTankCountOutside: Float = 0f,

    var minionArcherFactoryLevel: Int = 1,
    var minionArcherCountInside: Float = 0f,
    var minionArcherCountOutside: Float = 0f,

    var minionMinerFactoryLevel: Int = 1,
    var minionMinerCountInside: Float = 0f,
    var minionMinerCountOutside: Float = 0f,

    var resourceInventory: ResourcePackage = ResourcePackage(
        triangles = 100,
        circles = 0,
        squares = 0,
    ),

    var bossLevel: Int = 1,
    var bossHp: Int = 1000,

) {
    var lastFightUpdate=0f
    var fight:Boss_Fight= Boss_Fight(this)
    val round_length=1
    fun calculateFrame(delta: Float) {
        calculateFactoryFrame(delta)
        calculateCombatFrame(delta)
    }

    private fun calculateFactoryFrame(delta: Float) {
        // TODO
    }

    private fun calculateCombatFrame(delta: Float) {
        if (delta>lastFightUpdate+round_length){
            lastFightUpdate=delta
            fight.round()

        }
    }

    fun onGGClicked() {
        // TODO
    }

    fun onRepairClicked() {
        // TODO
    }

    fun onUpgradeTankFactoryClicked() {
        // TODO
    }

    fun onUpgradeArcherFactoryClicked() {
        // TODO
    }

    fun onToggleDoorClicked() {
        // TODO
    }

    fun getUpgradeCost(minionType: MinionType): ResourcePackage {
        val level = when (minionType) {
            MinionType.Tank -> minionTankFactoryLevel
            MinionType.Archer -> minionArcherFactoryLevel
            MinionType.Miner -> minionMinerFactoryLevel
        }
        return ResourcePackage(
            triangles = (10 * 1.3.pow(level)).roundToInt(),
            circles = 0,
            squares = 0,
        )
    }

    fun getRepairCost(): ResourcePackage {
        return ResourcePackage(
            triangles = 5 * (factoryMaxHp - factoryHp),
            circles = 0,
            squares = 0,
        )
    }
}
