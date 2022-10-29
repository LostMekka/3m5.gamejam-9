package com.mygdx.game

import kotlin.math.pow
import kotlin.math.roundToInt

class GameState(
    var factoryMaxHp: Int = 1000,
    var factoryHp: Int = factoryMaxHp,
    var doorIsOpen: Boolean = true,

    var tankMinionData: MinionProduction = MinionProduction(MinionType.Tank),
    var archerMinionData: MinionProduction = MinionProduction(MinionType.Archer),
    var minerMinionData: MinionProduction = MinionProduction(MinionType.Miner),

    var resourceInventory: ResourcePackage = ResourcePackage(triangles = 100),

    var bossLevel: Int = 1,
    var bossHp: Int = 1000,
) {

    var lastFightUpdate=0f
    var fight:Boss_Fight= Boss_Fight(this)
    val round_length=1


    operator fun get(minionType: MinionType) = when (minionType) {
        MinionType.Tank -> tankMinionData
        MinionType.Archer -> archerMinionData
        MinionType.Miner -> minerMinionData
    }


    fun calculateFrame(delta: Float) {
        calculateFactoryFrame(delta)
        calculateCombatFrame(delta)
    }

    private fun calculateFactoryFrame(delta: Float) {
        for (minionType in MinionType.values()) {
            this[minionType].apply {
                factoryProgress += factorySpeedForLevel(factoryLevel) * delta
                val fullProgress = factoryProgress.toInt()
                factoryProgress -= fullProgress
                minionCountInside += fullProgress
                if (doorIsOpen) {
                    minionCountOutside += minionCountInside
                    minionCountInside = 0f
                }
            }
        }
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
        val level = this[minionType].factoryLevel
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

fun factorySpeedForLevel(level: Int): Float {
    return 0.1f * level
}
