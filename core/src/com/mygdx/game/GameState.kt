package com.mygdx.game

import kotlin.math.pow
import kotlin.math.roundToInt

class GameState(
    var factoryHp: Hp = Hp(total = 1000),
    var doorIsOpen: Boolean = true,

    var tankMinionData: MinionProduction = MinionProduction(MinionType.Tank,2,4),
    var archerMinionData: MinionProduction = MinionProduction(MinionType.Archer,4,2),
    var minerMinionData: MinionProduction = MinionProduction(MinionType.Miner,0,1),

    var resourceInventory: ResourcePackage = ResourcePackage(triangles = 100),

    var bossLevel: Int = 1,
    var bossHp: Hp = Hp(total = 1000),
) {

    var lastFightUpdate=0f
    var lastMiningUpdate=0f
    var fight:Boss_Fight= Boss_Fight(this)
    val fight_round_length=1
    val mining_round_length=3


    private val factoryUpgradeCostCache = mutableMapOf<MinionType, ResourcePackage>()

    operator fun get(minionType: MinionType) = when (minionType) {
        MinionType.Tank -> tankMinionData
        MinionType.Archer -> archerMinionData
        MinionType.Miner -> minerMinionData
    }


    fun calculateFrame(delta: Float) {
        calculateFactoryFrame(delta)
        calculateCombatFrame(delta)
        calculateMiningFrame(delta)
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
        if (delta>lastFightUpdate+fight_round_length){
            lastFightUpdate=delta
            fight.round()

        }
    }

    private fun calculateMiningFrame(delta: Float) {
        if (delta>lastFightUpdate+mining_round_length&&doorIsOpen){
            lastMiningUpdate=delta
            this.resourceInventory.triangles+=minerMinionData.minionCountOutside.toInt()

        }
    }

    fun onGGClicked() {
        // TODO
    }

    fun onRepairClicked() {
        // TODO
    }

    fun canUpgradeFactory(minionType: MinionType): Boolean {
        return getUpgradeCost(minionType) in resourceInventory
    }

    fun onUpgradeFactoryClicked(minionType: MinionType) {
        if (!canUpgradeFactory(minionType)) return
        resourceInventory -= getUpgradeCost(minionType)
        this[minionType].factoryLevel++
    }

    fun onToggleDoorClicked() {
        doorIsOpen = !doorIsOpen
    }

    fun getUpgradeCost(minionType: MinionType): ResourcePackage {
        return factoryUpgradeCostCache.getOrPut(minionType) {
            val level = this[minionType].factoryLevel
            factoryUpgradeCost(minionType, level)
        }
    }

    fun getRepairCost(): ResourcePackage {
        return ResourcePackage(
            triangles = 5 * factoryHp.missing,
            circles = 0,
            squares = 0,
        )
    }
}

fun factorySpeedForLevel(level: Int): Float {
    return 0.1f * level
}

fun factoryUpgradeCost(minionType: MinionType, level: Int): ResourcePackage {
    return ResourcePackage(
        triangles = (10 * 1.3.pow(level)).roundToInt(),
        circles = 0,
        squares = 0,
    )
}
