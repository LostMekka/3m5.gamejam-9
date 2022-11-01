package com.mygdx.game

import kotlin.math.min

class PersistentGameState {
    var resettableState = ResettableGameState()
    private fun levelMediator(): Int {
        var sum = 0
        for (minionType in MinionType.values()) {
            sum += resettableState[minionType].factoryLevel
        }
        return sum / 3

    }

    fun onGGPressed() {
        // TODO: animation?
        // TODO: sound?
        val pentas = resettableState.resourceInventory.pentas
        val tankDefenseMultiplier = resettableState.tankMinionData.defenceMultiplier
        // Unused, included for completeness
        val tankAttackMultiplier = resettableState.tankMinionData.attackMultiplier
        val archerDefenseMultiplier = resettableState.archerMinionData.defenceMultiplier
        val archerAttackMultiplier = resettableState.archerMinionData.attackMultiplier
        val minerDefenseMultiplier = resettableState.minerMinionData.defenceMultiplier
        val roundTripShortening = resettableState.roundTripShortening

        resettableState = ResettableGameState()
        resettableState.resourceInventory.pentas = pentas
        resettableState.tankMinionData.defenceMultiplier = tankDefenseMultiplier
        resettableState.tankMinionData.attackMultiplier = tankAttackMultiplier
        resettableState.archerMinionData.defenceMultiplier = archerDefenseMultiplier
        resettableState.archerMinionData.attackMultiplier = archerAttackMultiplier
        resettableState.minerMinionData.defenceMultiplier = minerDefenseMultiplier
        resettableState.roundTripShortening = roundTripShortening
    }

    // ATTACK

    fun onUpgradeBaseAttack(minionType: MinionType) {
        if (canUpgradeBaseAttack(minionType)) {
            resettableState.resourceInventory -= getBaseAttackUpgradeCost(minionType)
            resettableState[minionType].attackMultiplier *= 1.2f
        }
    }

    fun canUpgradeBaseAttack(minionType: MinionType): Boolean =
        getBaseAttackUpgradeCost(minionType) in resettableState.resourceInventory

    fun getBaseAttackUpgradeCost(minionType: MinionType): ResourcePackage =
        strengthUpgradeCost(resettableState[minionType].attackMultiplier)

    // DEFENSE

    fun onUpgradeDefence(minionType: MinionType) {
        if (canUpgradeDefense(minionType)) {
            resettableState.resourceInventory -= getDefenseUpgradeCost(minionType)
            resettableState[minionType].defenceMultiplier *= 1.2f
        }
    }

    fun canUpgradeDefense(minionType: MinionType): Boolean =
        getDefenseUpgradeCost(minionType) in resettableState.resourceInventory

    fun getDefenseUpgradeCost(minionType: MinionType): ResourcePackage =
        strengthUpgradeCost(resettableState[minionType].defenceMultiplier)

    // MINER ROUND-TRIP

    fun onUpgradeRoundTrip() {
        if (canUpgradeRoundTrip()) {
            resettableState.resourceInventory -= getRoundTripUpgradeCost();
            resettableState.roundTripShortening *= 0.9f;
        }
    }

    fun canUpgradeRoundTrip() =
        getRoundTripUpgradeCost() in resettableState.resourceInventory

    fun getRoundTripUpgradeCost() =
        roundtripUpgradeCost(resettableState.roundTripShortening)

    fun calculateFrame(delta: Float) {
        resettableState.calculateFrame(delta)
        resettableState.factoryHp =
            Hp(levelMediator() * 100, min(resettableState.factoryHp.current, levelMediator() * 100))
    }
}

class MinionMultiplier(
    val minionType: MinionType,
    var attackMultiplier: Float = 1f,
    var defenceMultiplier: Float = 1f,
)
