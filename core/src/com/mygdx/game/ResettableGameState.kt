package com.mygdx.game

import com.mygdx.game.assets.AssetDescriptors
import com.mygdx.game.common.soundController
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt


class ResettableGameState(
    var factoryHp: Hp = Hp(total = 100),
    var doorIsOpen: Boolean = true,

    var tankMinionData: MinionProduction = MinionProduction(MinionType.Tank, tankBaseAttack, tankBaseDefense),
    var archerMinionData: MinionProduction = MinionProduction(MinionType.Archer, archerBaseAttack, archerBaseDefense),
    var minerMinionData: MinionProduction = MinionProduction(MinionType.Miner, 0f, minerBaseDefense),

    var resourceInventory: ResourcePackage = ResourcePackage(triangles = 100),

    var bossLevel: Int = 1,
    var bossHp: Hp = Hp(total = 10),
) {
    var bossFightState: BossFight = BossFight(this)

    var lastMiningUpdate = 0f
    var timeBetweenIncomingMiners: Float? = null
    var sendOutTime = 0f;
    var roundTripShortening = 1f;

    var basicAttack: Attack = Attack(1f, null)
    var waitAttack: Attack = Attack(0f, null)
    var bosses = mutableListOf(
        Boss(1, assetManager.get(AssetDescriptors.BOSS), "Bööööses Monster", listOf(basicAttack)),
        Boss(1, assetManager.get(AssetDescriptors.BOSS), "So Bööööses Monster", listOf(basicAttack, waitAttack))
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


    private fun calculateMiningFrame(delta: Float) {


        val minionCountOutside = minerMinionData.minionCountOutside
        if (minionCountOutside <= 0) {
            sendOutTime = 0f
            lastMiningUpdate = 0f
        }
        if (!doorIsOpen && minionCountOutside <= 0f) return

        if (doorIsOpen) {
            timeBetweenIncomingMiners = when (minionCountOutside) {
                0f -> null
                else -> baseMinerRoundTripTime*roundTripShortening / minionCountOutside
            }
        }

        sendOutTime += delta
        lastMiningUpdate += delta

        val targetTime = timeBetweenIncomingMiners
        if (targetTime != null && lastMiningUpdate >= targetTime && sendOutTime > baseMinerRoundTripTime) {
            val amount = (lastMiningUpdate / targetTime).toInt()
            lastMiningUpdate -= targetTime * amount
            resourceInventory.triangles += amount
            if (!doorIsOpen) {
                val newMinersOutside = minerMinionData.minionCountOutside - amount
                if (newMinersOutside < 0f) {
                    minerMinionData.minionCountInside += minerMinionData.minionCountOutside
                    minerMinionData.minionCountOutside = 0f
                } else {
                    minerMinionData.minionCountInside += amount
                    minerMinionData.minionCountOutside -= amount
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
