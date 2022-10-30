package com.mygdx.game

import com.mygdx.game.common.soundController
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

class ResettableGameState(
    var factoryHp: Hp = Hp(total = 1000),
    var doorIsOpen: Boolean = true,

    var tankMinionData: MinionProduction = MinionProduction(MinionType.Tank, 2f, 4f),
    var archerMinionData: MinionProduction = MinionProduction(MinionType.Archer, 4f, 2f),
    var minerMinionData: MinionProduction = MinionProduction(MinionType.Miner, 0f, 1f),

    var resourceInventory: ResourcePackage = ResourcePackage(triangles = 100),

    var bossLevel: Int = 1,
    var bossHp: Hp = Hp(total = 10),
) {
    var factoryRepairCostPerHpPoint = ResourcePackage(triangles = 1)

    var lastMiningUpdate = 0f
    var bossFightState: BossFight = BossFight(this)

    val minerRoundTripTime = 10f
    var timeBetweenIncomingMiners: Float? = null

    var basicAttack: Attack = Attack(1f, null)
    var waitAttack: Attack = Attack(0f, null)
    var bosses = mutableListOf(
        Boss(1, "Hier könnte ihre Werbung stehen", "Bööööses Monster", listOf(basicAttack)),
        Boss(1, "Hier könnte ihre Werbung stehen", "So Bööööses Monster", listOf(basicAttack,waitAttack))
    )
    var boss: Boss = bosses.first()

    private val factoryUpgradeCostCache = mutableMapOf<MinionType, ResourcePackage>()

    operator fun get(minionType: MinionType) = when (minionType) {
        MinionType.Tank -> tankMinionData
        MinionType.Archer -> archerMinionData
        MinionType.Miner -> minerMinionData
    }

    fun calculateFrame(delta: Float) {
        calculateFactoryFrame(delta)
        calculateMiningFrame(delta)
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
        bossFightState.update(delta)
    }

    var sendOutTime=0f;

    private fun calculateMiningFrame(delta: Float) {
        val minionCountOutside = minerMinionData.minionCountOutside
        if (minionCountOutside<=0) {
            sendOutTime=0f
            lastMiningUpdate=0f
        }
        if (!doorIsOpen && minionCountOutside <= 0f) return

        if (doorIsOpen) {
            timeBetweenIncomingMiners = when (minionCountOutside) {
                0f -> null
                else -> minerRoundTripTime / minionCountOutside
            }
        }

        sendOutTime+=delta
        lastMiningUpdate += delta;

        val targetTime = timeBetweenIncomingMiners
        if (targetTime != null && lastMiningUpdate >= targetTime&&sendOutTime>minerRoundTripTime) { //
            lastMiningUpdate -= targetTime
            resourceInventory.triangles++
            if (!doorIsOpen) {
                val newMinersOutside = minerMinionData.minionCountOutside - 1f
                if (newMinersOutside < 0f) {
                    minerMinionData.minionCountInside += minerMinionData.minionCountOutside
                    minerMinionData.minionCountOutside = 0f
                } else {
                    minerMinionData.minionCountInside += 1f
                    minerMinionData.minionCountOutside -= 1f
                }
            }
        }
    }

    fun onRepairClicked() {
        if (!canRepairFactory()) return
        soundController.playRepairSound()
        val repairAmount = min(factoryHp.missing, resourceInventory / factoryRepairCostPerHpPoint)
        resourceInventory -= factoryRepairCostPerHpPoint * repairAmount
        factoryHp.heal(repairAmount)
    }

    fun canRepairFactory(): Boolean {
        return when {
            factoryHp.isFull -> false
            factoryRepairCostPerHpPoint !in resourceInventory -> false
            else -> true
        }
    }

    fun getFullRepairCost(): ResourcePackage {
        return factoryRepairCostPerHpPoint * factoryHp.missing
    }

    fun onUpgradeFactoryClicked(minionType: MinionType) {
        if (!canUpgradeFactory(minionType)) return
        resourceInventory -= getUpgradeCost(minionType)
        factoryUpgradeCostCache -= minionType
        this[minionType].factoryLevel++
    }

    fun canUpgradeFactory(minionType: MinionType): Boolean {
        return getUpgradeCost(minionType) in resourceInventory
    }

    fun getUpgradeCost(minionType: MinionType): ResourcePackage {
        return factoryUpgradeCostCache.getOrPut(minionType) {
            val level = this[minionType].factoryLevel
            factoryUpgradeCost(minionType, level)
        }
    }

    fun onToggleDoorClicked() {
        doorIsOpen = !doorIsOpen
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